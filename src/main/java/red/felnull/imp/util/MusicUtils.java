package red.felnull.imp.util;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import red.felnull.imp.exception.IMPFFmpegException;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;

import java.io.*;
import java.net.URL;

public class MusicUtils {
    public static float getMP3MillisecondPerFrame(InputStream stream) throws BitstreamException {
        Bitstream bitstream = new Bitstream(stream);
        Header h = bitstream.readFrame();
        return h.ms_per_frame();
    }

    public static float getMP3MillisecondPerFrame(File file) throws IOException, BitstreamException {
        //    Mp3File mfile = new Mp3File(file);
        //    return (float) mfile.getLengthInMilliseconds() / (float) mfile.getFrameCount();
        return getMP3MillisecondPerFrame(new BufferedInputStream(new FileInputStream(file)));
    }

    public static long getMillisecondDuration(URL url) throws EncoderException, IMPFFmpegException {
        MultimediaObject mo = FFmpegUtils.createMultimediaObject(url);
        return mo.getInfo().getDuration();
    }

    public static long getMillisecondDuration(File file) throws EncoderException, IMPFFmpegException {
        MultimediaObject mo = FFmpegUtils.createMultimediaObject(file);
        return mo.getInfo().getDuration();
    }
}
