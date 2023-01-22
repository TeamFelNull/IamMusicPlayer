package dev.felnull.imp.client.music.player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import dev.felnull.fnjl.concurrent.InvokeExecutor;
import dev.felnull.imp.api.client.MusicSpeakerAccess;
import dev.felnull.imp.client.music.AudioInfo;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.music.speaker.MusicBuffer;
import dev.felnull.imp.client.music.speaker.MusicSpeaker;
import dev.felnull.imp.client.util.MusicUtils;
import dev.felnull.imp.music.MusicSpeakerFixedInfo;
import dev.felnull.imp.music.resource.MusicSource;
import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseMusicPlayer implements MusicPlayer<BaseMusicPlayer.LoadInput, BaseMusicPlayer.LoadResult> {
    private final InvokeExecutor tickExecutor = new InvokeExecutor();
    private final Map<UUID, MusicSpeaker> speakers = new HashMap<>();
    private final Map<UUID, MusicSpeaker> preSpeakers = new HashMap<>();
    private final List<ReadEntry> readEntries = new ArrayList<>();
    private final Map<Long, ReadWait> waits = new HashMap<>();
    private final AudioInfo audioInfo;
    private final MusicSource musicSource;
    private final int aheadLoad;
    private final Object readLock = new Object();
    private final AtomicBoolean readStreamEnd = new AtomicBoolean();
    private final UUID musicPlayerId;
    private boolean destroy;
    private boolean loaded;
    private boolean playing;
    private boolean pause;
    private boolean reading;
    private boolean finished;
    private boolean loadEnd;
    private long delay;
    private long startPosition = -1;
    private long startTime = -1;
    private long pauseTime = -1;
    private long totalPauseTime;
    private long totalReadTime;
    private AudioInputStream stream;

    protected BaseMusicPlayer(UUID musicPlayerId, AudioInfo audioInfo, MusicSource musicSource, int aheadLoad) {
        this.audioInfo = audioInfo;
        this.musicSource = musicSource;
        this.aheadLoad = aheadLoad;
        this.musicPlayerId = musicPlayerId;
    }

    @Override
    public List<MusicSpeaker> getSpeakerList() {
        ImmutableList.Builder<MusicSpeaker> speakerBuilder = new ImmutableList.Builder<>();
        speakerBuilder.addAll(speakers.values());
        speakerBuilder.addAll(preSpeakers.values());
        return speakerBuilder.build();
    }

    @Override
    public void play(long delay) {
        this.delay = delay;
        this.startTime = System.currentTimeMillis();
        this.playing = true;

        if (pause)
            this.pauseTime = System.currentTimeMillis();

        for (MusicSpeaker value : speakers.values()) {
            speakerUpdate(value);
        }

        for (UUID uuid : preSpeakers.keySet()) {
            updatePreSpeaker(uuid);
        }
    }


    @Override
    public void destroy() throws Exception {
        if (this.destroy)
            return;

        this.destroy = true;
        this.playing = false;

        stopReadStream();

        CompletableFuture.runAsync(() -> {
            try {
                closeAudioStream();
                synchronized (readLock) {
                    if (stream != null)
                        stream.close();
                }
            } catch (Exception e) {
                MusicEngine.getInstance().getLogger().error("Failed to close music stream", e);
            }
        }, MusicEngine.getInstance().getMusicAsyncExecutor());

        for (MusicSpeaker value : speakers.values()) {
            value.destroy();
        }

        for (MusicSpeaker value : preSpeakers.values()) {
            value.destroy();
        }

        for (ReadEntry readEntry : readEntries) {
            for (MusicBuffer value : readEntry.buffers.values()) {
                if (value.canRelease()) value.release();
            }
        }
    }

    @Override
    public boolean isDestroy() {
        return this.destroy;
    }

    @Override
    public void pause() {
        if (playing)
            this.pauseTime = System.currentTimeMillis();

        this.pause = true;

        for (MusicSpeaker value : speakers.values()) {
            value.pause();
        }
        for (MusicSpeaker value : preSpeakers.values()) {
            value.pause();
        }

        if (musicSource.isLive())
            finished = true;
    }

    @Override
    public void resume() {
        if (playing) {
            this.totalPauseTime += System.currentTimeMillis() - this.pauseTime;
            this.pauseTime = -1;
        }
        this.pause = false;

        for (MusicSpeaker value : speakers.values()) {
            value.resume();
        }
        for (MusicSpeaker value : preSpeakers.values()) {
            value.resume();
        }
    }

    @Override
    public void tick() {
        MusicUtils.runInvokeTasks(tickExecutor, "Music Player");


        if (!this.loaded) return;

        List<UUID> delSpeakers = new ArrayList<>();

        speakers.forEach((id, speaker) -> {
            if (speaker.isDead()) {
                try {
                    speaker.destroy();
                } catch (Exception e) {
                    MusicEngine.getInstance().getLogger().error("Failed to destroy speaker", e);
                }
                delSpeakers.add(id);
                return;
            }
            speaker.tick();
            speakerUpdate(speaker);
            speaker.releaseBuffers();
        });

        for (ReadEntry readEntry : readEntries) {
            if (readEntry.position() >= getPosition()) continue;

            List<MusicSpeakerFixedInfo> dels = new ArrayList<>();
            for (Map.Entry<MusicSpeakerFixedInfo, MusicBuffer> entry : readEntry.buffers.entrySet()) {
                if (entry.getValue().canRelease()) {
                    entry.getValue().release();
                    dels.add(entry.getKey());
                }
            }
            for (MusicSpeakerFixedInfo del : dels) {
                readEntry.buffers.remove(del);
            }
        }


        for (UUID delSpeaker : delSpeakers) {
            preSpeakers.put(delSpeaker, new MusicSpeaker(musicPlayerId, delSpeaker, speakers.get(delSpeaker).getTracker()));
            speakers.remove(delSpeaker);
        }

        List<ReadEntry> delREs = new ArrayList<>();
        for (ReadEntry readEntry : readEntries) {
            if (readEntry.buffers.isEmpty()) delREs.add(readEntry);
        }
        readEntries.removeAll(delREs);

        readTick();
    }


    private void readTick() {
        int lc = readEntries.size();

        if (lc == 0 && isPlaying()) {
            finished = true;
            return;
        }

        int nc;

        if (musicSource.isLive()) {
            nc = aheadLoad;
        } else {
            nc = (int) ((float) (musicSource.getDuration() - getPosition()) / 1000f + 0.5f);
        }

        if (!finished && !loadEnd && !reading && (lc <= aheadLoad || (nc - aheadLoad) <= aheadLoad)) {
            var cf = CompletableFuture.supplyAsync(() -> {
                return readStart(aheadLoad);
            }, tickExecutor).thenApplyAsync(ret -> {
                try {
                    return readAsync(ret);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, MusicEngine.getInstance().getMusicAsyncExecutor());

            cf.whenCompleteAsync((ret, error) -> {
                if (ret != null && error == null) {
                    readApply(ret);
                    return;
                }
                MusicEngine.getInstance().getLogger().error("Failed to read audio data", error);
            }, tickExecutor);
        }
    }

    private void updatePreSpeaker(UUID uuid) {
        if (pause) return;

        var sp = preSpeakers.get(uuid);
        if (sp == null) return;
        var r = readEntries.stream().filter(readEntry -> readEntry.position() >= getPosition()).sorted((o1, o2) -> (int) (o1.position() - o2.position())).toList();

        if (r.stream().allMatch(n -> n.buffers.containsKey(sp.getFixedInfo()))) {
            long min = Long.MAX_VALUE;
            for (ReadEntry readEntry : r) {
                min = Math.min(min, readEntry.position());
                sp.insertBuffer(readEntry.buffers.get(sp.getFixedInfo()));
            }

            sp.setScheduledStartTime(min - getPosition());
            preSpeakers.remove(uuid);
            speakers.put(uuid, sp);
        } else {

            List<ReadEntry> noLoaded = r.stream().filter(n -> !n.buffers.containsKey(sp.getFixedInfo())).toList();
            for (ReadEntry readEntry : noLoaded) {
                var rw = new ReadWait(readEntry, sp.getFixedInfo());
                if (waits.containsKey(readEntry.position)) continue;

                var mli = middleLoadStart(rw);

                var cf = CompletableFuture.supplyAsync(() -> {
                    return middleLoadAsync(mli);
                }, MusicEngine.getInstance().getMusicAsyncExecutor());

                cf.whenCompleteAsync((ret, error) -> {
                    if (ret != null && error == null) {
                        middleLoadApply(ret);
                        return;
                    }
                    MusicEngine.getInstance().getLogger().error("Failed to middle load audio data", error);
                }, tickExecutor);
            }
        }
    }

    private void speakerUpdate(MusicSpeaker speaker) {
        if (!this.pause && playing && !speaker.isPlaying() && speaker.canPlay())
            speaker.play(0);
    }

    @Override
    public void addSpeaker(UUID speakerId, MusicSpeaker speaker) {
        preSpeakers.put(speakerId, speaker);
        if (loaded) updatePreSpeaker(speakerId);
    }

    @Override
    public void removeSpeaker(UUID uuid) {
        var sp = speakers.remove(uuid);
        if (sp == null) sp = preSpeakers.remove(uuid);

        if (sp != null) {
            try {
                sp.destroy();
            } catch (Exception e) {
                MusicEngine.getInstance().getLogger().error("Failed to destroy speaker", e);
            }
        }
    }

    @Override
    public boolean existSpeaker(UUID uuid) {
        if (speakers.containsKey(uuid))
            return true;
        return preSpeakers.containsKey(uuid);
    }

    @Override
    public MusicSpeaker getSpeaker(UUID uuid) {
        var sp = speakers.get(uuid);
        if (sp == null) sp = preSpeakers.get(uuid);
        return sp;
    }

    @Override
    public int getSpeakerCount() {
        return speakers.size() + preSpeakers.size();
    }

    @Override
    public LoadInput loadStart(long position) {
        this.startPosition = position;

        return new LoadInput(position, ImmutableMap.copyOf(preSpeakers), aheadLoad, musicSource.isLive());
    }

    abstract protected AudioInputStream openAudioStream(long position) throws Exception;

    abstract protected void closeAudioStream() throws Exception;

    @Override
    public LoadResult loadAsync(LoadInput input) throws Exception {
        var stream = openAudioStream(input.position);
        var readInput = new ReadInput(stream, input.position, input.aheadLoad, input.speakers.values().stream().map(MusicSpeaker::getFixedInfo).distinct().toList(), getAudioInfo());
        var readRet = readAsync(readInput);

        if (input.live())
            Thread.sleep(1000 * 3);

        return new LoadResult(stream, readRet, input.speakers.keySet().stream().toList());
    }

    @Override
    public void loadApply(LoadResult loadedData) {
        this.loaded = true;
        this.stream = loadedData.stream;

        var spks = loadedData.speakers;
        for (UUID spk : spks) {
            var sp = preSpeakers.remove(spk);
            if (sp != null) speakers.put(spk, sp);
        }

        var rd = loadedData.readResults;
        readApply(rd);
    }

    @Override
    public float getCurrentAudioWave(int channel) {
        for (ReadEntry readEntry : readEntries) {
            long ed = readEntry.instantaneous().sampleTime();
            long st = ed - 1000;
            if (getPosition() >= st && getPosition() < ed) {
                long rp = getPosition() - st;
                int fp = (int) (rp / 1000f * 60f);
                return readEntry.instantaneous().getWaves(channel)[fp];
            }
        }
        return 0;
    }

    @Override
    public long getPosition() {
        if (this.startTime < 0)
            return this.startPosition + delay;

        long pt = this.totalPauseTime;
        if (this.pauseTime >= 0)
            pt += System.currentTimeMillis() - this.pauseTime;
        return System.currentTimeMillis() - this.startTime - pt + this.startPosition + this.delay;
    }


    @Override
    public AudioInfo getAudioInfo() {
        return audioInfo;
    }

    @Override
    public boolean waitDestroy() {
        return finished || (!musicSource.isLive() && getPosition() >= musicSource.getDuration());
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public boolean isPause() {
        return pause;
    }

    @Override
    public Map<UUID, MusicSpeakerAccess> getSpeakers() {
        ImmutableMap.Builder<UUID, MusicSpeakerAccess> speakersBuilder = ImmutableMap.builder();
        speakersBuilder.putAll(speakers);
        speakersBuilder.putAll(preSpeakers);
        return speakersBuilder.build();
    }

    private ReadInput readStart(int loadCount) {
        this.reading = true;
        List<MusicSpeakerFixedInfo> infos = new ArrayList<>();
        infos.addAll(speakers.values().stream().map(MusicSpeaker::getFixedInfo).distinct().toList());
        infos.addAll(preSpeakers.values().stream().map(MusicSpeaker::getFixedInfo).distinct().toList());

        return new ReadInput(this.stream, this.totalReadTime + this.startPosition, loadCount, infos.stream().distinct().toList(), getAudioInfo());
    }

    protected void stopReadStream() {
        readStreamEnd.set(true);
    }


    private boolean readStream(AudioInputStream stream, byte[] buffer) throws IOException {
        synchronized (readLock) {
            try {
                if (stream.read(buffer) < 0)
                    return false;
            } catch (InterruptedIOException ex) {
                return false;
            }
        }
        return !readStreamEnd.get();
    }

    private ReadResult readAsync(ReadInput input) throws Exception {
        int ol = input.audioInfo().sampleRate() * input.audioInfo().channel() * (input.audioInfo().bit() / 8);
        byte[] buffer = new byte[ol];
        List<ReadResultEntry> entries = new ArrayList<>();
        long st = input.readStartPosition();
        boolean end = false;

        for (int i = 0; i < input.aheadLoad(); i++) {
            if (isDestroy()) break;

            if (!musicSource.isLive() && (st + 1000) > musicSource.getDuration()) {
                Arrays.fill(buffer, (byte) 0);
            }

            end = !readStream(input.stream, buffer);

            byte[] data = buffer.clone();
            MusicInstantaneous mi = MusicInstantaneous.create(st, data, input.audioInfo().channel(), input.audioInfo.bit());

            Map<MusicSpeakerFixedInfo, BufferFactory> buffes = new HashMap<>();
            for (MusicSpeakerFixedInfo fixedInfo : input.fixedInfos()) {
                buffes.put(fixedInfo, BufferFactory.create(data, fixedInfo, input.audioInfo()));
            }
            entries.add(new ReadResultEntry(st, data, mi, buffes));

            st += 1000;

            if (end) break;
        }

        return new ReadResult(entries, end);
    }

    private void readApply(ReadResult readResult) {
        if (readResult.end())
            loadEnd = true;

        totalReadTime += readResult.readEntries().size() * 1000L;
        for (ReadResultEntry readEntry : readResult.readEntries()) {
            var re = readEntry.create();
            readEntries.add(re);
            re.buffers().forEach(this::insertBuffer);
        }

        this.reading = false;

        for (UUID uuid : preSpeakers.keySet()) {
            updatePreSpeaker(uuid);
        }
    }

    private void insertBuffer(MusicSpeakerFixedInfo info, MusicBuffer buffer) {
        for (MusicSpeaker value : speakers.values()) {
            if (!value.isDead() && value.getFixedInfo().equals(info)) value.insertBuffer(buffer);

            speakerUpdate(value);
        }
    }

    private MiddleLoadInput middleLoadStart(ReadWait readWait) {
        waits.put(readWait.readEntry.position, readWait);
        return new MiddleLoadInput(readWait, getAudioInfo());
    }

    private MiddleLoadResult middleLoadAsync(MiddleLoadInput input) {
        byte[] data = input.readWaiter().readEntry().data();
        return new MiddleLoadResult(input.readWaiter(), BufferFactory.create(data, input.readWaiter().fixedInfo(), input.audioInfo()));
    }

    private void middleLoadApply(MiddleLoadResult result) {
        if (result.readWaiter.readEntry.position() < getPosition()) return;

        result.readWaiter.readEntry().buffers.put(result.readWaiter.fixedInfo, result.bufferFactory().create());

        waits.remove(result.readWaiter.readEntry.position);

        for (UUID uuid : preSpeakers.keySet()) {
            updatePreSpeaker(uuid);
        }
    }

    @Override
    public int getTaskCount() {
        return tickExecutor.getTaskCount();
    }

    @Override
    public long getMaxWaitTime() {
        return aheadLoad * 1000L;
    }

    @Override
    public List<MusicLoadChunk> getLoadChunks() {
        return readEntries.stream().map(n -> new MusicLoadChunk(n.position(), 1000)).toList();
    }

    public static record LoadInput(long position, Map<UUID, MusicSpeaker> speakers, int aheadLoad, boolean live) {
    }

    public static record LoadResult(AudioInputStream stream, ReadResult readResults, List<UUID> speakers) {
    }

    private static record ReadInput(AudioInputStream stream, long readStartPosition, int aheadLoad,
                                    List<MusicSpeakerFixedInfo> fixedInfos, AudioInfo audioInfo) {
    }

    private static record ReadResult(List<ReadResultEntry> readEntries, boolean end) {
    }

    private static record ReadResultEntry(long position, byte[] data, MusicInstantaneous instantaneous,
                                          Map<MusicSpeakerFixedInfo, BufferFactory> buffers) {
        private ReadEntry create() {
            Map<MusicSpeakerFixedInfo, MusicBuffer> rb = new HashMap<>();
            buffers.forEach((fixedInfo, bufferFactory) -> rb.put(fixedInfo, bufferFactory.create()));
            return new ReadEntry(position, data, instantaneous, rb);
        }
    }

    private static record ReadEntry(long position, byte[] data, MusicInstantaneous instantaneous,
                                    Map<MusicSpeakerFixedInfo, MusicBuffer> buffers) {
    }

    private static record ReadWait(ReadEntry readEntry, MusicSpeakerFixedInfo fixedInfo) {
    }

    private static record BufferFactory(ByteBuffer buffer, boolean relative, AudioInfo audioData) {
        private BufferFactory(byte[] data, boolean relative, AudioInfo audioData) {
            this(getBuffer(data), relative, audioData);
        }

        private static BufferFactory create(byte[] data, MusicSpeakerFixedInfo fixedInfo, AudioInfo audioInfo) {
            if (MusicUtils.isSpatial(fixedInfo.spatialType()) && fixedInfo.channel() <= -1 && audioInfo.channel() >= 2) {
                data = synthesisChannel(data, audioInfo);
                audioInfo = new AudioInfo(1, audioInfo.sampleRate(), audioInfo.bit());
            } else if (fixedInfo.channel() >= 0 && audioInfo.channel() >= 2) {
                data = extractChannel(data, fixedInfo.channel(), audioInfo);
                audioInfo = new AudioInfo(1, audioInfo.sampleRate(), audioInfo.bit());
            }
            return new BufferFactory(data, !MusicUtils.isSpatial(fixedInfo.spatialType()), audioInfo);
        }

        private static byte[] synthesisChannel(byte[] data, AudioInfo audioInfo) {
            if (audioInfo.bit() != 16) throw new RuntimeException("Unsupported bit");

            byte[] ndata = new byte[data.length / audioInfo.channel()];

            for (int i = 0; i < data.length / 2 / audioInfo.channel(); i++) {
                float total = 0;
                for (int j = 0; j < audioInfo.channel(); j++) {
                    int l1 = i * 2 * audioInfo.channel() + 2 * j;
                    int l2 = l1 + 1;

                    byte[] d = {data[l1], data[l2]};
                    short r = ByteBuffer.wrap(d).order(ByteOrder.LITTLE_ENDIAN).getShort();
                    float val = (float) r / (float) Short.MAX_VALUE;
                    total += val;
                }
                byte[] ns = shortToByteArray((short) ((float) Short.MAX_VALUE * (total / (float) audioInfo.channel())));

                int n1 = i * 2;
                int n2 = i * 2 + 1;

                ndata[n1] = ns[0];
                ndata[n2] = ns[1];
            }

            return ndata;
        }

        private static byte[] shortToByteArray(short val) {
            ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            buffer.putShort(val);
            return buffer.array();
        }

        private static byte[] extractChannel(byte[] data, int channel, AudioInfo audioInfo) {
            if (audioInfo.bit() != 16) throw new RuntimeException("Unsupported bit");

            byte[] ndata = new byte[data.length / audioInfo.channel()];

            for (int i = 0; i < data.length / 2 / audioInfo.channel(); i++) {
                int l1 = i * 2 * audioInfo.channel() + 2 * channel;
                int l2 = l1 + 1;

                int n1 = i * 2;
                int n2 = i * 2 + 1;

                ndata[n1] = data[l1];
                ndata[n2] = data[l2];
            }

            return ndata;
        }

        private static ByteBuffer getBuffer(byte[] array) {
            ByteBuffer audioBuffer2 = BufferUtils.createByteBuffer(array.length);
            audioBuffer2.put(array);
            audioBuffer2.flip();
            return audioBuffer2;
        }

        private MusicBuffer create() {
            return new MusicBuffer(buffer, relative, audioData);
        }
    }

    private static record MiddleLoadInput(ReadWait readWaiter, AudioInfo audioInfo) {
    }

    private static record MiddleLoadResult(ReadWait readWaiter, BufferFactory bufferFactory) {
    }
}