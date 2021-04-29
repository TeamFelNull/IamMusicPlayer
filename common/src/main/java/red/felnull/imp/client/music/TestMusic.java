package red.felnull.imp.client.music;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import org.lwjgl.BufferUtils;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;

import static org.lwjgl.openal.AL10.*;

public class TestMusic {

    private static boolean inited;
    private static AudioPlayerManager audioPlayerManager;
    private static AudioDataFormat dataformat;

    private static void init() {
        if (inited)
            return;
        inited = true;
        audioPlayerManager = new DefaultAudioPlayerManager();
        dataformat = StandardAudioDataFormats.COMMON_PCM_S16_LE;
        audioPlayerManager.setFrameBufferDuration(1000);
        audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);
        audioPlayerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        audioPlayerManager.getConfiguration().setOpusEncodingQuality(10);
        audioPlayerManager.getConfiguration().setOutputFormat(dataformat);
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
    }

    public static void playStart(String indefirer) {
        init();
        AudioPlayer player = audioPlayerManager.createPlayer();
        audioPlayerManager.loadItem(indefirer, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                player.startTrack(track, false);
                System.out.println("test1");
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                System.out.println("test2");
            }

            @Override
            public void noMatches() {
                System.out.println("test3");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                System.out.println("test4");
            }
        });
        AudioInputStream stream = AudioPlayerInputStream.createStream(player, dataformat, dataformat.frameDuration(), false);
        PlayThread pt = new PlayThread(stream, dataformat, player);
        pt.start();
    }

    public static class PlayThread extends Thread {
        private final AudioInputStream audioInputStream;
        private final AudioDataFormat audioDataFormat;
        private final AudioPlayer player;

        public PlayThread(AudioInputStream stream, AudioDataFormat audioDataFormat, AudioPlayer player) {
            this.audioInputStream = stream;
            this.audioDataFormat = audioDataFormat;
            this.player = player;
        }

        @Override
        public void run() {
            int source = alGenSources();
            ByteBuffer pcm = BufferUtils.createByteBuffer(11025);
            float ang = 0;

            long frameDuration = audioDataFormat.frameDuration();

            while (true) {
                if (player.getPlayingTrack() != null) {
                    break;
                }

                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            message(player.getPlayingTrack().getDuration() + "ms");
            message(player.getPlayingTrack().getInfo().identifier);
            message(player.getPlayingTrack().getInfo().title);
            message(player.getPlayingTrack().getInfo().author);
            message("isStream: " + player.getPlayingTrack().getInfo().isStream);

            boolean played = false;

            try {
                //  final byte[] buffer = new byte[audioDataFormat.chunkSampleCount * audioDataFormat.channelCount * 2];
                int chunkSize;
                //   AudioInputStream testStream = AudioSystem.getAudioInputStream(new File("D:\\pcdatas\\music\\playlist\\カオス鳥.wav"));
                AudioFormat format = audioInputStream.getFormat();// AudioDataFormatTools.toAudioFormat(audioDataFormat);
                //  System.out.println(testStream.getFormat());
                System.out.println(audioInputStream.getFormat());

                //  AudioFormat format = testStream.getFormat();
                SourceDataLine souceLine = getLine(format);
                byte[] buffer = new byte[1024];
                while (true) {
                    if (!player.isPaused()) {
                        if ((chunkSize = audioInputStream.read(buffer)) >= 0) {
                            // souceLine.write(buffer, 0, chunkSize);
                            int b = alGenBuffers();
                            ang = fillBuffer(ang, pcm);
                            //   System.out.println(ang);
                            // System.out.println(b + ":" + getFormat(format) + ":" + getBuffer(buffer) + ":" + (int) format.getSampleRate());
                            alBufferData(b, getFormat(format), getBuffer(buffer), (int) format.getSampleRate());
                            alSourceQueueBuffers(source, b);
                            //     souceLine.write(buffer, 0, chunkSize);

                            if (!played) {
                                alSourcePlay(source);
                                played = true;
                            }
                        }
                    } else {
                        sleep(frameDuration);
                    }
                    int state = alGetSourcei(source, AL_SOURCE_STATE);
                    if (state == AL_STOPPED)
                        return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void closeLine(SourceDataLine sourceLine) {
            if (sourceLine != null) {
                sourceLine.flush();
                sourceLine.stop();
                sourceLine.close();
            }
        }

        public SourceDataLine getLine(AudioFormat format) throws Exception {
            //  AudioFormat format = AudioDataFormatTools.toAudioFormat(audioDataFormat);
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
            Mixer mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo()[0]);
            SourceDataLine line = (SourceDataLine) mixer.getLine(speakerInfo);
            AudioDataFormat dataFormat = audioDataFormat;
            line.open(format, 1024 * 5);
            line.start();
            return line;
        }
    }

    private static int trig = 0;

    public static float fillBuffer(float ang, ByteBuffer buff) {
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

    public static int getFormat(AudioFormat format) {
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
        throw new RuntimeException("can't handle format");
    }

    public static ByteBuffer getBuffer(byte[] array) {
        ByteBuffer audioBuffer2 = BufferUtils.createByteBuffer(array.length);
        audioBuffer2.put(array);
        audioBuffer2.flip();
        return audioBuffer2;
    }

    public static void message(String message) {
        Minecraft.getInstance().player.displayClientMessage(new TextComponent(message), false);
    }
}
