package dev.felnull.imp.client.music;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.music.loader.IMPMusicLoaders;
import dev.felnull.imp.client.music.loader.IMusicLoader;
import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.util.FlagThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MusicLoadThread extends FlagThread {
    private static final Logger LOGGER = LogManager.getLogger(MusicLoadThread.class);
    private final MusicSource source;
    private final MusicPlaybackInfo playbackInfo;
    private final long position;
    private final MusicLoadResultListener listener;

    protected MusicLoadThread(MusicSource source, MusicPlaybackInfo playbackInfo, long position, MusicLoadResultListener listener) {
        this.setName("Music Loader Thread: " + source.getIdentifier());
        this.source = source;
        this.playbackInfo = playbackInfo;
        this.position = position;
        this.listener = listener;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();

        var executor = MusicEngineOld.getInstance().getExecutor();

        IMusicLoader loader = null;
        for (IMusicLoader ldr : IMPMusicLoaders.getLoaders()) {
            var cf = CompletableFuture.supplyAsync(() -> {
                try {
                    return ldr.canLoad(source);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, executor);

            try {
                Boolean ret = cf.get(10, TimeUnit.SECONDS);
                if (ret != null && ret) {
                    loader = ldr;
                    break;
                }
            } catch (TimeoutException ex) {
                if (IamMusicPlayer.CONFIG.errorLog) LOGGER.error("Load check time out: " + source.getIdentifier());
            } catch (Exception ignored) {
            }
        }
        if (isStopped()) return;

        if (loader == null) {
            if (IamMusicPlayer.CONFIG.errorLog) LOGGER.error("Non existent music loader: " + source.getLoaderType());
            listener.onResult(false, System.currentTimeMillis() - time, null, false);
            return;
        }

        if (isStopped()) return;

        IMusicPlayer player = null;

        try {
            player = loader.createMusicPlayer(source);

            IMusicPlayer finalPlayer = player;
            var cf = CompletableFuture.runAsync(() -> {
                try {
                    finalPlayer.load(position);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, executor);
            cf.get(10, TimeUnit.SECONDS);
            if (isStopped()) return;

            if (!player.isLoadSuccess()) throw new IllegalStateException("Load failed");
        } catch (TimeoutException ignored) {
            if (IamMusicPlayer.CONFIG.errorLog) LOGGER.error("Load time out: " + source.getIdentifier());
            player.destroy();
            if (isStopped()) return;
            listener.onResult(false, System.currentTimeMillis() - time, null, false);
            return;
        } catch (Exception ex) {
            if (player != null) player.destroy();
            if (IamMusicPlayer.CONFIG.errorLog) LOGGER.error("Failed to load music: " + source.getIdentifier(), ex);
            if (isStopped()) return;
            listener.onResult(false, System.currentTimeMillis() - time, null, true);
            return;
        }

        if (isStopped()) return;
        listener.onResult(true, System.currentTimeMillis() - time, player, false);
    }

    public MusicPlaybackInfo getPlaybackInfo() {
        return playbackInfo;
    }

    public long getPosition() {
        return position;
    }

    public MusicSource getSource() {
        return source;
    }

    public MusicLoadResultListener getListener() {
        return listener;
    }

    public static interface MusicLoadResultListener {
        void onResult(boolean result, long time, IMusicPlayer player, boolean retry);
    }
}
