package red.felnull.imp.util;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MusicUtils {
    public static float getMP3MillisecondPerFrame(InputStream stream) throws BitstreamException {
        Bitstream bitstream = new Bitstream(stream);
        Header h = bitstream.readFrame();
        return h.ms_per_frame();
    }

    public static float getMP3MillisecondPerFrame(File file) throws InvalidDataException, IOException, UnsupportedTagException {
        Mp3File mfile = new Mp3File(file);
        return (float) mfile.getLengthInMilliseconds() / (float) mfile.getFrameCount();
    }
}
