package dev.felnull.imp.client.music;

import dev.felnull.imp.music.resource.MusicSource;

import java.util.UUID;

public class MusicScheduler {
    private final UUID musicPlayerId;
    private final MusicSource source;
    private final long position;
    private boolean broken;
    private long delayStartTime = -1;
    private MusicEntry musicEntry;
    private MusicEntry preMusicEntry;

    protected MusicScheduler(UUID musicPlayerId, MusicSource source, long position) {
        this.musicPlayerId = musicPlayerId;
        this.source = source;
        this.position = position;
    }

    protected void start(LoadCompleteListener listener) {
        this.musicEntry = new MusicEntry(musicPlayerId, source, position);
        this.musicEntry.loadStart(listener);
    }

    protected void delay() {
        if (this.delayStartTime >= 0) {
            getEngine().getLogger().error("Already delay");
            return;
        }
        this.delayStartTime = System.currentTimeMillis();
    }

    protected boolean play() {
        if (this.delayStartTime < 0) {
            this.musicEntry.playStart(0);
            return true;
        }
        return false;
    }

    protected void tick() {
        if (broken) return;


        if (musicEntry != null && !musicEntry.tick()) {
            broken = true;
            return;
        }

        if (preMusicEntry != null)
            preMusicEntry.tick();

        long mw = getMaxWait();

        if (this.delayStartTime >= 0) {
            if (!source.isLive() && source.getDuration() < (position + getDelay())) {
                broken = true;
                return;
            }

        }
    }

    protected boolean broken() {
        return broken;
    }

    private long getDelay() {
        if (delayStartTime < 0)
            return 0;
        return System.currentTimeMillis() - delayStartTime;
    }

    private long getMaxWait() {
        if (musicEntry == null || musicEntry.getMusicPlayer() == null) return -1;
        return musicEntry.getMusicPlayer().getMaxWaitTime();
    }

    private MusicEngine getEngine() {
        return MusicEngine.getInstance();
    }
}
