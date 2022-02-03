package red.felnull.imp.client.music.player;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormatTools;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL11.*;

public class URLNotStreamMusicPlayer implements IMusicPlayer {
    protected final AudioPlayerManager audioPlayerManager;
    protected final AudioDataFormat dataformat;
    protected final AudioPlayer audioPlayer;
    private final List<Integer> buffers = new ArrayList<>();
    protected final URL url;
    protected boolean trackLoaded;
    private long readyTime;
    protected boolean stereo;
    protected long duration;
    protected Exception exception;
    protected long startPosition;
    protected AudioInputStream stream;
    protected long startTime;
    private int source;
    private boolean intentionallyMono;
    private boolean stopped;
    private float ang;
    private byte[] buffer = new byte[1024 * 3];
    private ByteBuffer pcm = BufferUtils.createByteBuffer(11025);
    private int trig;
    protected boolean ready;

    public URLNotStreamMusicPlayer(long readyTime, String url, LavaMusicLoader loader) {
        URL url1 = null;
        try {
            if (url != null)
                url1 = new URL(url);
        } catch (Exception ignored) {
        }
        this.url = url1;
        this.readyTime = readyTime;
        this.dataformat = loader.getFormat();
        this.audioPlayerManager = loader.getAudioPlayerManager();
        this.audioPlayerManager.getConfiguration().setOutputFormat(dataformat);
        this.audioPlayer = audioPlayerManager.createPlayer();
        this.source = alGenSources();
        this.intentionallyMono = loader.isMono();

        if (!intentionallyMono)
            disableAttenuation();
    }

    public boolean isSpatial() {
        return intentionallyMono;
    }

    @Override
    public void setSpatial(boolean enable) {
        if (!enable) {
            disableAttenuation();
            intentionallyMono = false;
        }
    }

    public void setPos(Vector3d pos, float range) {
        alSource3f(source, AL_POSITION, (float) pos.x, (float) pos.y, (float) pos.z);
        linearAttenuation(range);
    }

    private void linearAttenuation(float f) {
        alSourcei(source, AL_DISTANCE_MODEL, 53251);
        alSourcef(source, AL_MAX_DISTANCE, f);
        alSourcef(source, AL_ROLLOFF_FACTOR, 1.0F);
        alSourcef(source, AL_REFERENCE_DISTANCE, 0.0F);
    }

    private void disableAttenuation() {
        alSourcei(this.source, AL_DISTANCE_MODEL, AL_FALSE);
    }


    @Override
    public void ready(long position) throws Exception {
        startPosition = position;
        this.trackLoaded = false;
        audioPlayerManager.loadItem(url.toString(), new AudioLoadResultHandler() {
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
                exception = new IllegalStateException("ambiguous");
                trackLoaded = true;
            }

            @Override
            public void noMatches() {
                exception = new IllegalStateException("nomatche");
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
        stereo = AudioDataFormatTools.toAudioFormat(dataformat).getChannels() >= 2;

        loadOpenAL();

        ready = true;
    }

    protected void loadOpenAL() {
        alSourcef(source, AL_PITCH, 1f);
        alSourcei(source, AL_SOURCE_RELATIVE, AL_FALSE);
        alSourcei(source, AL_LOOPING, AL_FALSE);
        //OpenALUtil.checkErrorThrower();

        AudioFormat format = AudioDataFormatTools.toAudioFormat(dataformat);
        for (int i = 0; i < 500; i++) {
            try {
                if (stream != null && stream.read(buffer) >= 0) {
                    int bff = alGenBuffers();
                    ang = fillBuffer(ang, pcm);

                    int formatId = audioFormatToOpenAl(format);
                    alBufferData(bff, formatId, getBuffer(buffer), (int) format.getSampleRate() * (intentionallyMono ? 2 : 1));
                    buffers.add(bff);
                    alSourceQueueBuffers(source, bff);
                }
            } catch (IOException ignored) {
            }
        }
        LoadThread lt = new LoadThread();
        lt.start();
    }

    @Override
    public void play() {
        playMisalignment(0);
    }

    @Override
    public void playMisalignment(long delay) {
        if (!ready)
            return;

        startTime = System.currentTimeMillis();
        startPosition += delay;

        if (duration == 0 || duration >= startPosition) {
            float secdelay = delay / 1000f;
            alSourcef(source, AL_SEC_OFFSET, secdelay);
            alSourcePlay(this.source);
        }
    }

    @Override
    public void playAutoMisalignment() {
        long zure = System.currentTimeMillis() - readyTime;
        if (getMaxMisalignment() > zure) {
            playMisalignment(zure);
        }
    }

    @Override
    public void playAndReady(long startMiliSecond) throws Exception {
        ready(startMiliSecond);
        play();
    }

    @Override
    public void stop() {
        if (ready)
            alSourceStop(this.source);
        stopped = true;
        startTime = 0;
        startPosition = 0;
        if (this.stream != null) {
            try {
                this.stream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.stream = null;
        }
        audioPlayer.destroy();
        if (ready) {
            alSourceStop(source);
            alDeleteSources(source);
            List<Integer> bffs = new ArrayList<>(buffers);
            bffs.forEach(AL11::alDeleteBuffers);
            buffers.clear();
            // OpenALUtil.checkErrorThrower();
        }
    }

    private int getPlayState() {
        if (!ready)
            return AL_STOPPED;
        return alGetSourcei(this.source, AL_SOURCE_STATE);
    }

    @Override
    public boolean isPlaying() {
        return getPlayState() == AL_PLAYING;
    }

    @Override
    public long getMaxMisalignment() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long getCureentElapsed() {
        return System.currentTimeMillis() - startTime + startPosition;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public Object getMusicSource() {
        return this.url;
    }

    @Override
    public void setVolume(float f) {
        //   if (stereo && !disableAttenuation) {
        //      f = SoundMath.calculatePseudoAttenuation(position, attenuation, f);
        //  }
        // run(() -> alSourcef(source, AL_GAIN, f));
        alSourcef(source, AL_GAIN, f);
    }

    @Override
    public float getVolume() {
        //    AtomicReference<Float> val= new AtomicReference<>((float) 0);
        //   run(()-> val.set(alGetSourcef(this.source, AL_GAIN)));
        //    return val.get();
        return alGetSourcef(this.source, AL_GAIN);
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
                    AudioFormat format = AudioDataFormatTools.toAudioFormat(dataformat);
                    if (stopped)
                        return;
                    int formatId = audioFormatToOpenAl(format);
                    if (stopped)
                        return;
                    alBufferData(b, formatId, getBuffer(buffer), (int) format.getSampleRate() * (intentionallyMono ? 2 : 1));
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

    private int audioFormatToOpenAl(AudioFormat audioFormat) {

        AudioFormat.Encoding encoding = audioFormat.getEncoding();
        int i = audioFormat.getChannels();
        int j = audioFormat.getSampleSizeInBits();
        if (encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED) || encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            return getOpenALFormat(intentionallyMono ? 1 : 2, j);
        }

        throw new IllegalArgumentException("Invalid audio format: " + audioFormat);
    }

    public static int getOpenALFormat(int channel, int bit) {
        if (channel == 1) {
            if (bit == 8) {
                return AL_FORMAT_MONO8;
            }
            if (bit == 16) {
                return AL_FORMAT_MONO16;
            }
        } else if (channel == 2) {
            if (bit == 8) {
                return AL_FORMAT_STEREO8;
            }

            if (bit == 16) {
                return AL_FORMAT_STEREO16;
            }
        }
        return AL_FORMAT_MONO16;
    }
}
