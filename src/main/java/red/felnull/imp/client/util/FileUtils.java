package red.felnull.imp.client.util;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;

public class FileUtils {
    public static void openFileChoser(String title, Consumer<File> op) {
        try {
            File[] of = openFileChooser(title, null, null, false);
            if (of != null && of.length == 1) {
                File file = of[0];
                if (file.exists() && !file.isDirectory())
                    op.accept(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isCanOpenFileChooser() {
        return true;
    }

    private static File[] openFileChooser(String title, @Nullable Path defaultPath, @Nullable String singleFilter, boolean allowMultipleSelects) {
        String st = TinyFileDialogs.tinyfd_openFileDialog(title, defaultPath != null ? defaultPath.toString() : null, null, singleFilter, allowMultipleSelects);
        if (st == null) return null;
        try {
            if (allowMultipleSelects) {
                String[] sp = st.split("\\|");
                File[] fl = new File[sp.length];
                for (int i = 0; i < sp.length; i++) {
                    fl[i] = new File(sp[i]);
                }
                return fl;
            } else {
                return new File[]{new File(st)};
            }
        } catch (Exception ex) {
            return null;
        }
    }
}
