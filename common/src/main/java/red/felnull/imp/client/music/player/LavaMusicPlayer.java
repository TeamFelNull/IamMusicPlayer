package red.felnull.imp.client.music.player;

import com.mojang.math.Vector3f;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.lwjgl.BufferUtils;
import red.felnull.imp.throwable.InvalidIdentifierException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.openal.AL10.*;

public class LavaMusicPlayer implements IMusicPlayer {
    private final String identifier;
    private final AudioPlayerManager audioPlayerManager;
    private final AudioDataFormat dataformat;
    private final AudioPlayer audioPlayer;
    private AudioInputStream stream;
    private boolean trackLoaded;
    private boolean playing;
    private Exception exception;
    private int source;
    private Vector3f pos;

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
    }

    @Override
    public void play() {
        if (playing)
            return;

        source = alGenSources();
        PlayThread pt = new PlayThread();
        pt.start();
    }

    @Override
    public void stop() {

    }

    @Override
    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    @Override
    public void setPosition(long position) {
        if (audioPlayer.getPlayingTrack() != null) {
            audioPlayer.getPlayingTrack().setPosition(position);
        }
    }

    @Override
    public void clear() {

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

                //        alSourcei(source, AL_BUFFER, source);
                alSourcei(source, AL_DISTANCE_MODEL, 53251);
                alSourcei(source, AL_MAX_DISTANCE, 10);
                alSourcef(source, AL_ROLLOFF_FACTOR, 1.0F);
                alSourcef(source, AL_REFERENCE_DISTANCE, 0.0F);
                alSourcei(source, AL_LOOPING, AL_FALSE);
                alSourcefv(source, AL_POSITION, new float[]{0f, -60f, 0f});
                //   alSource3f(source, AL_POSITION, pos.x(), pos.y(), pos.z());
                alSourcef(source, AL_GAIN, 1f);
                alSourcef(source, AL_PITCH, 1f);
                alSourcei(source, AL_SOURCE_RELATIVE, AL_FALSE);


                while (true) {
                    if (stream.read(buffer) >= 0) {
                        int b = alGenBuffers();
                        ang = fillBuffer(ang, pcm);
                        alBufferData(b, getFormat(format), getBuffer(buffer), (int) format.getSampleRate());
                        alSourceQueueBuffers(source, b);

                        if (!statted) {
                            alSourcePlay(source);
                            statted = true;
                        }
                    }
                    int state = alGetSourcei(source, AL_SOURCE_STATE);
                    if (state == AL_STOPPED)
                        return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                playing = false;
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

        public int getFormat(AudioFormat format) {
            if (format.getChannels() == 1) {
                if (format.getSampleSizeInBits() == 8) {
                    return AL_FORMAT_MONO8;
                } else if (format.getSampleSizeInBits() == 16) {
                    return AL_FORMAT_MONO16;
                }
            } else if (format.getChannels() == 2) {
                if (format.getSampleSizeInBits() == 8) {
                    return AL_FORMAT_STEREO8;
                } else if (format.getSampleSizeInBits() == 16) {
                    return AL_FORMAT_STEREO16;
                }
            }
            return 0;
        }

        public ByteBuffer getBuffer(byte[] array) {
            ByteBuffer audioBuffer2 = BufferUtils.createByteBuffer(array.length);
            audioBuffer2.put(array);
            audioBuffer2.flip();
            return audioBuffer2;
        }

    }
}
