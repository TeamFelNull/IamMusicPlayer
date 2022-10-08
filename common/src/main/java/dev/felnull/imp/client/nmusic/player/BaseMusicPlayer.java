package dev.felnull.imp.client.nmusic.player;

import dev.felnull.imp.client.nmusic.MusicEngine;
import dev.felnull.imp.client.nmusic.MusicInstantaneous;
import dev.felnull.imp.client.nmusic.speaker.MusicSpeaker;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBuffer;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBufferSpeakerData;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public abstract class BaseMusicPlayer implements MusicPlayer {
    private static final Minecraft mc = Minecraft.getInstance();
    private final Map<UUID, MusicSpeaker<? extends MusicBuffer<?>>> speakers = new HashMap<>();
    private final byte[] buffer;
    private final int channel;
    private final int sampleRate;
    private final int bit;
    private AudioInputStream stream;
    private MusicInstantaneous lastInstantaneous;

    protected BaseMusicPlayer(int channel, int sampleRate, int bit) {
        this.buffer = new byte[sampleRate * channel * (bit / 8)];
        this.channel = channel;
        this.sampleRate = sampleRate;
        this.bit = bit;
    }

    @Override
    public void addSpeaker(UUID speakerId, MusicSpeaker<?> speaker) {
        synchronized (speakers) {
            this.speakers.put(speakerId, speaker);
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
        synchronized (speakers) {
            for (MusicSpeaker<? extends MusicBuffer<?>> value : speakers.values()) {
                value.tick();
            }
        }
    }

    @Override
    public void pause() {
        synchronized (speakers) {
            for (MusicSpeaker<? extends MusicBuffer<?>> value : speakers.values()) {
                value.pause();
            }
        }
    }

    @Override
    public void resume() {
        synchronized (speakers) {
            for (MusicSpeaker<? extends MusicBuffer<?>> value : speakers.values()) {
                value.resume();
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        synchronized (speakers) {
            for (MusicSpeaker<? extends MusicBuffer<?>> value : speakers.values()) {
                value.destroy();
            }
        }

        this.stream.close();
    }

    @Override
    public void load(long position) throws Exception {
        this.stream = loadAudioStream(position);

        for (int i = 0; i < 100; i++) {
            readAudioStream();
            var r = convertSpeakerBuffer(buffer);
            r.forEach(this::insetBuffer);
        }
    }

    abstract protected AudioInputStream loadAudioStream(long position) throws Exception;

    private <T, E extends MusicSpeaker<MusicBuffer<T>>> Map<Pair<Class<E>, MusicBufferSpeakerData>, MusicBuffer<T>> convertSpeakerBuffer(byte[] data) throws ExecutionException, InterruptedException {
        var me = MusicEngine.getInstance();
        Map<Pair<Class<E>, MusicBufferSpeakerData>, E> scs = new HashMap<>();
        synchronized (speakers) {
            for (MusicSpeaker<? extends MusicBuffer<?>> value : speakers.values()) {
                var cs = (Class<E>) value.getClass();
                if (!scs.containsKey(scs))
                    scs.put(Pair.of(cs, value.getBufferSpeakerData()), (E) value);
            }
        }

        Map<Pair<Class<E>, MusicBufferSpeakerData>, MusicBuffer<T>> ret = new HashMap<>();
        scs.forEach((c, s) -> mc.submit(() -> {
            ret.put(Pair.of(c.getLeft(), c.getRight()), s.createBuffer());
        }).join());

        List<CompletableFuture<Triple<MusicBuffer<T>, T, MusicBufferSpeakerData>>> cfs = new ArrayList<>();
        ret.forEach((c, s) -> {
            var cf = CompletableFuture.supplyAsync(() -> Triple.of(s, s.asyncConvertBuffer(data, sampleRate, channel, bit), c.getRight()), me.getMusicLoaderExecutor());
            cfs.add(cf);
        });

        for (CompletableFuture<Triple<MusicBuffer<T>, T, MusicBufferSpeakerData>> cf : cfs) {
            var r = cf.get();
            r.getLeft().putBuffer(r.getMiddle(), r.getRight());
        }

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

    private <T extends MusicBuffer<?>> void insetBuffer(Pair<Class<MusicSpeaker<T>>, MusicBufferSpeakerData> speakerDataPair, T buffer) {
        synchronized (speakers) {
            for (MusicSpeaker<? extends MusicBuffer<?>> value : speakers.values()) {
                if (speakerDataPair.getLeft().isInstance(value) && speakerDataPair.getRight().equals(value.getBufferSpeakerData())) {
                    var v = (MusicSpeaker<T>) value;
                    v.insertAudio(buffer);
                }
            }
        }
    }

    @Override
    public void play(long delay) {
        for (MusicSpeaker<? extends MusicBuffer<?>> value : speakers.values()) {
            value.play();
        }
    }
}
