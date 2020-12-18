package red.felnull.imp.util;

import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.util.IKSGPathUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {
    public static Path getIMPFolder() {
        return Paths.get(IamMusicPlayer.MODID);
    }

    public static Path getClientTmpFolder() {
        return getIMPFolder().resolve("tmp");
    }

    public static Path getWorldIMPFolder() {
        return IKSGPathUtil.getWorldSaveDataPath().resolve(IamMusicPlayer.MODID);
    }

    public static Path getWorldTmpFolder() {
        return getWorldIMPFolder().resolve("tmp");
    }

    public static Path getWorldMusicFolder() {
        return getWorldIMPFolder().resolve("music");
    }

    public static Path getWorldMusic(String uuid) {
        return getWorldMusicFolder().resolve(uuid);
    }

    public static Path getFFmpegFolder() {
        return getIMPFolder().resolve("ffmpeg");
    }


}
