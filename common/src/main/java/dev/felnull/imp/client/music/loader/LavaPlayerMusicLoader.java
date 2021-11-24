package dev.felnull.imp.client.music.loader;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.client.music.player.LavaALMusicPlayer;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.music.resource.MusicSource;

import java.util.Arrays;
import java.util.Optional;

public class LavaPlayerMusicLoader implements IMusicLoader {
    protected static final AudioDataFormat COMMON_PCM_S16_LE_C2 = new Pcm16AudioDataFormat(2, 48000, 960, false);
    protected final String loaderType;
    protected final AudioPlayerManager audioPlayerManager;

    public LavaPlayerMusicLoader(String loaderType, AudioSourceManager... sourceManagers) {
        this.loaderType = loaderType;
        this.audioPlayerManager = LavaPlayerUtil.createAudioPlayerManager();
        audioPlayerManager.getConfiguration().setOutputFormat(COMMON_PCM_S16_LE_C2);
        Arrays.stream(sourceManagers).forEach(audioPlayerManager::registerSourceManager);
    }

    @Override
    public IMusicPlayer createMusicPlayer(MusicSource source) {
        return new LavaALMusicPlayer(source, audioPlayerManager, COMMON_PCM_S16_LE_C2, isSpatial());
    }

    @Override
    public boolean canLoad(MusicSource source) throws Exception {
        if (!source.getLoaderType().isEmpty() && !loaderType.equals(source.getLoaderType()))
            return false;

        Optional<AudioTrack> track = LavaPlayerUtil.loadCashedTrack(source.getLoaderType(), audioPlayerManager, source.getIdentifier(), false);

        return track.isPresent() && source.isLive() == track.get().getInfo().isStream;
    }

    protected boolean isSpatial() {
        return IamMusicPlayer.CONFIG.spatial;
    }
}
