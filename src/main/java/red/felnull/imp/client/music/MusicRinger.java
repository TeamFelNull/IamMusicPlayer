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

    public double getDistance() {
        return Math.sqrt(Objects.requireNonNull(IamMusicPlayer.proxy.getMinecraft().player).getDistanceSq(getPosition()));
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
            float vl = volume;
            float rarnge = 30f * volume;
            float distance = (float) getDistance();
            float zure = rarnge / 5f;
            float atzure = rarnge / 10f;
            float nn = Math.min((vl / (distance - zure)) * 3f, 1f);
            float at = Math.min((vl / (rarnge - zure)) * 3f, 1f);
            float volume = distance <= zure ? 1f : distance <= (rarnge - atzure) ? nn : at * ((distance - rarnge) * -1 / atzure);
            musicPlayer.setVolume((float) (volume * ClientWorldMusicManager.instance().getEventuallyMusicVolume()));
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
