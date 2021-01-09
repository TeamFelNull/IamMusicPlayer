package red.felnull.imp.ffmpeg;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.music.MusicThread;
import red.felnull.imp.exception.IMPFFmpegException;
import red.felnull.imp.util.FFmpegUtils;
import red.felnull.imp.util.PathUtils;
import red.felnull.otyacraftengine.util.IKSGFileLoadUtil;
import red.felnull.otyacraftengine.util.IKSGURLUtil;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.process.ProcessLocator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FFmpegManeger {
    private static FFmpegManeger INSTANCE;
    private static final String FFMPEG_IMPVERSION = "1";
    private static final String FFMPEGLIST_LINK = "https://raw.githubusercontent.com/TeamFelnull/IamMusicPlayer/master/ffmpeg/ffmpeg_link.json";
    private static final Logger LOGGER = LogManager.getLogger(FFmpegManeger.class);

    private OSAndArch osAndArch;
    private ProcessLocator locator;
    private FFmpegState state;

    public FFmpegManeger() {
        state = FFmpegState.NOFILE;
    }

    public static void init() {
        FFmpegDownloader.init();
        INSTANCE = new FFmpegManeger();
    }

    public static FFmpegManeger instance() {
        return INSTANCE;
    }

    public void check() {
        LOGGER.info("FFmpeg check");
        Path ffmpegFolderPath = PathUtils.getFFmpegFolder();
        IKSGFileLoadUtil.createFolder(ffmpegFolderPath);

        osAndArch = OSAndArch.getMyOSAndArch();
        String os = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        LOGGER.info("OS and Arch : " + os + " - " + arch + " - " + osAndArch.getName());

        String filename = "ffmpeg-" + osAndArch.getName() + "-" + FFMPEG_IMPVERSION + (osAndArch.isExe() ? ".exe" : "");
        File ffmpegfile = ffmpegFolderPath.resolve(filename).toFile();

        if (ffmpegfile.exists()) {
            setLocator(ffmpegfile);
            state = FFmpegState.NONE;
            startFFmpegEncodeTest();
        } else {
            LOGGER.info("No ffmpeg");
            FFmpegDownloader.getInstance().startDL(osAndArch, ffmpegfile);
        }
    }

    protected void setState(FFmpegState state) {
        this.state = state;
    }

    public FFmpegState getState() {
        return state;
    }

    public String getFFmpegLink(OSAndArch oaa) throws IOException {
        String str = IKSGURLUtil.getURLResponse(FFMPEGLIST_LINK);
        JsonObject jsonobject = new Gson().fromJson(str, JsonObject.class);
        JsonObject links = jsonobject.getAsJsonObject(FFMPEG_IMPVERSION);
        return links.get(oaa.getName()).getAsString();
    }

    public InputStream getFFmpegResource(String name) {
        //ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator
        try {
            String resourceName = "nativebin/" + name;
            InputStream is = getClass().getResourceAsStream(resourceName);
            if (is == null) {
                resourceName = "ws/schild/jave/nativebin/" + name;
                is = ClassLoader.getSystemResourceAsStream(resourceName);
            }
            if (is == null) {
                resourceName = "ws/schild/jave/nativebin/" + name;
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                is = classloader.getResourceAsStream(resourceName);
            }
            return is;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void error() {

    }

    public void setLocator(File file) {
        locator = new IMPFFMPEGLocator(file.getAbsolutePath());
        if (osAndArch != OSAndArch.windows_amd64 && osAndArch != OSAndArch.windows_x86) {
            try {
                Runtime.getRuntime().exec(new String[]{"/bin/chmod", "755", file.getAbsolutePath()});
            } catch (IOException e) {
                LOGGER.error("Error setting executable via chmod", e);
            }
        }
    }

    public ProcessLocator getLocator() {
        return locator;
    }

    public boolean canUseFFmpeg() {
        return locator != null && state == FFmpegState.NONE;
    }

    public void cantFFmpegCaution(PlayerEntity player) {
        player.sendStatusMessage(new TranslationTextComponent("ffmpegdl.caution"), true);
    }

    public void startFFmpegEncodeTest() {
        if (canUseFFmpeg()) {
            FFmpegEncodeTestThread fet = new FFmpegEncodeTestThread();
            fet.start();
        }
    }

    private class FFmpegEncodeTestThread extends Thread {

        private FFmpegEncodeTestThread() {
            this.setName("FFmpeg Encode Test");
        }

        @Override
        public void run() {
            LOGGER.info("Start FFmpeg encode test");
            try {
                try {
                    state = FFmpegState.TESTING;
                    IResourceManager rm = Minecraft.getInstance().getResourceManager();
                    InputStream stream = rm.getResource(new ResourceLocation(IamMusicPlayer.MODID, "ffmpeg_testdata")).getInputStream();
                    IKSGFileLoadUtil.createFolder(PathUtils.getIMPTmpFolder());
                    IKSGFileLoadUtil.fileInputStreamWriter(stream, PathUtils.getIMPTmpFolder().resolve("ffmpeg_test_in"), StandardCopyOption.REPLACE_EXISTING);
                    MultimediaObject mo = FFmpegUtils.createMultimediaObject(PathUtils.getIMPTmpFolder().resolve("ffmpeg_test_in").toFile());
                    Encoder encoder = new Encoder();
                    AudioAttributes aa = new AudioAttributes();
                    aa.setCodec("libmp3lame");
                    aa.setBitRate(128);
                    aa.setChannels(1);
                    aa.setSamplingRate(32000);
                    EncodingAttributes ea = new EncodingAttributes();
                    ea.setOutputFormat("mp3");
                    ea.setAudioAttributes(aa);
                    encoder.encode(mo, PathUtils.getIMPTmpFolder().resolve("ffmpeg_test_out").toFile(), ea);
                    IKSGFileLoadUtil.deleteFile(PathUtils.getIMPTmpFolder().resolve("ffmpeg_test_in"));
                    IKSGFileLoadUtil.deleteFile(PathUtils.getIMPTmpFolder().resolve("ffmpeg_test_out"));
                    state = FFmpegState.NONE;
                } catch (IMPFFmpegException | EncoderException ex) {
                    LOGGER.error("FFmpeg encode test failed", ex);
                    state = FFmpegState.ERROR;
                }
                LOGGER.info("Finish FFmpeg encode test");
            } catch (Exception ex) {
                LOGGER.error("FFmpeg encode test not complete successfully");
            }

        }
    }

    public enum FFmpegState {
        NONE("none"),
        PREPARATION("preparation"),
        DOWNLOADING("downloading"),
        EXTRACTING("extracting"),
        ERROR("error"),
        NOFILE("nofile"),
        TESTING("testing");
        private final String name;

        private FFmpegState(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public TranslationTextComponent getLocalized() {
            return new TranslationTextComponent("ffmpegdlstate." + name);
        }
    }

    public enum OSAndArch {
        windows_amd64("windows-amd64", "ffmpeg-amd64.exe", true),
        windows_x86("windows-x86", "ffmpeg-x86.exe", true),
        linux_amd64("linux-amd64", "ffmpeg-amd64"),
        linux_i386("linux-i386", "ffmpeg-i386"),
        linux_aarch64("linux-aarch64", "ffmpeg-aarch64"),
        osx_x86("osx-x86", "ffmpeg-x86_64-osx"),
        other("other");
        private final String name;
        private final String resourceName;
        private final boolean exe;

        private OSAndArch(String name) {
            this(name, "", false);
        }

        private OSAndArch(String name, String resourceName) {
            this(name, resourceName, false);
        }

        private OSAndArch(String name, String resourceName, boolean exe) {
            this.name = name;
            this.resourceName = resourceName;
            this.exe = exe;
        }

        public String getName() {
            return name;
        }

        public String getResourceName() {
            return resourceName;
        }

        public boolean isExe() {
            return exe;
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
