package red.felnull.imp.client.util;

import java.io.File;
import java.lang.reflect.Method;

public class FileUtils {
    public static void openFileChoser(String title, OpenFile op) {
        try {
         /*   javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle(title);
            com.sun.javafx.application.PlatformImpl.startup(() -> {
                File file = fc.showOpenDialog(null);
                op.openFile(file);
            });*/
            Class<?> fcClass = Class.forName("javafx.stage.FileChooser");
            Object fcIns = fcClass.newInstance();

            Method stMethod = fcClass.getDeclaredMethod("setTitle", String.class);
            stMethod.invoke(fcIns, "A-ikiso");

            Class<?> piClass = Class.forName("com.sun.javafx.application.PlatformImpl");
            Method suMethod = piClass.getDeclaredMethod("startup", Runnable.class);

            suMethod.invoke(fcIns, (Runnable) () -> {
                try {
                    Class<?> wdClass = Class.forName("javafx.stage.Window");
                    Method sodMethod = fcClass.getDeclaredMethod("showOpenDialog", wdClass);
                    Object nullble = null;
                    File file = (File) sodMethod.invoke(fcIns, nullble);
                    op.openFile(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isCanOpenFileChooser() {
        try {
            //     javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            Class<?> fcClass = Class.forName("javafx.stage.FileChooser");
            Class<?> piClass = Class.forName("com.sun.javafx.application.PlatformImpl");
            Class<?> wdClass = Class.forName("javafx.stage.Window");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public interface OpenFile {
        void openFile(File file);
    }
}
