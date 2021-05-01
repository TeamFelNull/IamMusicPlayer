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
import red.felnull.imp.throwable.InvalidIdentifierException;
import red.felnull.otyacraftengine.client.util.IKSGOpenALUtil;
import red.felnull.otyacraftengine.throwable.OpenALException;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.openal.AL11.*;

public class LavaMusicPlayer implements IMusicPlayer {
    private static final Logger LOGGER = LogManager.getLogger(LavaMusicPlayer.class);
    private final int source;
    private final String identifier;
    private final AudioPlayerManager audioPlayerManager;
    private final AudioDataFormat dataformat;
    private final AudioPlayer audioPlayer;
    private ByteBuffer pcm = BufferUtils.createByteBuffer(11025);
    private byte[] buffer = new byte[1024];
    private AudioInputStream stream;
    private boolean trackLoaded;
    private Exception exception;
    private boolean ready;
    private float ang;
    private int trig;

    public LavaMusicPlayer(String identifier, AudioPlayerManager audioPlayerManager, AudioDataFormat dataformat) {
        this.identifier = identifier;
        this.audioPlayerManager = audioPlayerManager;
        this.dataformat = dataformat;
        this.audioPlayer = audioPlayerManager.createPlayer();
        this.source = alGenSources();
    }


    @Override
    public void ready(long position) throws Exception {
        alSourcef(source, AL_PITCH, 1f);
        alSourcei(source, AL_SOURCE_RELATIVE, AL_FALSE);
        alSourcei(source, AL_LOOPING, AL_FALSE);

        IKSGOpenALUtil.checkErrorThrower();
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

        if (stream.read(buffer) >= 0) {
            int bff = alGenBuffers();
            ang = fillBuffer(ang, pcm);
            alBufferData(bff, AL_FORMAT_MONO16, getBuffer(buffer), (int) stream.getFormat().getSampleRate() * 2);
            alSourceQueueBuffers(source, bff);
        }

        LoadThread lt = new LoadThread();
        lt.start();

        ready = true;
    }

    @Override
    public void play(long delay) {
        if (!ready)
            return;

//  alSourcei(source, AL_SEC_OFFSET, 30);
        alSourcePlay(this.source);

    }

    @Override
    public void stop() {
        if (ready)
            alSourceStop(this.source);
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
            alSourceStop(source);
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
        return !ready ? AL_STOPPED : alGetSourcei(this.source, AL_SOURCE_STATE);
    }

    @Override
    public void pause() {
        if (getPlayState() == AL_PLAYING) {
            alSourcePause(this.source);
        }
    }

    @Override
    public void unpause() {
        if (getPlayState() == AL_PAUSED) {
            alSourcePlay(this.source);
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
        int i = alGetSourcei(this.source, AL_BUFFERS_PROCESSED);
        if (i > 0) {
            int[] is = new int[i];
            alSourceUnqueueBuffers(this.source, is);
            try {
                IKSGOpenALUtil.checkErrorThrower();
            } catch (OpenALException e) {
                e.printStackTrace();
            }
            alDeleteBuffers(is);
            try {
                IKSGOpenALUtil.checkErrorThrower();
            } catch (OpenALException e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public class LoadThread extends Thread {
        public LoadThread() {
            setName("LavaPlayer Load Thread");
        }

        @Override
        public void run() {
            try {
                while (stream.read(buffer) >= 0) {
                    int b = alGenBuffers();
                    ang = fillBuffer(ang, pcm);
                    alBufferData(b, AL_FORMAT_MONO16, getBuffer(buffer), (int) stream.getFormat().getSampleRate() * 2);
                    alSourceQueueBuffers(source, b);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                System.out.println("finished");
            }
        }
    }

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
