package red.felnull.imp.util;

import red.felnull.imp.ffmpeg.FFmpegManeger;
import ws.schild.jave.MultimediaObject;

import java.io.File;
import java.net.URL;

public class FFmpegUtils {

    public static MultimediaObject createMultimediaObject(URL url) {
        MultimediaObject mo = new MultimediaObject(null, FFmpegManeger.instance().getLocator());
        mo.setUR(url);
        return mo;
    }

    public static MultimediaObject createMultimediaObject(File file) {

        return new MultimediaObject(file, FFmpegManeger.instance().getLocator());
    }

}
