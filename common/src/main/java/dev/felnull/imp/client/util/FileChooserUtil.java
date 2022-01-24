package dev.felnull.imp.client.util;

import dev.felnull.otyacraftengine.util.FNJLNativesWrapper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileChooserUtil {
    private static final Minecraft mc = Minecraft.getInstance();

    public static File[] openMusicFileChooser(boolean multiSelect) {
        Path initPath = Paths.get(".");
        if (FNJLNativesWrapper.isSupportSpecialFolder())
            initPath = FNJLNativesWrapper.getMyMusicFolder();
        return trayOpenFileChooser("music", initPath, multiSelect, new FNJLNativesWrapper.FileChooserFilterWrapper(I18n.get("imp.fileChooser.files.music"), "mp3", "mp4", "wav", "m4a"));
    }

    public static File[] openImageFileChooser(boolean multiSelect) {
        Path initPath = Paths.get(".");
        if (FNJLNativesWrapper.isSupportSpecialFolder())
            initPath = FNJLNativesWrapper.getMyPicturesFolder();
        return trayOpenFileChooser("image", initPath, multiSelect, new FNJLNativesWrapper.FileChooserFilterWrapper(I18n.get("imp.fileChooser.files.image"), "png", "jpg", "jpeg", "bmp", "gif"));
    }

    private static File[] trayOpenFileChooser(String name, Path initPath, boolean multiSelect, FNJLNativesWrapper.FileChooserFilterWrapper filter) {
       /* if (FNJLNativesWrapper.isSupportNativeFileChooser()) {
            var flg = new FNJLNativesWrapper.FileChooserFlagWrapper().hideReadOnly(true);
            if (multiSelect) flg.allowMultiSelect(true).explorer(true);
            return FNJLNativesWrapper.openNativeFileChooser(name, initPath, flg, filter);
        }*/
        Util.getPlatform().openFile(initPath.toFile());
        return null;
    }
}
