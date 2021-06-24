package red.felnull.imp.client.music.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.api.client.IMPClientRegistry;
import red.felnull.imp.client.music.MusicEngine;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.subtitle.IMusicSubtitle;
import red.felnull.imp.client.music.subtitle.SubtitleLoaderThread;
import red.felnull.imp.client.music.subtitle.SubtitleSystem;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.resource.MusicSource;

import java.util.UUID;

public class MusicLoaderThread extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(MusicLoaderThread.class);
    private final UUID uuid;
    private final MusicSource location;
    private final long startPosition;
    private final long startTime;
    private final MusicPlayInfo autPlayInfo;
    private final MusicLoaderThread.MusicLoadResultListener resultListener;
    private boolean stop;

    public MusicLoaderThread(UUID uuid, MusicSource location, long startPosition, MusicPlayInfo autPlayInfo, MusicLoaderThread.MusicLoadResultListener resultListener) {
        this.setName("Music Loader Thread: " + location.getIdentifier());
        this.uuid = uuid;
        this.location = location;
        this.startPosition = startPosition;
        this.autPlayInfo = autPlayInfo;
        this.startTime = System.currentTimeMillis();
        this.resultListener = resultListener;
    }

    @Override
    public void run() {
        MusicEngine engine = MusicEngine.getInstance();
        if (!IMPClientRegistry.isLoaderContains(location.getLoaderName())) {
            LOGGER.error("Non existent music loader: " + location.getLoaderName());
            engine.loadFinished(uuid, null);
            if (stop)
                return;
            resultListener.onResult(MusicLoadResult.FAILURE, uuid, 0);
            return;
        }

        IMusicLoader loader = IMPClientRegistry.getLoader(location.getLoaderName());

        IMusicPlayer player = null;
        long startTime = System.currentTimeMillis();
        try {
            player = loader.createMusicPlayer(location);
            player.ready(startPosition);
        } catch (Exception ex) {
            LOGGER.error("Failed to load music: " + location.getIdentifier(), ex);
            engine.loadFinished(uuid, null);
            if (!stop)
                resultListener.onResult(MusicLoadResult.FAILURE, uuid, 0);
            if (player != null)
                player.destroy();
            return;
        }

        long eqTime = System.currentTimeMillis() - startTime;

        IMusicSubtitle subtitle = IamMusicPlayer.CONFIG.subtitleSystem == SubtitleSystem.OFF ? null : loader.createMusicSubtitle(player, location);

        if (subtitle != null) {
            SubtitleLoaderThread slt = new SubtitleLoaderThread(uuid, location, subtitle, () -> stop);
            slt.start();
        }

        if (stop)
            return;

        engine.loadFinished(uuid, player);
        resultListener.onResult(MusicLoadResult.COMPLETE, uuid, eqTime);
    }

    public void stopped() {
        stop = true;
    }

    public MusicSource getLocation() {
        return location;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public long getCurrentDelayStartPosition() {
        return startPosition + (System.currentTimeMillis() - startTime);
    }

    public MusicPlayInfo getAutPlayInfo() {
        return autPlayInfo;
    }

    public static enum MusicLoadResult {
        FAILURE,
        COMPLETE;
    }

    public static interface MusicLoadResultListener {
        void onResult(MusicLoadResult result, UUID uuid, long time);
    }

}
