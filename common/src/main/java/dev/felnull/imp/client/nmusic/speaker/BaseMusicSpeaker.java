package dev.felnull.imp.client.nmusic.speaker;

import dev.felnull.imp.client.nmusic.speaker.buffer.MusicBuffer;
import dev.felnull.imp.nmusic.tracker.MusicTracker;

public abstract class BaseMusicSpeaker<T extends MusicBuffer<?>> implements MusicSpeaker<T> {
    private MusicTracker tracker;

    protected BaseMusicSpeaker(MusicTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public void update(MusicTracker tracker) {
        this.tracker = tracker;
    }
}
