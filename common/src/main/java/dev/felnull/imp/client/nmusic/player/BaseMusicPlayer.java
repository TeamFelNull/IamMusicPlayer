package dev.felnull.imp.client.nmusic.player;

import com.google.common.collect.ImmutableMap;
import dev.felnull.imp.client.nmusic.MusicEngine;
import dev.felnull.imp.client.nmusic.speaker.MusicSpeaker;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBuffer;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBufferSpeakerData;
import dev.felnull.imp.client.nmusic.task.MusicTaskRunner;
import dev.felnull.imp.client.util.MusicUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public abstract class BaseMusicPlayer implements MusicPlayer {
    private final Queue<BufferEntry> buffers = new LinkedList<>();
    private final List<BufferEntry> bufferInserted = new ArrayList<>();
    private final Map<UUID, MusicSpeaker<? extends MusicBuffer<?>>> speakers = new HashMap<>();
    private final Object SLIDE_LOCK = new Object();
    private final Object NEXT_LOCK = new Object();
    private final Object READ_LOCK = new Object();
    private final byte[] buffer;
    private long totalSampleTime;
    private long sampleTime;
    private long delay;
    private long startPosition;
    private long startTime = -1;
    private long pauseTime = -1;
    private long totalPauseTime;
    private final int channel;
    private final int sampleRate;
    private final int bit;
    private final int aheadLoad;
    private boolean loading;
    private boolean ready;
    private boolean playing;
    private boolean initSpeakerLoad;
    private boolean destroy;
    private boolean streamDead;
    private boolean streamReading;
    private AudioInputStream stream;

    protected BaseMusicPlayer(int channel, int sampleRate, int bit, int aheadLoad) {
        this.buffer = new byte[sampleRate * channel * (bit / 8)];
        this.channel = channel;
        this.sampleRate = sampleRate;
        this.bit = bit;
        this.aheadLoad = aheadLoad;
    }

    @Override
    public void addSpeaker(UUID speakerId, MusicSpeaker<?> speaker) {
        if (!initSpeakerLoad) {
            synchronized (speakers) {
                this.speakers.put(speakerId, speaker);
            }
        }
    }

    @Override
    public MusicSpeaker<?> getSpeaker(UUID uuid) {
        synchronized (speakers) {
            return this.speakers.get(uuid);
        }
    }

    @Override
    public void tick() {
        forEachAvailableSpeakers(spk -> {
            spk.tick();

            var pl = spk.pollBuffers();
            int r = pollBufferCheck(spk, pl);

            for (int i = 0; i < r; i++)
                slideBuffer();
        });

        if (!streamReading && !streamDead) {
            int bs;
            synchronized (buffers) {
                bs = buffers.size();
            }

            if (bs <= (aheadLoad / 2)) {
                streamReading = true;
                CompletableFuture.runAsync(() -> {
                    try {
                        for (int i = 0; i < bs; i++) {
                            if (!nextBuffer()) {
                                streamDead = true;
                                break;
                            }
                        }
                    } catch (IOException | ExecutionException | InterruptedException e) {
                        streamDead = true;
                        MusicEngine.getInstance().getLogger().error("failed to get stream", e);
                    }
                    streamReading = false;
                }, MusicEngine.getInstance().getMusicLoaderExecutor());
            }

        }
    }

    private int pollBufferCheck(MusicSpeaker<? extends MusicBuffer<?>> speaker, List<? extends MusicBuffer<?>> buffers) {
        int ct = 0;
        for (MusicBuffer<?> buffer : buffers) {
            synchronized (bufferInserted) {
                List<BufferEntry> dels = new ArrayList<>();
                for (BufferEntry bufferEntry : bufferInserted) {
                    synchronized (bufferEntry.insertedSpeakers) {

                        boolean fl = false;
                        for (MusicBuffer<Object> value : bufferEntry.data.values()) {
                            if (value == buffer) {
                                fl = true;
                                break;
                            }
                        }

                        if (fl) bufferEntry.insertedSpeakers.remove(speaker);

                        if (bufferEntry.insertedSpeakers.isEmpty()) {
                            dels.add(bufferEntry);
                        }
                    }
                }

                for (BufferEntry del : dels) {
                    for (MusicBuffer<Object> value : del.data.values()) {
                        value.release();
                    }
                    bufferInserted.remove(del);
                    ct++;
                }
            }
        }
        return ct;
    }

    @Override
    public void pause() {
        this.pauseTime = System.currentTimeMillis();
        forEachAvailableSpeakers(MusicSpeaker::pause);
    }

    @Override
    public void resume() {
        this.totalPauseTime += System.currentTimeMillis() - this.pauseTime;
        this.pauseTime = -1;
        forEachAvailableSpeakers(MusicSpeaker::resume);
    }

    @Override
    public void destroy() throws Exception {
        destroy = true;
        playing = false;

        Map<UUID, MusicSpeaker<? extends MusicBuffer<?>>> copySpeakers;
        synchronized (speakers) {
            copySpeakers = ImmutableMap.copyOf(speakers);
        }

        for (MusicSpeaker<? extends MusicBuffer<?>> value : copySpeakers.values()) {
            value.destroy();
        }

        if (this.stream != null)
            this.stream.close();
    }

    @Override
    public boolean isDestroy() {
        return destroy;
    }

    @Override
    public void load(MusicTaskRunner runner, long position) throws Exception {
        this.startPosition = position;
        this.loading = true;

        this.stream = loadAudioStream(position);
        initSpeakerLoad = true;

        streamReading = true;
        for (int i = 0; i < aheadLoad; i++) {
            if (!nextBuffer()) break;
        }
        streamReading = false;

        for (int i = 0; i < 3; i++) {
            if (!slideBuffer()) break;
        }

        this.loading = false;
        this.ready = true;
    }

    private boolean nextBuffer() throws IOException, ExecutionException, InterruptedException {
        synchronized (NEXT_LOCK) {
            if (isDestroy()) return false;

            int len;
            if ((len = readAudioStream()) < 0) return false;

            var cdata = buffer.clone();

            var cp = convertSpeakerBuffer(cdata);
            MusicInstantaneous mi = MusicInstantaneous.create(sampleTime + startPosition, len, cdata, channel, sampleRate, bit);

            synchronized (buffers) {
                buffers.add(new BufferEntry(cdata, cp, new ArrayList<>(), mi));
            }

            return true;
        }
    }

    @Override
    public float getCurrentAudioWave(int channel) {
        synchronized (bufferInserted) {
            for (BufferEntry bufferEntry : bufferInserted) {
                long ed = bufferEntry.musicInstantaneous.sampleTime();
                long st = ed - 1000;
                if (getPosition() >= st && getPosition() < ed) {
                    long rp = getPosition() - st;
                    int fp = (int) (rp / 1000f * 60f);
                    return bufferEntry.musicInstantaneous.getWaves(channel)[fp];
                }
            }
        }
        return 0;
    }

    private boolean slideBuffer() {
        synchronized (SLIDE_LOCK) {
            BufferEntry br;
            synchronized (buffers) {
                br = buffers.poll();
            }
            if (br == null) return false;

            synchronized (bufferInserted) {
                bufferInserted.add(br);
            }

            MusicUtils.runOnMusicTick(() -> {
                br.data().forEach((k, e) -> {
                    var r = insetBuffer(k, e);
                    synchronized (br.insertedSpeakers) {
                        br.insertedSpeakers.addAll(r);
                    }
                });
            });
            return true;
        }
    }

    abstract protected AudioInputStream loadAudioStream(long position) throws Exception;

    private <T, E extends MusicSpeaker<MusicBuffer<T>>> Map<Pair<Class<E>, MusicBufferSpeakerData>, MusicBuffer<T>> convertSpeakerBuffer(byte[] data) throws ExecutionException, InterruptedException {
        var me = MusicEngine.getInstance();
        Map<Pair<Class<E>, MusicBufferSpeakerData>, E> scs = new HashMap<>();

        forEachAvailableSpeakers(sp -> {
            var cs = (Class<E>) sp.getClass();
            if (!scs.containsKey(scs)) scs.put(Pair.of(cs, sp.getBufferSpeakerData()), (E) sp);
        });

        Map<Pair<Class<E>, MusicBufferSpeakerData>, MusicBuffer<T>> ret = new HashMap<>();
        scs.forEach((c, s) -> {
            AtomicReference<MusicBuffer<T>> b = new AtomicReference<>();
            MusicUtils.runOnMusicTick(() -> b.set(s.createBuffer()));
            ret.put(Pair.of(c.getLeft(), c.getRight()), b.get());
        });

        List<CompletableFuture<Triple<MusicBuffer<T>, T, MusicBufferSpeakerData>>> cfs = new ArrayList<>();
        ret.forEach((c, s) -> {
            var cf = CompletableFuture.supplyAsync(() -> Triple.of(s, s.asyncConvertBuffer(data, sampleRate, channel, bit), c.getRight()), me.getMusicLoaderExecutor());
            cfs.add(cf);
        });

        MusicUtils.runOnMusicTick(() -> {
            for (CompletableFuture<Triple<MusicBuffer<T>, T, MusicBufferSpeakerData>> cf : cfs) {
                Triple<MusicBuffer<T>, T, MusicBufferSpeakerData> r;
                try {
                    r = cf.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
                r.getLeft().putBuffer(r.getMiddle(), r.getRight());
            }
        });

        return ret;
    }

    /**
     * AudioStreamから1秒間のデータを読み込む
     *
     * @return 端まで読み込み終ってないかどうか
     * @throws IOException 例外
     */
    private int readAudioStream() throws IOException {
        synchronized (READ_LOCK) {
            totalSampleTime += 1000;
            sampleTime = totalSampleTime;
            return stream.read(buffer);
        }
    }

    private <T extends MusicBuffer<?>> List<MusicSpeaker<T>> insetBuffer(Pair<Class<MusicSpeaker<T>>, MusicBufferSpeakerData> speakerDataPair, T buffer) {
        List<MusicSpeaker<T>> ret = new ArrayList<>();
        forEachAvailableSpeakers(sp -> {
            if (speakerDataPair.getLeft().isInstance(sp) && speakerDataPair.getRight().equals(sp.getBufferSpeakerData())) {
                var v = (MusicSpeaker<T>) sp;
                v.insertAudio(buffer);
                ret.add(v);
            }
        });
        return ret;
    }

    @Override
    public void play(long delay) {
        this.delay = delay;
        this.startTime = System.currentTimeMillis();
        this.playing = true;
        forEachAvailableSpeakers(speaker -> speaker.play(delay));
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public int getSpeakerCount() {
        synchronized (speakers) {
            return speakers.size();
        }
    }

    private void forEachAvailableSpeakers(Consumer<MusicSpeaker<? extends MusicBuffer<?>>> speakerConsumer) {
        Map<UUID, MusicSpeaker<? extends MusicBuffer<?>>> copySpeakers;
        synchronized (speakers) {
            copySpeakers = ImmutableMap.copyOf(speakers);
        }
        for (MusicSpeaker<? extends MusicBuffer<?>> value : copySpeakers.values()) {
            if (!value.isDead()) speakerConsumer.accept(value);
        }
    }

    @Override
    public long getPosition() {
        if (this.startTime < 0) return this.startPosition + delay;

        long pt = this.totalPauseTime;
        if (this.pauseTime >= 0) pt += System.currentTimeMillis() - this.pauseTime;
        return System.currentTimeMillis() - this.startTime - pt + this.startPosition + this.delay;
    }

    @Override
    public int getChannels() {
        return channel;
    }

    private static record BufferEntry(byte[] rawData,
                                      Map<Pair<Class<MusicSpeaker<MusicBuffer<Object>>>, MusicBufferSpeakerData>, MusicBuffer<Object>> data,
                                      List<MusicSpeaker<? extends MusicBuffer<?>>> insertedSpeakers,
                                      MusicInstantaneous musicInstantaneous) {

    }
}
