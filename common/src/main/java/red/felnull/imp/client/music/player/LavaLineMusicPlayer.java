package red.felnull.imp.client.music.player;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormatTools;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import red.felnull.imp.client.util.SoundMath;
import red.felnull.imp.music.resource.MusicLocation;
import red.felnull.otyacraftengine.util.IKSGMath;

import javax.sound.sampled.*;
import java.io.IOException;

public class LavaLineMusicPlayer extends LavaAbstractMusicPlayer {
    private SourceDataLine line;
    private boolean ready;
    private boolean stopped;
    private boolean paused;

    public LavaLineMusicPlayer(MusicLocation location, AudioPlayerManager audioPlayerManager, AudioDataFormat dataformat) {
        super(location, audioPlayerManager, dataformat);
    }

    @Override
    public void ready(long position) throws Exception {
        super.ready(position);

        AudioFormat format = AudioDataFormatTools.toAudioFormat(dataformat);
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
        stopped = false;
        paused = false;
    }

    @Override
    public void stop() {
        if (line != null) {
            line.stop();
        }
        stopped = true;
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
    public boolean paused() {
        return paused;
    }

    @Override
    public void pause() {
        super.pause();
        audioPlayer.setPaused(true);
        paused = true;
    }

    @Override
    public void unpause() {
        super.unpause();
        audioPlayer.setPaused(false);
        paused = false;
    }

    @Override
    public boolean playing() {
        return !stopped && !paused;
    }

    @Override
    public boolean stopped() {
        return stopped;
    }

    @Override
    public void setVolume(float f) {
        if (line.isOpen()) {
            if (!disableAttenuation)
                f = SoundMath.calculatePseudoAttenuation(position, attenuation, f) * Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MASTER);
            FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(IKSGMath.clamp((float) (20d * Math.log10(f)), control.getMinimum(), control.getMaximum()));
        }
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
