package red.felnull.imp.client.music;

import com.github.kiulian.downloader.YoutubeException;
import javazoom.jl.decoder.BitstreamException;
import net.minecraft.util.math.vector.Vector3d;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.data.MusicSourceClientReferencesType;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.exception.IMPWorldMusicException;
import red.felnull.imp.music.resource.PlayMusic;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.util.Objects;

public class MusicRinger {
    private final PlayMusic music;
    private Vector3d positionVec;
    private IMusicPlayer musicPlayer;
    private boolean playWaiting;
    private boolean playReady;

    public MusicRinger(PlayMusic playMusic, Vector3d positionVec) {
        this.music = playMusic;
        this.positionVec = positionVec;
    }

    public Vector3d getPosition() {
        return positionVec;
    }

    public double getDistance() {

        return Objects.requireNonNull(IamMusicPlayer.proxy.getMinecraft().player).getDistanceSq(getPosition());
    }

    public void playWait() throws YoutubeException, BitstreamException, IMPWorldMusicException, EncoderException, InterruptedException, IOException {
        playWaiting = true;
        PlayWaitThread pwt = new PlayWaitThread();
        pwt.start();
    }

    public void playStart() {

    }

    public class PlayWaitThread extends Thread {

        @Override
        public void run() {
            try {
                musicPlayer = MusicSourceClientReferencesType.getMusicPlayer(music);
                playReady = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
