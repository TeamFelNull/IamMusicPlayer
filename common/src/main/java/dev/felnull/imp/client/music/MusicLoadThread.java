package dev.felnull.imp.client.music;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.music.loader.IMPMusicLoaders;
import dev.felnull.imp.client.music.loader.IMusicLoader;
import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.client.music.subtitle.IMPMusicSubtitles;
import dev.felnull.imp.client.music.subtitle.IMusicSubtitle;
import dev.felnull.imp.client.music.subtitle.SubtitleType;
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
    private LoadTimer timer;
    private boolean timeOut;

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
                timer = new LoadTimer();
                timer.start();
                boolean lod = ldr.canLoad(source);
                timer.interrupt();
                timer = null;
                if (lod) {
                    loader = ldr;
                    break;
                }
            } catch (InterruptedException ex) {
                if (!timeOut)
                    return;
                if (IamMusicPlayer.CONFIG.errorLog)
                    LOGGER.error("Load check time out: " + source.getIdentifier());
                timeOut = false;
            } catch (Exception ignored) {
            }
        }
        if (loader == null) {
            if (IamMusicPlayer.CONFIG.errorLog)
                LOGGER.error("Non existent music loader: " + source.getLoaderType());
            listener.onResult(false, System.currentTimeMillis() - time, null, false);
            return;
        }
        IMusicPlayer player = null;
        try {
            player = loader.createMusicPlayer(source);
            timer = new LoadTimer();
            timer.start();
            player.load(position);
            timer.interrupt();
            timer = null;
            if (!player.isLoadSuccess())
                throw new IllegalStateException("Load failed");
        } catch (InterruptedException ignored) {
            if (timeOut) {
                if (IamMusicPlayer.CONFIG.errorLog)
                    LOGGER.error("Load time out: " + source.getIdentifier());
                listener.onResult(false, System.currentTimeMillis() - time, null, false);
            }
            player.destroy();
            return;
        } catch (Exception ex) {
            if (player != null)
                player.destroy();
            if (IamMusicPlayer.CONFIG.errorLog)
                LOGGER.error("Failed to load music: " + source.getIdentifier(), ex);
            listener.onResult(false, System.currentTimeMillis() - time, null, true);
            return;
        }

        if (IamMusicPlayer.CONFIG.subtitleType != SubtitleType.OFF) {
            try {
                IMusicSubtitle subtitle = IMPMusicSubtitles.createSubtitle(source.getLoaderType(), source);
                if (subtitle != null && subtitle.isExist()) {
                    subtitle.load();
                    player.setSubtitle(subtitle);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

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

    private class LoadTimer extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(15000);
                timeOut = true;
                MusicLoadThread.this.interrupt();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
