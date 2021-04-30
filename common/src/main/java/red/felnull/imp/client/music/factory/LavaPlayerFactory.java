package red.felnull.imp.client.music.factory;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.player.LavaMusicPlayer;

import java.util.Arrays;

public class LavaPlayerFactory implements IMusicPlayerFactory {
    public static final AudioDataFormat COMMON_PCM_S16_LE_C1 = new Pcm16AudioDataFormat(1, 44100, 960, false);
    private final AudioSourceManager[] sourceManagers;
    private AudioPlayerManager audioPlayerManager;
    private AudioDataFormat dataformat;

    public LavaPlayerFactory(AudioSourceManager... sourceManagers) {
        this.sourceManagers = sourceManagers;
    }

    @Override
    public void init() {
        audioPlayerManager = new DefaultAudioPlayerManager();
        dataformat = COMMON_PCM_S16_LE_C1;
        audioPlayerManager.setFrameBufferDuration(1000);
        audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);
        audioPlayerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        audioPlayerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        audioPlayerManager.getConfiguration().setOutputFormat(dataformat);
        Arrays.stream(sourceManagers).forEach(n -> audioPlayerManager.registerSourceManager(n));
    }

    @Override
    public IMusicPlayer createMusicPlayer(String identifier) {
        return new LavaMusicPlayer(identifier, audioPlayerManager,dataformat);
    }
}
