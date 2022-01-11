package dev.felnull.imp.client.music.loadertypes;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.music.resource.ImageInfo;

public class YoutubeMusicLoaderType extends AbstractLavaPlayerMusicLoaderType {

    public YoutubeMusicLoaderType() {
        super(IMPMusicLoaderTypes.YOUTUBE);
    }

    @Override
    public boolean isSearchable() {
        return true;
    }

    @Override
    public void registerSourceManager(AudioPlayerManager audioPlayerManager) {
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
    }

    @Override
    protected ImageInfo createThumbnail(AudioTrack track) {
        return new ImageInfo(ImageInfo.ImageType.YOUTUBE_THUMBNAIL, track.getIdentifier());
    }

    @Override
    public boolean match(AudioTrack track) {
        return track.getSourceManager() instanceof YoutubeAudioSourceManager;
    }
}
