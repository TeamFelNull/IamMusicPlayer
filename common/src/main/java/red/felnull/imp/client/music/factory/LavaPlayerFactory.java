package red.felnull.imp.client.music.factory;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.player.LavaMusicPlayer;

import java.util.Arrays;

public class LavaPlayerFactory implements IMusicPlayerFactory {
    private final AudioSourceManager[] sourceManagers;
    private AudioPlayerManager audioPlayerManager;
    private AudioDataFormat dataformat;

    public LavaPlayerFactory(AudioSourceManager... sourceManagers) {
        this.sourceManagers = sourceManagers;
    }

    @Override
    public void init() {
        audioPlayerManager = new DefaultAudioPlayerManager();
        dataformat = StandardAudioDataFormats.COMMON_PCM_S16_LE;
        audioPlayerManager.setFrameBufferDuration(1000);
        audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);
        audioPlayerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        audioPlayerManager.getConfiguration().setOpusEncodingQuality(10);
        audioPlayerManager.getConfiguration().setOutputFormat(dataformat);
        Arrays.stream(sourceManagers).forEach(n -> audioPlayerManager.registerSourceManager(n));
    }

    @Override
    public IMusicPlayer createMusicPlayer(String identifier) {
        return new LavaMusicPlayer(identifier, audioPlayerManager,dataformat);
    }
}
