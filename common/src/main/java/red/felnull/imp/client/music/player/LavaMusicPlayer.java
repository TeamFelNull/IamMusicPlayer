package red.felnull.imp.client.music.player;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;
import red.felnull.imp.throwable.InvalidIdentifierException;
import red.felnull.otyacraftengine.client.util.IKSGOpenALUtil;
import red.felnull.otyacraftengine.throwable.OpenALException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.openal.AL11.*;

public class LavaMusicPlayer implements IMusicPlayer {
    private static final Logger LOGGER = LogManager.getLogger(LavaMusicPlayer.class);

    private final String identifier;
    private final AudioPlayerManager audioPlayerManager;
    private final AudioDataFormat dataformat;
    private final AudioPlayer audioPlayer;
    private AudioInputStream stream;
    private boolean trackLoaded;
    private boolean playing;
    private Exception exception;
    private int source;
    private boolean ready;

    public LavaMusicPlayer(String identifier, AudioPlayerManager audioPlayerManager, AudioDataFormat dataformat) {
        this.identifier = identifier;
        this.audioPlayerManager = audioPlayerManager;
        this.dataformat = dataformat;
        this.audioPlayer = audioPlayerManager.createPlayer();
    }


    @Override
    public void ready(long position) throws Exception {
        this.trackLoaded = false;
        audioPlayerManager.loadItem(identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setPosition(position);
                audioPlayer.startTrack(track, false);
                trackLoaded = true;
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                exception = new InvalidIdentifierException("ambiguous");
                trackLoaded = true;
            }

            @Override
            public void noMatches() {
                exception = new InvalidIdentifierException("nomatche");
                trackLoaded = true;
            }

            @Override
            public void loadFailed(FriendlyException ex) {
                exception = ex;
                trackLoaded = true;
            }
        });

        while (!trackLoaded) {
            Thread.sleep(20);
        }

        if (exception != null)
            throw exception;

        stream = AudioPlayerInputStream.createStream(audioPlayer, dataformat, dataformat.frameDuration(), false);
        source = alGenSources();

        alSourcef(source, AL_PITCH, 1f);
        alSourcei(source, AL_SOURCE_RELATIVE, AL_FALSE);
        alSourcei(source, AL_LOOPING, AL_FALSE);
        setVolume(1f);
        setSelfPosition(Vec3.ZERO);
        linearAttenuation(0f);

        IKSGOpenALUtil.checkErrorThrower();

        ready = true;
    }

    @Override
    public void play(long delay) {

        if (!ready)
            return;

/*
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            alSourcei(source, AL_SEC_OFFSET, 30);
        }).start();
*/

        if (playing)
            return;

        PlayThread pt = new PlayThread();
        pt.start();
    }

    @Override
    public void stop() {
        if (ready)
            AL11.alSourceStop(this.source);
    }

    @Override
    public void setPosition(long position) {
        if (audioPlayer.getPlayingTrack() != null) {
            audioPlayer.getPlayingTrack().setPosition(position);
        }
    }

    @Override
    public void destroy() {
        if (ready) {
            AL11.alSourceStop(source);
            try {
                IKSGOpenALUtil.checkErrorThrower();
            } catch (OpenALException e) {
                e.printStackTrace();
            }
            if (this.stream != null) {
                try {
                    this.stream.close();
                } catch (IOException var2) {
                    LOGGER.error("Failed to close audio stream", var2);
                }

                this.removeProcessedBuffers();
                this.stream = null;
            }
        }
    }

    private int getPlayState() {
        return !ready ? AL_STOPPED : AL11.alGetSourcei(this.source, AL_SOURCE_STATE);
    }

    @Override
    public void pause() {
        if (getPlayState() == AL_PLAYING) {
            AL11.alSourcePause(this.source);
        }
    }

    @Override
    public void unpause() {
        if (getPlayState() == AL_PAUSED) {
            AL11.alSourcePlay(this.source);
        }
    }

    @Override
    public boolean playing() {
        return getPlayState() == AL_PLAYING;
    }

    @Override
    public boolean stopped() {
        return getPlayState() == AL_STOPPED;
    }

    @Override
    public void setSelfPosition(Vec3 sp) {
        alSource3f(source, AL_POSITION, (float) sp.x, (float) sp.y, (float) sp.z);
    }

    @Override
    public void setVolume(float f) {
        alSourcef(source, AL_GAIN, f);
    }

    @Override
    public void linearAttenuation(float f) {
        alSourcei(source, AL_DISTANCE_MODEL, 53251);
        alSourcef(source, AL_MAX_DISTANCE, f);
        alSourcef(source, AL_ROLLOFF_FACTOR, 1.0F);
        alSourcef(source, AL_REFERENCE_DISTANCE, 0.0F);
    }

    @Override
    public void disableAttenuation() {
        alSourcei(this.source, AL_DISTANCE_MODEL, AL_FALSE);
    }

    @Override
    public void update() {

    }

    private int removeProcessedBuffers() {
        int i = AL11.alGetSourcei(this.source, 4118);
        if (i > 0) {
            int[] is = new int[i];
            AL11.alSourceUnqueueBuffers(this.source, is);
            try {
                IKSGOpenALUtil.checkErrorThrower();
            } catch (OpenALException e) {
                e.printStackTrace();
            }
            AL11.alDeleteBuffers(is);
            try {
                IKSGOpenALUtil.checkErrorThrower();
            } catch (OpenALException e) {
                e.printStackTrace();
            }
        }

        return i;
    }


    public class PlayThread extends Thread {
        @Override
        public void run() {
            try {
                float ang = 0;
                ByteBuffer pcm = BufferUtils.createByteBuffer(11025);
                AudioFormat format = stream.getFormat();
                byte[] buffer = new byte[1024];
                boolean statted = false;

                while (true) {
                    if (stream.read(buffer) >= 0) {
                        int b = alGenBuffers();
                        ang = fillBuffer(ang, pcm);
                        alBufferData(b, AL_FORMAT_MONO16, getBuffer(buffer), (int) format.getSampleRate() * 2);
                        alSourceQueueBuffers(source, b);

                        if (!statted) {
                            alSourcePlay(source);
                            statted = true;
                        }
                    }
                    int state = alGetSourcei(source, AL_SOURCE_STATE);
                    if (state == AL_STOPPED)
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                playing = false;
                System.out.println("finished");
            }
        }

        private int trig = 0;

        public float fillBuffer(float ang, ByteBuffer buff) {
            int size = buff.capacity();
            trig++;
            for (int i = 0; i < size; i++) {
                int source1 = (int) (Math.sin(ang) * 127 + 128);
                int source2 = 0;
                if (trig > 3) source2 = (int) (Math.sin(ang * 3) * 127 + 128);
                if (trig > 4) trig = 0;
                buff.put(i, (byte) ((source1 + source2) / 2));
                ang += 0.1f;
            }
            return ang;
        }


        public ByteBuffer getBuffer(byte[] array) {
            ByteBuffer audioBuffer2 = BufferUtils.createByteBuffer(array.length);
            audioBuffer2.put(array);
            audioBuffer2.flip();
            return audioBuffer2;
        }

    }
}
