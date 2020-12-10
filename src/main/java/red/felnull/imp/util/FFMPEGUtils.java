package red.felnull.imp.util;

import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;

import java.io.File;
import java.net.URL;

public class FFMPEGUtils {

    public static MultimediaObject createMultimediaObject(URL url) {

        return new MultimediaObject(url);
    }

    public static MultimediaObject createMultimediaObject(File file) {

        return new MultimediaObject(file);
    }



}
