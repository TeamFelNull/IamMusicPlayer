package red.felnull.imp.ffmpeg;

import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.util.PathUtils;
import red.felnull.otyacraftengine.util.IKSGFileLoadUtil;
import ws.schild.jave.process.ProcessLocator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class FFmpegManeger {
    private static FFmpegManeger INSTANCE;
    public static final String FFMPEG_IMPVERSION = "1";

    private OSAndArch osAndArch;
    private ProcessLocator locator;

    public static void init() {
        INSTANCE = new FFmpegManeger();
    }

    public static FFmpegManeger instance() {
        return INSTANCE;
    }

    public void check() {
        IamMusicPlayer.LOGGER.info("FFmpeg check");
        Path ffmpegFolderPath = PathUtils.getFFmpegFolder();
        IKSGFileLoadUtil.createFolder(ffmpegFolderPath);

        osAndArch = OSAndArch.getMyOSAndArch();
        IamMusicPlayer.LOGGER.info("OS and Arch : " + osAndArch.getName());

        String os = System.getProperty("os.name").toLowerCase();

        File ffmpegfile = ffmpegFolderPath.resolve("ffmpeg-windows-amd64.exe").toFile();

        locator = new IMPFFMPEGLocator(ffmpegfile.getAbsolutePath());
        if (osAndArch == OSAndArch.windows_amd64 || osAndArch == OSAndArch.windows_x86) {
            try {
                Runtime.getRuntime().exec(new String[]{"/bin/chmod", "755", ffmpegfile.getAbsolutePath()});
            } catch (IOException e) {
                IamMusicPlayer.LOGGER.error("Error setting executable via chmod", e);
            }
        }
    }

    public ProcessLocator getLocator() {
        return locator;
    }

    public enum OSAndArch {
        windows_amd64("windows-amd64"),
        windows_x86("windows-x86"),
        linux_amd64("linux-amd64"),
        linux_i386("linux-i386"),
        linux_aarch64("linux-aarch64"),
        osx_x86("osx-x86"),
        other("other");
        private final String name;

        private OSAndArch(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static OSAndArch getMyOSAndArch() {
            String os = System.getProperty("os.name").toLowerCase();
            String arch = System.getProperty("os.arch");
            if (os.contains("windows")) {
                if (arch.contains("amd64"))
                    return windows_amd64;
                else if (arch.contains("x86"))
                    return windows_x86;
            } else if (os.contains("mac")) {
                if (arch.contains("x86"))
                    return osx_x86;
            } else if (os.contains("linux")) {
                if (arch.contains("amd64"))
                    return linux_amd64;
                else if (arch.contains("i386"))
                    return linux_i386;
                else if (arch.contains("aarch64"))
                    return linux_aarch64;
            }
            return other;
        }
    }
}
