package red.felnull.imp.ffmpeg;

import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.util.IKSGFileLoadUtil;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicLong;

public class FFmpegDownloader {
    private static FFmpegDownloader INSTANCE;
    private boolean dwonloading;
    private FFmpegDwonloadState state;
    private float progress;

    public static void init() {
        INSTANCE = new FFmpegDownloader();
    }

    public static FFmpegDownloader getInstance() {
        return INSTANCE;
    }

    public void startDL(FFmpegManeger.OSAndArch oaa, File file) {
        if (dwonloading)
            return;
        dwonloading = true;
        FFmpegDownloadThread fdt = new FFmpegDownloadThread(oaa, file);
        fdt.start();
    }

    public class FFmpegDownloadThread extends Thread {
        private final FFmpegManeger.OSAndArch osAndArch;
        private final File ffmpegfile;

        public FFmpegDownloadThread(FFmpegManeger.OSAndArch oaa, File file) {
            this.osAndArch = oaa;
            this.ffmpegfile = file;
        }

        @Override
        public void run() {
            FFmpegManeger maneger = FFmpegManeger.instance();
            state = FFmpegDwonloadState.PREPARATION;
            IamMusicPlayer.proxy.addFFmpegLoadToast();
            try {
                InputStream ffmpegResource = maneger.getFFmpegResource(osAndArch.getResourceName());
                //      ffmpegResource = null;
                if (ffmpegResource != null) {
                    state = FFmpegDwonloadState.LOADING;
                    IamMusicPlayer.LOGGER.info("Start ffmpeg copy");
                    Files.copy(ffmpegResource, ffmpegfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (!ffmpegfile.exists()) {
                try {
                    URL url = new URL(maneger.getFFmpegLink(osAndArch));
                    state = FFmpegDwonloadState.DOWNLOAD;
                    IamMusicPlayer.LOGGER.info("Start ffmpeg download");
                    AtomicLong lastt = new AtomicLong(System.currentTimeMillis());
                    IKSGFileLoadUtil.fileURLWriterProgress(url, ffmpegfile.toPath(), progressa -> {
                        progress = progressa;
                        if (System.currentTimeMillis() - lastt.get() >= 3000) {
                            lastt.set(System.currentTimeMillis());
                            IamMusicPlayer.LOGGER.info("Download ffmpeg : " + progressa + "%");
                        }
                    });
                    IamMusicPlayer.LOGGER.info("Completed ffmpeg download");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            state = null;
            dwonloading = false;

            if (ffmpegfile.exists())
                FFmpegManeger.instance().setLocator(ffmpegfile);
        }
    }

    public float getProgress() {
        return progress;
    }

    public FFmpegDwonloadState getState() {
        return state;
    }

    public enum FFmpegDwonloadState {
        PREPARATION(new TranslationTextComponent("ffmpegdlstate.preparation")),
        LOADING(new TranslationTextComponent("ffmpegdlstate.loading")),
        DOWNLOAD(new TranslationTextComponent("ffmpegdlstate.download"));

        private final TranslationTextComponent localized;

        FFmpegDwonloadState(TranslationTextComponent localized) {
            this.localized = localized;
        }

        public TranslationTextComponent getLocalized() {
            return localized;
        }

    }

    public boolean isDwonloading() {
        return dwonloading;
    }


}
