package dev.felnull.imp.client.music.loader;

import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.client.music.player.YoutubeDownloaderMusicPlayer;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.client.util.YoutubeDownloaderUtil;
import dev.felnull.imp.music.resource.MusicSource;

import java.util.Optional;

public class YoutubeDownloaderMusicLoader extends LavaPlayerMusicLoader {
    public YoutubeDownloaderMusicLoader() {
        super("http", new HttpAudioSourceManager());
    }

    @Override
    public IMusicPlayer createMusicPlayer(MusicSource source) {
        return new YoutubeDownloaderMusicPlayer(source, audioPlayerManager, COMMON_PCM_S16_LE_C2, isSpatial());
    }

    @Override
    public boolean canLoad(MusicSource source) throws Exception {
        if (!"youtube".equals(source.getLoaderType()))
            return false;

        var url = YoutubeDownloaderUtil.getCashedYoutubeRawURL(source.getIdentifier(), false);
        if (url == null)
            return false;

        Optional<AudioTrack> track = LavaPlayerUtil.loadCashedTrack(source.getLoaderType(), audioPlayerManager, url, false);
        return track.isPresent() && !track.get().getInfo().isStream;
    }
}
