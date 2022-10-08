package dev.felnull.imp.client.nmusic.player;

import dev.felnull.imp.client.nmusic.MusicInstantaneous;
import dev.felnull.imp.client.nmusic.speaker.MusicSpeaker;
import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBuffer;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.*;

public abstract class BaseMusicPlayer implements MusicPlayer {
    private final Map<UUID, MusicSpeaker<? extends MusicBuffer<?>>> speakers = new HashMap<>();
    private final byte[] buffer;
    private AudioInputStream stream;
    private MusicInstantaneous lastInstantaneous;

    protected BaseMusicPlayer(int channel, int sampleRate, int bit) {
        this.buffer = new byte[sampleRate * channel * (bit / 8)];
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

        convertSpeakerBuffer(null);
    }

    abstract protected AudioInputStream loadAudioStream(long position) throws Exception;

    private void convertSpeakerBuffer(byte[] data) {
        Set<Class<? extends MusicSpeaker<?>>> scs = new HashSet<>();
        synchronized (speakers) {
            for (MusicSpeaker<? extends MusicBuffer<?>> value : speakers.values()) {
                scs.add((Class<? extends MusicSpeaker<?>>) value.getClass());
            }
        }
        System.out.println(scs);
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

    private <T extends MusicBuffer<?>> void insetBuffer(Class<MusicSpeaker<T>> speakerClass, T buffer) {
        synchronized (speakers) {
            for (MusicSpeaker<? extends MusicBuffer<?>> value : speakers.values()) {
                if (speakerClass.isInstance(value)) {
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
