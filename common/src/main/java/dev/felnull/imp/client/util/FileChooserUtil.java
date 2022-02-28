package dev.felnull.imp.client.util;

import dev.felnull.otyacraftengine.client.util.OEClientUtil;
import dev.felnull.otyacraftengine.util.FNJLNativesWrapper;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;

public class FileChooserUtil {

    public static File[] openMusicFileChooser(boolean multiSelect) {
        Path initPath = null;
        if (FNJLNativesWrapper.isSupportSpecialFolder())
            initPath = FNJLNativesWrapper.getMyMusicFolder();
        return trayOpenFileChooser("music", initPath, multiSelect);
    }

    public static File[] openImageFileChooser(boolean multiSelect) {
        Path initPath = null;
        if (FNJLNativesWrapper.isSupportSpecialFolder())
            initPath = FNJLNativesWrapper.getMyPicturesFolder();
        return trayOpenFileChooser("image", initPath, multiSelect);
    }

    @Nullable
    private static File[] trayOpenFileChooser(String name, Path initPath, boolean multiSelect) {
        return OEClientUtil.openFileChooser(I18n.get("imp.fileChooser.title." + name), initPath, null, multiSelect);
    }
}
