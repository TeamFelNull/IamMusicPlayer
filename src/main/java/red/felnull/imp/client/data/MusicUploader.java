package red.felnull.imp.client.data;

import com.mpatric.mp3agic.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.client.gui.toasts.MusicUploadToast;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.musicplayer.PlayImage;
import red.felnull.imp.util.PathUtil;
import red.felnull.otyacraftengine.api.DataSendReceiverManager;
import red.felnull.otyacraftengine.util.IKSGDataUtil;
import red.felnull.otyacraftengine.util.IKSGFileLoadUtil;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.progress.EncoderProgressListener;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class MusicUploader {
    private static MusicUploader INSTANCE;
    private final Map<String, MusicUploadData> stateDatas = new HashMap<>();

    public static void init() {
        INSTANCE = new MusicUploader();
    }

    public static MusicUploader instance() {
        return INSTANCE;
    }

    public void startUpload(String name, Path path, String uuid, PlayImage image, byte[] imageData) {
        startUpload(name, path, null, uuid, image, imageData);
    }

    public void startUpload(String name, URL url, String uuid, PlayImage image, byte[] imageData) {
        startUpload(name, null, url, uuid, image, imageData);
    }

    public void startUpload(String name, Path path, URL url, String uuid, PlayImage image, byte[] imageData) {
        stateDatas.put(uuid, new MusicUploadData(name, image, imageData));
        MusicUploadToast.add(uuid);
        UploadThread ut = new UploadThread(path, url, uuid);
        ut.start();
    }


    protected void upload(URL url, String uuid) {
        try {
            setState(uuid, MusicUploadData.UploadState.CONVERTING);
            if (!conversion(url, uuid, 128)) {
                setState(uuid, MusicUploadData.UploadState.ERROR);
                return;
            }
            setState(uuid, MusicUploadData.UploadState.COMPRESSING);
            byte[] compdata = compressing(uuid);
            setState(uuid, MusicUploadData.UploadState.SENDING);
            setProgress(uuid, 0f);
            DataSendReceiverManager.instance().sendToServer(IMPWorldData.SERVER_TMP_DATA, uuid, compdata);
        } catch (Exception ex) {
            setState(uuid, MusicUploadData.UploadState.ERROR);
        }
    }

    protected void upload(Path path, String uuid) {
        try {
            setState(uuid, MusicUploadData.UploadState.CONVERTING);
            if (!conversion(path, uuid, 128)) {
                setState(uuid, MusicUploadData.UploadState.ERROR);
                return;
            }
            setState(uuid, MusicUploadData.UploadState.COMPRESSING);
            byte[] compdata = compressing(uuid);
            setState(uuid, MusicUploadData.UploadState.SENDING);
            setProgress(uuid, 0f);
            DataSendReceiverManager.instance().sendToServer(IMPWorldData.SERVER_TMP_DATA, uuid, compdata);
        } catch (Exception ex) {
            setState(uuid, MusicUploadData.UploadState.ERROR);
        }
    }

    private boolean conversion(URL url, String uuid, int bitrate) throws UnsupportedTagException, NotSupportedException, EncoderException, InvalidDataException, IOException {
        MultimediaObject mo = new MultimediaObject(url);
        return conversion(mo, uuid, bitrate);
    }

    private boolean conversion(Path path, String uuid, int bitrate) throws UnsupportedTagException, NotSupportedException, EncoderException, InvalidDataException, IOException {
        MultimediaObject mo = new MultimediaObject(path.toFile());
        return conversion(mo, uuid, bitrate);
    }

    private boolean conversion(MultimediaObject mo, String uuid, int bitrate) throws EncoderException, InvalidDataException, IOException, UnsupportedTagException, NotSupportedException {
        if (mo.getInfo().getDuration() == -1)
            return false;
        IKSGFileLoadUtil.createFolder(PathUtil.getClientTmpFolder());
        AudioAttributes aa = new AudioAttributes();
        aa.setCodec("libmp3lame");
        aa.setBitRate(bitrate);
        aa.setChannels(1);
        aa.setSamplingRate(32000);
        EncodingAttributes ea = new EncodingAttributes();
        ea.setOutputFormat("mp3");
        ea.setAudioAttributes(aa);
        Encoder encoder = new Encoder();
        encoder.encode(mo, PathUtil.getClientTmpFolder().resolve(uuid + "-tmp").toFile(), ea, new EncoderProgressListener() {
            @Override
            public void sourceInfo(MultimediaInfo info) {
            }

            @Override
            public void progress(int permil) {
                setProgress(uuid, (float) permil / 1000f);
            }

            @Override
            public void message(String message) {
            }
        });
        Mp3File m3f = new Mp3File(PathUtil.getClientTmpFolder().resolve(uuid + "-tmp").toFile());
        m3f.setId3v1Tag(new ID3v1Tag());
        m3f.setId3v2Tag(new ID3v24Tag());
        m3f.setCustomTag(new byte[0]);
        m3f.save(PathUtil.getClientTmpFolder().resolve(uuid).toString());
        IKSGFileLoadUtil.deleteFile(PathUtil.getClientTmpFolder().resolve(uuid + "-tmp"));
        return true;
    }

    private byte[] compressing(String uuid) throws IOException {
        File file = PathUtil.getClientTmpFolder().resolve(uuid).toFile();
        byte[] bytes = IKSGFileLoadUtil.fileBytesReader(file.toPath());
        IKSGFileLoadUtil.deleteFile(file);
        return IKSGDataUtil.gzZipping(bytes);
    }

    public boolean isUploaded(String uuid) {
        return stateDatas.containsKey(uuid);
    }

    public void setState(String uuid, MusicUploadData.UploadState state) {

        if (!stateDatas.containsKey(uuid))
            return;

        stateDatas.get(uuid).setState(state);
    }

    public void setProgress(String uuid, float parsent) {

        if (!stateDatas.containsKey(uuid))
            return;

        stateDatas.get(uuid).setProgress(parsent);
    }

    public MusicUploadData getStateData(String uuid) {
        return stateDatas.get(uuid);
    }


    private class UploadThread extends Thread {
        private final Path path;
        private final String uuid;
        private final URL url;

        public UploadThread(Path path, URL url, String uuid) {
            this.path = path;
            this.uuid = uuid;
            this.url = url;
        }

        @Override
        public void run() {
            if (path != null)
                upload(path, uuid);
            else if (url != null)
                upload(url, uuid);
        }
    }


}
