package dev.felnull.imp.client.nmusic.player;

import dev.felnull.imp.client.nmusic.MusicEngine;
import dev.felnull.imp.client.nmusic.MusicInstantaneous;
import dev.felnull.imp.client.nmusic.speaker.MusicSpeaker;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBuffer;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBufferSpeakerData;
import dev.felnull.imp.client.util.ALUtils;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public abstract class BaseMusicPlayer implements MusicPlayer {
    private static final Minecraft mc = Minecraft.getInstance();
    private final Queue<BufferEntry> buffers = new LinkedList<>();
    private final List<BufferEntry> bufferInserted = new ArrayList<>();
    private final Map<UUID, MusicSpeaker<? extends MusicBuffer<?>>> speakers = new HashMap<>();
    private final byte[] buffer;
    private final int channel;
    private final int sampleRate;
    private final int bit;
    private final int aheadLoad;
    private boolean loading;
    private boolean ready;
    private boolean playing;
    private boolean initSpeakerLoad;
    private boolean destroy;
    private AudioInputStream stream;
    private MusicInstantaneous lastInstantaneous;

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

            CompletableFuture.runAsync(() -> {
                for (int i = 0; i < r; i++) {
                    try {
                        if (!nextBuffer())
                            break;
                    } catch (IOException | ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, MusicEngine.getInstance().getMusicLoaderExecutor());
        });
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

                        if (fl)
                            bufferEntry.insertedSpeakers.remove(speaker);

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
        forEachAvailableSpeakers(MusicSpeaker::pause);
    }

    @Override
    public void resume() {
        forEachAvailableSpeakers(MusicSpeaker::resume);
    }

    @Override
    public void destroy() throws Exception {
        destroy = true;
        playing = false;

        synchronized (speakers) {
            for (MusicSpeaker<? extends MusicBuffer<?>> value : speakers.values()) {
                value.destroy();
            }
        }

        this.stream.close();
    }

    @Override
    public boolean isDestroy() {
        return destroy;
    }

    @Override
    public void load(long position) throws Exception {
        this.loading = true;

        this.stream = loadAudioStream(position);
        initSpeakerLoad = true;

        for (int i = 0; i < aheadLoad; i++) {
            if (!readAudioStream())
                break;
            buffers.add(new BufferEntry(buffer.clone(), convertSpeakerBuffer(buffer), new ArrayList<>()));
        }

        for (int i = 0; i < 3; i++) {
            if (!slideBuffer())
                break;
        }

        this.loading = false;
        this.ready = true;
    }

    private synchronized boolean nextBuffer() throws IOException, ExecutionException, InterruptedException {
        if (isDestroy())
            return false;

        if (!readAudioStream())
            return false;

        synchronized (buffers) {
            buffers.add(new BufferEntry(buffer.clone(), convertSpeakerBuffer(buffer), new ArrayList<>()));
        }

        return slideBuffer();
    }

    private synchronized boolean slideBuffer() {
        BufferEntry br;
        synchronized (buffers) {
            br = buffers.poll();
        }
        if (br == null)
            return false;

        synchronized (bufferInserted) {
            bufferInserted.add(br);
        }

        ALUtils.runOnSoundThread(() -> {
            br.data().forEach((k, e) -> {
                var r = insetBuffer(k, e);
                synchronized (br.insertedSpeakers) {
                    br.insertedSpeakers.addAll(r);
                }
            });
        });
        return true;
    }

    abstract protected AudioInputStream loadAudioStream(long position) throws Exception;

    private <T, E extends MusicSpeaker<MusicBuffer<T>>> Map<Pair<Class<E>, MusicBufferSpeakerData>, MusicBuffer<T>> convertSpeakerBuffer(byte[] data) throws ExecutionException, InterruptedException {
        var me = MusicEngine.getInstance();
        Map<Pair<Class<E>, MusicBufferSpeakerData>, E> scs = new HashMap<>();

        forEachAvailableSpeakers(sp -> {
            var cs = (Class<E>) sp.getClass();
            if (!scs.containsKey(scs))
                scs.put(Pair.of(cs, sp.getBufferSpeakerData()), (E) sp);
        });

        Map<Pair<Class<E>, MusicBufferSpeakerData>, MusicBuffer<T>> ret = new HashMap<>();
        scs.forEach((c, s) -> mc.submit(() -> {
            ret.put(Pair.of(c.getLeft(), c.getRight()), s.createBuffer());
        }).join());

        List<CompletableFuture<Triple<MusicBuffer<T>, T, MusicBufferSpeakerData>>> cfs = new ArrayList<>();
        ret.forEach((c, s) -> {
            var cf = CompletableFuture.supplyAsync(() -> Triple.of(s, s.asyncConvertBuffer(data, sampleRate, channel, bit), c.getRight()), me.getMusicLoaderExecutor());
            cfs.add(cf);
        });

        ALUtils.runOnSoundThread(() -> {
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
    private synchronized boolean readAudioStream() throws IOException {
        return stream.read(buffer) >= 0;
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
        this.playing = true;
        forEachAvailableSpeakers(MusicSpeaker::play);
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
        return speakers.size();
    }

    private void forEachAvailableSpeakers(Consumer<MusicSpeaker<? extends MusicBuffer<?>>> speakerConsumer) {
        synchronized (speakers) {
            for (MusicSpeaker<? extends MusicBuffer<?>> value : speakers.values()) {
                if (!value.isDead())
                    speakerConsumer.accept(value);
            }
        }
    }

    private static record BufferEntry(byte[] rawData,
                                      Map<Pair<Class<MusicSpeaker<MusicBuffer<Object>>>, MusicBufferSpeakerData>, MusicBuffer<Object>> data,
                                      List<MusicSpeaker<? extends MusicBuffer<?>>> insertedSpeakers) {

    }
}
