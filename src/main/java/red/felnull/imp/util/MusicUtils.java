package red.felnull.imp.util;

import red.felnull.imp.client.util.YoutubeUtils;
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
    public static long getMillisecondDuration(String videoID) {
        try {
            String url = YoutubeUtils.getYoutubeMa4DirectLink(videoID);
            if (url != null)
                return LavaPlayerPort.getDuration(url);
        } catch (Exception ignored) {
        }
        return LavaPlayerPort.getDurationByYoutube(videoID);
    }

    public static long getMillisecondDuration(URL url) {
        return LavaPlayerPort.getDuration(url.toString());
        // MultimediaObject mo = FFmpegUtils.createMultimediaObject(url);
        // return FFmpegUtils.getInfo(mo).getDuration();
    }

    public static long getMillisecondDuration(File file) {
        return LavaPlayerPort.getDuration(file);
        //  MultimediaObject mo = FFmpegUtils.createMultimediaObject(file);
        //  return FFmpegUtils.getInfo(mo).getDuration();
    }
}
