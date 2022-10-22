package dev.felnull.imp.client.util;

import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.natives.OENatives;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;

public class FileChooserUtils {

    public static File[] openMusicFileChooser(boolean multiSelect) {
        Path initPath = null;
        if (OENatives.getInstance().isSupportSpecialFolder())
            initPath = OENatives.getInstance().getMyMusicFolder();
        return trayOpenFileChooser("music", initPath, multiSelect);
    }

    public static File[] openImageFileChooser(boolean multiSelect) {
        Path initPath = null;
        if (OENatives.getInstance().isSupportSpecialFolder())
            initPath = OENatives.getInstance().getMyPicturesFolder();
        return trayOpenFileChooser("image", initPath, multiSelect);
    }

    @Nullable
    private static File[] trayOpenFileChooser(String name, Path initPath, boolean multiSelect) {
        return OEClientUtils.openFileChooser(I18n.get("imp.fileChooser.title." + name), initPath, null, multiSelect);
    }
}
