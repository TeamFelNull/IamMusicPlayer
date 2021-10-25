package dev.felnull.imp.client.music;

import dev.felnull.imp.client.music.loader.IMPMusicLoaders;
import dev.felnull.imp.client.music.loader.IMusicLoader;
import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.MusicSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MusicLoadThread extends Thread {
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
        IMusicLoader loader = null;
        for (IMusicLoader ldr : IMPMusicLoaders.getLoaders()) {
            try {
                if (ldr.canLoad(source)) {
                    loader = ldr;
                    break;
                }
            } catch (InterruptedException ex) {
                return;
            } catch (Exception ignored) {
            }
        }
        if (loader == null) {
            LOGGER.error("Non existent music loader: " + source.getLoaderType());
            listener.onResult(false, System.currentTimeMillis() - time, null, false);
            return;
        }
        IMusicPlayer player = null;
        try {
            player = loader.createMusicPlayer(source);
            player.load(position);
            listener.onResult(true, System.currentTimeMillis() - time, player, false);
        } catch (InterruptedException ignored) {
        } catch (Exception ex) {
            if (player != null)
                player.destroy();
            LOGGER.error("Failed to load music: " + source.getIdentifier(), ex);
            listener.onResult(false, System.currentTimeMillis() - time, null, true);
        }
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
