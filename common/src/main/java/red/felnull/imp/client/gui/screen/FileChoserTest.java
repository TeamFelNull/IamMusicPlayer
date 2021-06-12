package red.felnull.imp.client.gui.screen;

import java.io.File;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class FileChoserTest {
    public static void openFileChoser(String title, Consumer<File> op) {
        try {
            Class<?> fcClass = Class.forName("javafx.stage.FileChooser");
            Object fcIns = fcClass.newInstance();

            Method stMethod = fcClass.getDeclaredMethod("setTitle", String.class);
            stMethod.invoke(fcIns, title);

            Class<?> piClass = Class.forName("com.sun.javafx.application.PlatformImpl");
            Method suMethod = piClass.getDeclaredMethod("startup", Runnable.class);

            suMethod.invoke(fcIns, (Runnable) () -> {
                try {
                    Class<?> wdClass = Class.forName("javafx.stage.Window");
                    Method sodMethod = fcClass.getDeclaredMethod("showOpenDialog", wdClass);
                    Object nullble = null;
                    File file = (File) sodMethod.invoke(fcIns, nullble);
                    op.accept(file);
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
            Class<?> fcClass = Class.forName("javafx.stage.FileChooser");
            Class<?> piClass = Class.forName("com.sun.javafx.application.PlatformImpl");
            Class<?> wdClass = Class.forName("javafx.stage.Window");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
