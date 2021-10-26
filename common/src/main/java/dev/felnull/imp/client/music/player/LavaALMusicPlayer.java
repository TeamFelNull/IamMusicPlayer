package dev.felnull.imp.client.music.player;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormatTools;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.client.util.SoundMath;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class LavaALMusicPlayer implements IMusicPlayer {
    private static final Logger LOGGER = LogManager.getLogger(LavaALMusicPlayer.class);
    private static final Minecraft mc = Minecraft.getInstance();
    private final MusicSource musicSource;
    private final int source;
    private final AudioPlayerManager audioPlayerManager;
    private final AudioDataFormat audioFormat;
    private final AudioPlayer audioPlayer;
    private final boolean spatial;
    private boolean stereo;
    protected AudioInputStream stream;
    private ByteBuffer pcm = BufferUtils.createByteBuffer(11025);
    private byte[] buffer = new byte[1024 * 3];
    private final List<Integer> buffers = new ArrayList<>();
    private float ang;
    private int trig;
    private boolean loaded;
    private Vec3 pos = Vec3.ZERO;
    private boolean fixed;
    private float range;
    private long startTime;
    protected long startPosition;
    private long lastPausedTime;
    private long pausedTime;
    private boolean firstStart;
    private float noSpatialVolume;
    private LavaLoadThread loadThread;
    private long lastLavaLoad;

    public LavaALMusicPlayer(MusicSource musicSource, AudioPlayerManager audioPlayerManager, AudioDataFormat audioFormat, boolean spatial) {
        this.musicSource = musicSource;
        this.source = AL11.alGenSources();
        this.audioPlayerManager = audioPlayerManager;
        this.audioFormat = audioFormat;
        this.audioPlayer = audioPlayerManager.createPlayer();
        this.spatial = spatial;
    }

    @Override
    public void load(long position) throws Exception {
        startPosition = position;
        var track = LavaPlayerUtil.loadCashedTrack(musicSource.getLoaderType(), audioPlayerManager, musicSource.getIdentifier(), true);
        if (track.isEmpty())
            throw new IllegalStateException("Could not load");

        track.get().setPosition(position);
        audioPlayer.startTrack(track.get(), false);

        stream = AudioPlayerInputStream.createStream(audioPlayer, audioFormat, audioFormat.frameDuration(), false);

        stereo = AudioDataFormatTools.toAudioFormat(audioFormat).getChannels() >= 2;
        AL11.alSourcef(source, AL11.AL_PITCH, 1f);
        AL11.alSourcei(source, AL11.AL_SOURCE_RELATIVE, AL11.AL_FALSE);
        AL11.alSourcei(source, AL11.AL_LOOPING, AL11.AL_FALSE);
        checkError();

        AudioFormat format = AudioDataFormatTools.toAudioFormat(audioFormat);

        for (int i = 0; i < 500; i++) {
            try {
                if (stream.read(buffer) >= 0) {
                    lavaLoad(format);
                }
            } catch (InterruptedIOException ex) {
                return;
            }
        }

        loadThread = new LavaLoadThread();
        loadThread.start();

        loaded = true;
    }

    @Override
    public boolean isLoadSuccess() {
        return loaded;
    }

    @Override
    public void play(long delay) {
        if (!loaded)
            return;

        if (musicSource.getDuration() == 0 || musicSource.getDuration() >= startPosition) {
            startTime = System.currentTimeMillis();
            startPosition += delay;
            float secdelay = delay / 1000f;
            AL11.alSourcef(source, AL11.AL_SEC_OFFSET, secdelay);
            AL11.alSourcePlay(this.source);
            firstStart = true;
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        if (loadThread != null)
            loadThread.interrupt();

        startTime = 0;
        startPosition = 0;
        if (this.stream != null) {
            try {
                this.stream.close();
            } catch (IOException var2) {
                LOGGER.error("Failed to close audio stream", var2);
            }
            this.stream = null;
        }
        audioPlayer.destroy();

        if (loaded) {
            AL11.alSourceStop(source);
            AL11.alDeleteSources(source);
            List<Integer> bffs = new ArrayList<>(buffers);
            bffs.forEach(AL11::alDeleteBuffers);
            buffers.clear();
            //checkError();
        }
    }

    private int getPlayState() {
        return !loaded ? AL11.AL_STOPPED : AL11.alGetSourcei(this.source, AL11.AL_SOURCE_STATE);
    }

    @Override
    public void pause() {
        if (getPlayState() == AL11.AL_PLAYING) {
            lastPausedTime = System.currentTimeMillis();
            AL11.alSourcePause(this.source);
        }
    }

    @Override
    public void unpause() {
        if (getPlayState() == AL11.AL_PAUSED) {
            lastPausedTime = 0;
            AL11.alSourcePlay(this.source);
        }
    }

    @Override
    public boolean isPlaying() {
        return getPlayState() == AL11.AL_PLAYING;
    }

    @Override
    public boolean isPaused() {
        return getPlayState() == AL11.AL_PAUSED;
    }

    @Override
    public boolean isFinished() {
        return getPlayState() == AL11.AL_STOPPED;
    }

    @Override
    public void setCoordinatePosition(Vec3 vec3) {
        if (vec3 == null)
            if (mc.player != null) {
                vec3 = mc.player.position();
            } else {
                vec3 = Vec3.ZERO;
            }
        this.pos = vec3;
        AL11.alSource3f(source, AL11.AL_POSITION, (float) vec3.x, (float) vec3.y, (float) vec3.z);
    }

    @Override
    public Vec3 getCoordinatePosition() {
        return pos;
    }

    @Override
    public void setVolume(float v) {
        if (spatial)
            AL11.alSourcef(source, AL11.AL_GAIN, v);
        else
            this.noSpatialVolume = v;
    }

    @Override
    public void setRange(float r) {
        range = r;
        if (!fixed) {
            linearAttenuation(range);
        } else {
            AL11.alSourcei(this.source, AL11.AL_DISTANCE_MODEL, AL11.AL_FALSE);
        }
    }

    @Override
    public void update(MusicPlaybackInfo playbackInf) {
        if (isPaused()) {
            pausedTime += System.currentTimeMillis() - lastPausedTime;
            lastPausedTime = System.currentTimeMillis();
        }

        if (!spatial)
            AL11.alSourcef(source, AL11.AL_GAIN, SoundMath.calculatePseudoAttenuation(pos, range, noSpatialVolume));


        if (getPosition() - lastLavaLoad >= 20000 && loadThread == null) {
            lastLavaLoad = getPosition();
            loadThread = new LavaLoadThread();
            loadThread.start();
        }

    }

    @Override
    public long getPosition() {
        return firstStart ? (System.currentTimeMillis() - startTime + startPosition - pausedTime) : startPosition;
    }

    @Override
    public MusicSource getMusicSource() {
        return musicSource;
    }

    @Override
    public void setFixedSound(boolean enable) {
        boolean flg = fixed != enable;
        this.fixed = enable;
        if (flg)
            if (enable) {
                AL11.alSourcei(this.source, AL11.AL_DISTANCE_MODEL, AL11.AL_FALSE);
            } else {
                linearAttenuation(range);
            }
    }

    private void linearAttenuation(float r) {
        AL11.alSourcei(source, AL11.AL_DISTANCE_MODEL, 53251);
        AL11.alSourcef(source, AL11.AL_MAX_DISTANCE, r);
        AL11.alSourcef(source, AL11.AL_ROLLOFF_FACTOR, 1.0F);
        AL11.alSourcef(source, AL11.AL_REFERENCE_DISTANCE, 0.0F);
    }

    private static void checkError() {
        switch (AL11.alGetError()) {
            case AL11.AL_NO_ERROR:
                break;
            case AL11.AL_INVALID_NAME:
                throw new IllegalStateException("Invalid name parameter.");
            case AL11.AL_INVALID_ENUM:
                throw new IllegalStateException("Invalid enumerated parameter value.");
            case AL11.AL_INVALID_VALUE:
                throw new IllegalStateException("Invalid parameter parameter value.");
            case AL11.AL_INVALID_OPERATION:
                throw new IllegalStateException("Invalid operation.");
            case AL11.AL_OUT_OF_MEMORY:
                throw new IllegalStateException("Unable to allocate memory.");
        }
    }

    private float fillBuffer(float ang, ByteBuffer buff) {
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


    private ByteBuffer getBuffer(byte[] array) {
        ByteBuffer audioBuffer2 = BufferUtils.createByteBuffer(array.length);
        audioBuffer2.put(array);
        audioBuffer2.flip();
        return audioBuffer2;
    }

    private int audioFormatToOpenAl(AudioFormat audioFormat) {

        AudioFormat.Encoding encoding = audioFormat.getEncoding();
        int j = audioFormat.getSampleSizeInBits();
        if (encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED) || encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
            return getOpenALFormat(spatial ? 1 : 2, j);
        }

        throw new IllegalArgumentException("Invalid audio format: " + audioFormat);
    }

    private static int getOpenALFormat(int channel, int bit) {
        if (channel == 1) {
            if (bit == 8) {
                return AL11.AL_FORMAT_MONO8;
            }
            if (bit == 16) {
                return AL11.AL_FORMAT_MONO16;
            }
        } else if (channel == 2) {
            if (bit == 8) {
                return AL11.AL_FORMAT_STEREO8;
            }

            if (bit == 16) {
                return AL11.AL_FORMAT_STEREO16;
            }
        }
        return AL11.AL_FORMAT_MONO16;
    }

    private class LavaLoadThread extends Thread {
        private LavaLoadThread() {
            setName("LavaPlayer Load Thread : " + musicSource.getIdentifier());
        }

        @Override
        public void run() {
            try {
                AudioFormat format = AudioDataFormatTools.toAudioFormat(audioFormat);
                for (int i = 0; i < 500 * 6; i++) {
                    try {
                        if (stream.read(buffer) >= 0 && !isInterrupted()) {
                            lavaLoad(format);
                        } else {
                            break;
                        }
                    } catch (Exception ex) {
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            loadThread = null;
        }
    }

    private synchronized void lavaLoad(AudioFormat format) {
        int b = AL11.alGenBuffers();
        ang = fillBuffer(ang, pcm);
        int formatId = audioFormatToOpenAl(format);
        AL11.alBufferData(b, formatId, getBuffer(buffer), (int) format.getSampleRate() * (spatial ? 2 : 1));
        AL11.alSourceQueueBuffers(source, b);
        buffers.add(b);
    }
}
