package red.felnull.imp.util;

import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.util.IKSGPathUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {
    public static Path getClientTmpFolder() {
        return Paths.get(IamMusicPlayer.MODID).resolve("tmp");
    }

    public static Path getWorldTmpFolder() {
        return IKSGPathUtil.getWorldSaveDataPath().resolve(IamMusicPlayer.MODID).resolve("tmp");
    }

    public static Path getWorldMusicFolder() {
        return IKSGPathUtil.getWorldSaveDataPath().resolve(IamMusicPlayer.MODID).resolve("music");
    }
}
