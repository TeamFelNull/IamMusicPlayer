package red.felnull.imp.client.music;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.data.MusicSourceClientReferencesType;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.otyacraftengine.api.ResponseSender;

import java.util.Objects;
import java.util.UUID;

public class MusicRinger {
    private final UUID uuid;
    private final PlayMusic music;
    private Vector3d positionVec;
    private IMusicPlayer musicPlayer;

    public MusicRinger(UUID uuid, PlayMusic playMusic, Vector3d positionVec) {
        this.uuid = uuid;
        this.music = playMusic;
        this.positionVec = positionVec;
    }

    public Vector3d getPosition() {
        return positionVec;
    }

    public double getDistance() {

        return Objects.requireNonNull(IamMusicPlayer.proxy.getMinecraft().player).getDistanceSq(getPosition());
    }

    public void playWait() {
        PlayWaitThread pwt = new PlayWaitThread();
        pwt.start();
    }

    public void playStart() {

    }

    public IMusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public class PlayWaitThread extends Thread {

        @Override
        public void run() {
            try {
                musicPlayer = MusicSourceClientReferencesType.getMusicPlayer(music);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ResponseSender.sendToServer(IMPWorldData.MUSIC_RINGD, 0, uuid.toString(), new CompoundNBT());
        }
    }
}
