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
import red.felnull.imp.music.resource.MusicLocation;
import red.felnull.imp.throwable.InvalidIdentifierException;
import red.felnull.otyacraftengine.client.util.IKSGOpenALUtil;
import red.felnull.otyacraftengine.throwable.OpenALException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL11.*;

public class LavaMusicPlayer implements IMusicPlayer {
    private static final Logger LOGGER = LogManager.getLogger(LavaMusicPlayer.class);
    private final List<Integer> buffers = new ArrayList<>();
    private final int source;
    private final MusicLocation musicLocation;
    private boolean stopped;
    private final AudioPlayerManager audioPlayerManager;
    private final AudioDataFormat dataformat;
    private final AudioPlayer audioPlayer;
    private ByteBuffer pcm = BufferUtils.createByteBuffer(11025);
    private byte[] buffer = new byte[1024];
    private AudioInputStream stream;
    private long startTime;
    private long startPosition;
    private boolean trackLoaded;
    private Exception exception;
    private boolean ready;
    private long duration;
    private float ang;
    private int trig;


    public LavaMusicPlayer(MusicLocation location, AudioPlayerManager audioPlayerManager, AudioDataFormat dataformat) {
        this.musicLocation = location;
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
        startPosition = position;
        IKSGOpenALUtil.checkErrorThrower();
        this.trackLoaded = false;
        audioPlayerManager.loadItem(musicLocation.getIdentifier(), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setPosition(position);
                audioPlayer.startTrack(track, false);
                if (!track.getInfo().isStream)
                    duration = track.getDuration();
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
            AudioFormat format = stream.getFormat();
            int fomatId = audioFormatToOpenAl(format);
            alBufferData(bff, fomatId, getBuffer(buffer), (int) format.getSampleRate());
            buffers.add(bff);
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

        startTime = System.currentTimeMillis();
        startPosition += delay;

        if (duration == 0 || duration >= startPosition) {
            float secdelay = delay / 1000f;
            alSourcef(source, AL_MAX_DISTANCE, secdelay);
            alSourcePlay(this.source);
        }
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
        stopped = true;
        startTime = 0;
        startPosition = 0;
        if (ready) {
            alSourceStop(source);
            alDeleteSources(source);
            List<Integer> bffs = new ArrayList<>(buffers);
            bffs.forEach(AL11::alDeleteBuffers);
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

    @Override
    public MusicLocation getMusicLocation() {
        return musicLocation;
    }

    @Override
    public long getPosition() {
        return System.currentTimeMillis() - startTime + startPosition;
    }


    public class LoadThread extends Thread {
        public LoadThread() {
            setName("LavaPlayer Load Thread");
        }

        @Override
        public void run() {
            try {
                while (stream.read(buffer) >= 0) {
                    if (stopped)
                        return;
                    int b = alGenBuffers();
                    if (stopped)
                        return;
                    ang = fillBuffer(ang, pcm);
                    if (stopped)
                        return;
                    AudioFormat format = stream.getFormat();
                    if (stopped)
                        return;
                    int formatId = audioFormatToOpenAl(format);
                    if (stopped)
                        return;
                    alBufferData(b, formatId, getBuffer(buffer), (int) format.getSampleRate());
                    if (stopped)
                        return;
                    alSourceQueueBuffers(source, b);
                    if (stopped)
                        return;
                    buffers.add(b);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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

    private static int audioFormatToOpenAl(AudioFormat audioFormat) {
        AudioFormat.Encoding encoding = audioFormat.getEncoding();
        int i = audioFormat.getChannels();
        int j = audioFormat.getSampleSizeInBits();
        if (encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED) || encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            if (i == 1) {
                if (j == 8) {
                    return AL_FORMAT_MONO8;
                }

                if (j == 16) {
                    return AL_FORMAT_MONO16;
                }
            } else if (i == 2) {
                if (j == 8) {
                    return AL_FORMAT_STEREO8;
                }

                if (j == 16) {
                    return AL_FORMAT_STEREO16;
                }
            }
        }

        throw new IllegalArgumentException("Invalid audio format: " + audioFormat);
    }
}
