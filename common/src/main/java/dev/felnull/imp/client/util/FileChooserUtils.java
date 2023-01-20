package dev.felnull.imp.client.util;

import com.sun.jna.platform.win32.Shell32Util;
import com.sun.jna.platform.win32.ShlObj;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileChooserUtils {

    public static File[] openMusicFileChooser(boolean multiSelect) {
        return trayOpenFileChooser("music", getMusicFolder(), multiSelect);
    }

    public static File[] openImageFileChooser(boolean multiSelect) {
        return trayOpenFileChooser("image", getPicturesFolder(), multiSelect, "*.png", "*.jpg", "*.jpeg", "*.gif");
    }

    @Nullable
    private static File[] trayOpenFileChooser(String name, Path initPath, boolean multiSelect, @Nullable String... filterPatterns) {
        return OEClientUtils.openFilterFileChooser(I18n.get("imp.fileChooser.title." + name), initPath, null, multiSelect, filterPatterns);
    }

    private static Path getMusicFolder() {
        if (Util.getPlatform() == Util.OS.WINDOWS)
            return Paths.get(Shell32Util.getSpecialFolderPath(ShlObj.CSIDL_MYMUSIC, false));

        return null;
    }

    private static Path getPicturesFolder() {
        if (Util.getPlatform() == Util.OS.WINDOWS)
            return Paths.get(Shell32Util.getSpecialFolderPath(ShlObj.CSIDL_MYPICTURES, false));

        return null;
    }
}
