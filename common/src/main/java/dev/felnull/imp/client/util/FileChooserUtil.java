package dev.felnull.imp.client.util;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;

public class FileChooserUtil {

    public static File[] openMusicFileChooser(boolean multiSelect) {
        /*Path initPath = null;
        if (FNJLNativesWrapper.isSupportSpecialFolder())
            initPath = FNJLNativesWrapper.getMyMusicFolder();
        return trayOpenFileChooser("music", initPath, multiSelect);*/
        return null;
    }

    public static File[] openImageFileChooser(boolean multiSelect) {
       /* Path initPath = null;
        if (FNJLNativesWrapper.isSupportSpecialFolder())
            initPath = FNJLNativesWrapper.getMyPicturesFolder();
        return trayOpenFileChooser("image", initPath, multiSelect);*/
        return null;
    }

    @Nullable
    private static File[] trayOpenFileChooser(String name, Path initPath, boolean multiSelect) {
        return null;
        //   return OEClientUtil.openFileChooser(I18n.get("imp.fileChooser.title." + name), initPath, null, multiSelect);
    }
}
