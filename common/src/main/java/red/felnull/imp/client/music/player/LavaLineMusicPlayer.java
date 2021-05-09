package red.felnull.imp.client.music.player;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.minecraft.world.phys.Vec3;
import red.felnull.imp.music.resource.MusicLocation;

import javax.sound.sampled.*;
import java.io.IOException;

public class LavaLineMusicPlayer extends LavaAbstractMusicPlayer {
    private SourceDataLine line;
    private boolean ready;

    public LavaLineMusicPlayer(MusicLocation location, AudioPlayerManager audioPlayerManager, AudioDataFormat dataformat) {
        super(location, audioPlayerManager, dataformat);
    }

    @Override
    public void ready(long position) throws Exception {
        super.ready(position);

        AudioFormat format = stream.getFormat();
        DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
        Mixer mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
        line = (SourceDataLine) mixer.getLine(speakerInfo);
        AudioDataFormat dataFormat = dataformat;
        line.open(format, dataFormat.chunkSampleCount * dataFormat.channelCount * 2 * 5);

        ready = true;
    }

    @Override
    public void play(long delay) {
        if (!ready)
            return;

        super.play(delay);

        if (duration == 0 || duration >= startPosition) {
            PlayThread pt = new PlayThread();
            pt.start();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void setPosition(long position) {

    }

    @Override
    public void destroy() {
        if (line != null) {
            line.flush();
            line.stop();
            line.close();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void unpause() {

    }

    @Override
    public boolean playing() {
        return false;
    }

    @Override
    public boolean stopped() {
        return false;
    }

    @Override
    public void setSelfPosition(Vec3 vec3) {

    }

    @Override
    public void setVolume(float f) {

    }

    @Override
    public void linearAttenuation(float f) {

    }

    @Override
    public void disableAttenuation() {

    }

    public class PlayThread extends Thread {
        public PlayThread() {
            setName("LavaPlayer Play Thread");
        }

        @Override
        public void run() {
            long frameDuration = dataformat.frameDuration();
            line.start();
            final byte[] buffer = new byte[dataformat.chunkSampleCount * dataformat.channelCount * 2];
            int chunkSize;
            try {
                while (true) {
                    if (!audioPlayer.isPaused()) {
                        if ((chunkSize = stream.read(buffer)) >= 0) {
                            line.write(buffer, 0, chunkSize);
                        } else {
                            throw new IllegalStateException("Audiostream ended. This should not happen.");
                        }
                    } else {
                        line.drain();
                        sleep(frameDuration);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
