package dev.felnull.imp.client.music.loader;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.client.music.player.LavaALMusicPlayer;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.music.resource.MusicSource;

import java.util.Arrays;
import java.util.Optional;

public class LavaPlayerMusicLoader implements IMusicLoader {
    private static final AudioDataFormat COMMON_PCM_S16_LE_C2 = new Pcm16AudioDataFormat(2, 48000, 960, false);
    private final String loaderType;
    private final AudioPlayerManager audioPlayerManager;

    public LavaPlayerMusicLoader(String loaderType, AudioSourceManager... sourceManagers) {
        this.loaderType = loaderType;
        this.audioPlayerManager = LavaPlayerUtil.createAudioPlayerManager();
        audioPlayerManager.getConfiguration().setOutputFormat(COMMON_PCM_S16_LE_C2);
        Arrays.stream(sourceManagers).forEach(audioPlayerManager::registerSourceManager);
    }

    @Override
    public IMusicPlayer createMusicPlayer(MusicSource source) {
        return new LavaALMusicPlayer(source, audioPlayerManager, COMMON_PCM_S16_LE_C2, true);
    }

    @Override
    public boolean canLoad(MusicSource source) throws Exception {
        if (!loaderType.equals(source.getLoaderType()))
            return false;

        Optional<AudioTrack> track = LavaPlayerUtil.loadTrack(audioPlayerManager, source.getIdentifier());
        return track.isPresent() && !track.get().getInfo().isStream;
    }
}
