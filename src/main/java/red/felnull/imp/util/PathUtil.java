package red.felnull.imp.util;

import red.felnull.imp.IamMusicPlayer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {
    public static Path getTmpFolder() {
        return Paths.get(IamMusicPlayer.MODID + "/tmp");
    }
}
