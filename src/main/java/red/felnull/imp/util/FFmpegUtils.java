package red.felnull.imp.util;

import red.felnull.imp.exception.IMPFFmpegException;
import red.felnull.imp.ffmpeg.FFmpegManeger;
import ws.schild.jave.MultimediaObject;

import java.io.File;
import java.net.URL;

public class FFmpegUtils {

    public static MultimediaObject createMultimediaObject(URL url) throws IMPFFmpegException {
        try {
            MultimediaObject mo = new MultimediaObject(null, FFmpegManeger.instance().getLocator());
            mo.setUR(url);
            return mo;
        } catch (Exception ex) {
            FFmpegManeger.instance().error();
            throw new IMPFFmpegException("Create URL MultimediaObject Error");
        }
    }

    public static MultimediaObject createMultimediaObject(File file) throws IMPFFmpegException {
        try {
            return new MultimediaObject(file, FFmpegManeger.instance().getLocator());
        } catch (Exception ex) {
            FFmpegManeger.instance().error();
            throw new IMPFFmpegException("Create File MultimediaObject Error");
        }
    }
}
