package red.felnull.imp.client.music;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import red.felnull.imp.client.data.MusicSourceClientReferencesType;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.player.URLNotStreamMusicPlayer;
import red.felnull.imp.client.util.SoundMath;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.otyacraftengine.api.ResponseSender;

import java.util.UUID;

public class MusicRinger {
    private final UUID uuid;
    private final PlayMusic music;
    private Vector3d positionVec;
    private IMusicPlayer musicPlayer;
    private boolean readyPlay;
    private float volume;

    public MusicRinger(UUID uuid, PlayMusic playMusic, Vector3d positionVec) {
        this.uuid = uuid;
        this.music = playMusic;
        this.positionVec = positionVec;
    }

    public Vector3d getPosition() {
        return positionVec;
    }

    public void playWait(long startpos) {
        PlayWaitThread pwt = new PlayWaitThread(startpos);
        pwt.start();
    }

    public void playStart() {
        if (readyPlay && musicPlayer != null) {
            musicPlayer.play();
        }
    }

    public void playStartAutoMisalignment() {
        if (readyPlay && musicPlayer != null) {
            musicPlayer.playAutoMisalignment();
        }
    }

    public void playStop() {
        if (readyPlay && musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void volumeUpdate() {
        if (musicPlayer != null) {
            float rarnge = 30f * volume;
            float vol = SoundMath.calculateVolume(volume/10, ClientWorldMusicManager.instance().getEventuallyMusicVolume());
            if (musicPlayer instanceof URLNotStreamMusicPlayer) {
                if (!((URLNotStreamMusicPlayer) musicPlayer).isSpatial())
                    vol = SoundMath.calculatePseudoAttenuation(getPosition(), rarnge, vol);
                else
                    ((URLNotStreamMusicPlayer) musicPlayer).setPos(getPosition(), rarnge);
            }
            musicPlayer.setVolume(vol);
        }
    }

    public IMusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public class PlayWaitThread extends Thread {
        private final long startPos;

        public PlayWaitThread(long pos) {
            this.setName("Play Wait Thread");
            this.startPos = pos;
        }

        @Override
        public void run() {
            if (!readyPlay) {
                try {
                    musicPlayer = MusicSourceClientReferencesType.getMusicPlayer(music);
                    musicPlayer.setVolume(0);
                    musicPlayer.ready(startPos);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            ResponseSender.sendToServer(IMPWorldData.MUSIC_RINGD, 0, uuid.toString(), new CompoundNBT());
            readyPlay = true;
        }
    }
}
