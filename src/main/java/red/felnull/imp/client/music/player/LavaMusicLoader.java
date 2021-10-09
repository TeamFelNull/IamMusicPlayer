package red.felnull.imp.client.music.player;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import red.felnull.imp.client.music.ClientWorldMusicManager;

public class LavaMusicLoader {
    private static final AudioDataFormat COMMON_PCM_S16_LE_C2 = new Pcm16AudioDataFormat(2, 48000, 960, false);
    private final AudioSourceManager sourceManager;
    private AudioPlayerManager audioPlayerManager;

    public LavaMusicLoader(AudioSourceManager sourceManager) {
        this.sourceManager = sourceManager;
        init();
    }

    public void init() {
        audioPlayerManager = new DefaultAudioPlayerManager();
        audioPlayerManager.setFrameBufferDuration(1000);
        audioPlayerManager.setPlayerCleanupThreshold(Long.MAX_VALUE);
        audioPlayerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        audioPlayerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        audioPlayerManager.registerSourceManager(sourceManager);
    }

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    public AudioDataFormat getFormat() {
        return COMMON_PCM_S16_LE_C2;
    }

    public boolean isMono() {
        return !ClientWorldMusicManager.instance().isStereoEnabled();
    }
}
