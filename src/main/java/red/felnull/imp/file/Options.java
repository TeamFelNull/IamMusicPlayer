package red.felnull.imp.file;

import red.felnull.imp.client.screen.IMPSoundSlider;
import red.felnull.imp.util.FileHelper;
import red.felnull.imp.util.FileLoader;

import java.util.HashMap;


public class Options {

    public static void loadOption() {
        HashMap<String, String> opmap = new HashMap<String, String>();
        FileLoader.txtReader(opmap, FileHelper.getClientOptionTxtPath());
        if (opmap.isEmpty()) {
            opmap.put("soundCategory_iammusicplayer", "1.0");
            FileLoader.txtWriter(opmap, FileHelper.getClientOptionTxtPath());
        }
        IMPSoundSlider.AllSoundVolume = Float.valueOf(opmap.get("soundCategory_iammusicplayer"));
    }

    public static void writeOption(boolean tread) {
        if (!tread) {
            HashMap<String, String> opmap = new HashMap<String, String>();
            opmap.put("soundCategory_iammusicplayer", String.valueOf(IMPSoundSlider.AllSoundVolume));
            FileLoader.txtWriter(opmap, FileHelper.getClientOptionTxtPath());
        } else {
            WriteThread wt = new WriteThread();
            wt.start();
        }
    }
}

class WriteThread extends Thread {
    public void run() {
        Options.writeOption(false);
    }
}
