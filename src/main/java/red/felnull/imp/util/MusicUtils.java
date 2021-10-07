package red.felnull.imp.util;

import red.felnull.imp.exception.IMPFFmpegException;
import red.felnull.imp.lava.LavaPlayerPort;

import java.io.File;
import java.net.URL;

public class MusicUtils {
    /*public static float getMP3MillisecondPerFrame(InputStream stream) throws BitstreamException {
        Bitstream bitstream = new Bitstream(stream);
        Header h = bitstream.readFrame();
        return h.ms_per_frame();
    }

    public static float getMP3MillisecondPerFrame(File file) throws IOException, BitstreamException {
        return getMP3MillisecondPerFrame(new BufferedInputStream(new FileInputStream(file)));
    }*/

    public static long getMillisecondDuration(URL url) throws IMPFFmpegException {
        return LavaPlayerPort.getDuration(url.toString());
        // MultimediaObject mo = FFmpegUtils.createMultimediaObject(url);
        // return FFmpegUtils.getInfo(mo).getDuration();
    }

    public static long getMillisecondDuration(File file) throws IMPFFmpegException {
        return LavaPlayerPort.getDuration(file);
        //  MultimediaObject mo = FFmpegUtils.createMultimediaObject(file);
        //  return FFmpegUtils.getInfo(mo).getDuration();
    }
}
