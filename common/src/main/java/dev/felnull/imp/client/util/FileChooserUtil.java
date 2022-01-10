package dev.felnull.imp.client.util;

import dev.felnull.otyacraftengine.util.FNJLNativeWrapper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileChooserUtil {
    private static final Minecraft mc = Minecraft.getInstance();

    public static File[] openImageFileChooser(boolean multiSelect) {
        Path initPath = Paths.get(".");
        if (FNJLNativeWrapper.isSupportSpecialFolder())
            initPath = FNJLNativeWrapper.getMyPicturesFolder();
        return trayOpenFileChooser("image", initPath, multiSelect, new FNJLNativeWrapper.FileChooserFilterWrapper(I18n.get("imp.fileChooser.files.image"), "png", "jpg", "jpeg", "bmp", "gif"));
    }

    private static File[] trayOpenFileChooser(String name, Path initPath, boolean multiSelect, FNJLNativeWrapper.FileChooserFilterWrapper filter) {
       /* if (FNJLNativeWrapper.isSupportNativeFileChooser()) {
            var flg = new FNJLNativeWrapper.FileChooserFlagWrapper().hideReadOnly(true);
            if (multiSelect) flg.allowMultiSelect(true).explorer(true);
            return FNJLNativeWrapper.openNativeFileChooser(name, initPath, flg, filter);
        }*/
        Util.getPlatform().openFile(initPath.toFile());
        return null;
    }
}
