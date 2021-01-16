package red.felnull.imp.util;

import red.felnull.imp.exception.IMPFFmpegException;
import red.felnull.imp.ffmpeg.FFmpegErrorReporter;
import red.felnull.imp.ffmpeg.FFmpegManeger;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.progress.EncoderProgressListener;

import java.io.File;
import java.net.URL;

public class FFmpegUtils {

    public static MultimediaObject createMultimediaObject(URL url) throws IMPFFmpegException {
        try {
            MultimediaObject mo = new MultimediaObject(null, FFmpegManeger.instance().getLocator());
            mo.setUR(url);
            return mo;
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPFFmpegException impfFmpegException = new IMPFFmpegException("Create URL MultimediaObject Error");
            FFmpegErrorReporter.reportExport(ex, impfFmpegException);
            FFmpegManeger.instance().error(impfFmpegException);
            throw impfFmpegException;
        }
    }

    public static MultimediaObject createMultimediaObject(File file) throws IMPFFmpegException {
        try {
            return new MultimediaObject(file, FFmpegManeger.instance().getLocator());
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPFFmpegException impfFmpegException = new IMPFFmpegException("Create File MultimediaObject Error");
            FFmpegErrorReporter.reportExport(ex, impfFmpegException);
            FFmpegManeger.instance().error(impfFmpegException);
            throw impfFmpegException;
        }
    }

    public static MultimediaInfo getInfo(MultimediaObject multimediaObject) throws IMPFFmpegException {
        try {
            return multimediaObject.getInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
            IMPFFmpegException impfFmpegException = new IMPFFmpegException("Get Info Error");
            FFmpegErrorReporter.reportExport(ex, impfFmpegException);
            FFmpegManeger.instance().error(impfFmpegException);
            throw impfFmpegException;
        }
    }

    public static void encode(MultimediaObject multimediaObject, File file, String codec, int bitrate, int channels, int samplingRate, String format, long offset, long duration) throws IMPFFmpegException {
        encode(multimediaObject, file, codec, bitrate, channels, samplingRate, format, null, offset, duration);
    }

    public static void encode(MultimediaObject multimediaObject, File file, String codec, int bitrate, int channels, int samplingRate, String format) throws IMPFFmpegException {
        encode(multimediaObject, file, codec, bitrate, channels, samplingRate, format, null, 0, 0);
    }

    public static void encode(MultimediaObject multimediaObject, File file, String codec, int bitrate, int channels, int samplingRate, String format, EncoderProgressListener listener) throws IMPFFmpegException {
        encode(multimediaObject, file, codec, bitrate, channels, samplingRate, format, listener, 0, 0);
    }

    public static void encode(MultimediaObject multimediaObject, File file, String codec, int bitrate, int channels, int samplingRate, String format, EncoderProgressListener listener, long offset, long duration) throws IMPFFmpegException {
        try {
            Encoder encoder = new Encoder();
            AudioAttributes aa = new AudioAttributes();
            aa.setCodec(codec);
            aa.setBitRate(bitrate);
            aa.setChannels(channels);
            aa.setSamplingRate(samplingRate);
            EncodingAttributes ea = new EncodingAttributes();
            ea.setOutputFormat(format);
            ea.setAudioAttributes(aa);

            float offsetF = (float) offset / 1000f;
            float durationF = (float) duration / 1000f;

            if (offsetF > 0f)
                ea.setOffset(offsetF);
            if (durationF > 0f)
                ea.setDuration(durationF);

            encoder.encode(multimediaObject, file, ea, listener);

        } catch (Exception ex) {
            ex.printStackTrace();
            IMPFFmpegException impfFmpegException = new IMPFFmpegException("Encode Error");
            FFmpegErrorReporter.reportExport(ex, impfFmpegException);
            FFmpegManeger.instance().error(impfFmpegException);
            throw impfFmpegException;
        }
    }
}
