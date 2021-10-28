package dev.felnull.imp.client.music.player;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.client.util.YoutubeDownloaderUtil;
import dev.felnull.imp.music.resource.MusicSource;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class YoutubeDownloaderMusicPlayer extends LavaALMusicPlayer {
    public YoutubeDownloaderMusicPlayer(MusicSource musicSource, AudioPlayerManager audioPlayerManager, AudioDataFormat audioFormat, boolean spatial) {
        super(musicSource, audioPlayerManager, audioFormat, spatial);
    }

    @Override
    protected Optional<AudioTrack> createTrack() throws ExecutionException, InterruptedException {
        return LavaPlayerUtil.loadCashedTrack(musicSource.getLoaderType(), audioPlayerManager, YoutubeDownloaderUtil.getCashedYoutubeRawURL(musicSource.getIdentifier(), true), true);
    }
}
