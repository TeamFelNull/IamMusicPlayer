package red.felnull.imp.client.util;

import java.io.File;

public class FileUtils {
    public static void openFileChoser(String title, OpenFile op) {
        try {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle(title);
            com.sun.javafx.application.PlatformImpl.startup(() -> {
                File file = fc.showOpenDialog(null);
                op.openFile(file);
            });
        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isCanOpenFileChooser() {
        try {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            return true;
        } catch (NoClassDefFoundError ex) {
            return false;
        }
    }

    public interface OpenFile {
        void openFile(File file);
    }
}
