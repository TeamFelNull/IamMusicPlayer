package red.felnull.imp.ffmpeg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.util.IKSGFileLoadUtil;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicLong;

public class FFmpegDownloader {
    private static final Logger LOGGER = LogManager.getLogger(FFmpegDownloader.class);
    private static FFmpegDownloader INSTANCE;
    private float progress;

    public static void init() {
        INSTANCE = new FFmpegDownloader();
    }

    public static FFmpegDownloader getInstance() {
        return INSTANCE;
    }

    public void startDL(FFmpegManeger.OSAndArch oaa, File file) {
        FFmpegDownloadThread fdt = new FFmpegDownloadThread(oaa, file);
        fdt.start();
    }

    public class FFmpegDownloadThread extends Thread {
        private final FFmpegManeger.OSAndArch osAndArch;
        private final File ffmpegfile;

        public FFmpegDownloadThread(FFmpegManeger.OSAndArch oaa, File file) {
            this.setName("FFmpeg Downloader thread");
            this.osAndArch = oaa;
            this.ffmpegfile = file;
        }

        @Override
        public void run() {
            FFmpegManeger maneger = FFmpegManeger.instance();
            maneger.setState(FFmpegManeger.FFmpegState.PREPARATION);
            IKSGFileLoadUtil.deleteFile(ffmpegfile);
            IamMusicPlayer.proxy.addFFmpegLoadToast();
            try {
                InputStream ffmpegResource = maneger.getFFmpegResource(osAndArch.getResourceName());
                //    ffmpegResource = null;
                if (ffmpegResource != null) {
                    maneger.setState(FFmpegManeger.FFmpegState.EXTRACTING);
                    LOGGER.info("Start ffmpeg copy");
                    Files.copy(ffmpegResource, ffmpegfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (!ffmpegfile.exists()) {
                try {
                    URL url = new URL(maneger.getFFmpegLink(osAndArch));
                    maneger.setState(FFmpegManeger.FFmpegState.DOWNLOADING);
                    LOGGER.info("Start ffmpeg download");
                    AtomicLong lastt = new AtomicLong(System.currentTimeMillis());
                    IKSGFileLoadUtil.fileURLWriterProgress(url, ffmpegfile.toPath(), progressa -> {
                        progress = progressa;
                        if (System.currentTimeMillis() - lastt.get() >= 3000) {
                            lastt.set(System.currentTimeMillis());
                            LOGGER.info("Download ffmpeg : " + getPercentage(progressa));
                        }
                    });
                    LOGGER.info("Completed ffmpeg download");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            maneger.setState(FFmpegManeger.FFmpegState.NONE);
            if (ffmpegfile.exists())
                FFmpegManeger.instance().setLocator(ffmpegfile);
        }
    }

    public float getProgress() {
        return progress;
    }

    public static String getPercentage(float per) {
        return Math.round(per * 100) + " %";
    }
}
