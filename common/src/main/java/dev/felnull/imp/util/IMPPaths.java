package dev.felnull.imp.util;

import dev.felnull.imp.IamMusicPlayer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class IMPPaths {
    public static Path getNaiveLibraryFolder(String lavaVersion) {
        return Paths.get(IamMusicPlayer.MODID).resolve(lavaVersion);
    }

    public static Path getTmpFolder() {
        return Paths.get(IamMusicPlayer.MODID).resolve("tmp");
    }
}
