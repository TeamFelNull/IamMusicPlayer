package dev.felnull.imp.client.nmusic.speaker;

import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBuffer;
import dev.felnull.imp.nmusic.tracker.MusicTracker;

public abstract class BaseMusicSpeaker<T extends MusicBuffer<?>> implements MusicSpeaker<T> {
    private MusicTracker tracker;
    private long startTime = -1;
    private long pauseTime = -1;
    private long totalPauseTime;

    protected BaseMusicSpeaker(MusicTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public void update(MusicTracker tracker) {
        this.tracker = tracker;
    }

    public MusicTracker getTracker() {
        return tracker;
    }

    @Override
    public void play(long delay) {
        this.startTime = System.currentTimeMillis();

    }

    @Override
    public void pause() {
        this.pauseTime = System.currentTimeMillis();
    }

    @Override
    public void resume() {
        this.totalPauseTime += System.currentTimeMillis() - this.pauseTime;
        this.pauseTime = -1;
    }

    @Override
    public long getPlayTime() {
        if (this.startTime < 0)
            return 0;

        long pt = this.totalPauseTime;
        if (this.pauseTime >= 0)
            pt += System.currentTimeMillis() - this.pauseTime;
        return System.currentTimeMillis() - this.startTime - pt;
    }
}
