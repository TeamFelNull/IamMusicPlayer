package dev.felnull.imp.client.music.loadertypes;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class HttpURLMusicLoaderType extends AbstractLavaPlayerMusicLoaderType {
    public HttpURLMusicLoaderType() {
        super(IMPMusicLoaderTypes.HTTP);

    }

    @Override
    public boolean isSearchable() {
        return false;
    }

    @Override
    public void registerSourceManager(AudioPlayerManager audioPlayerManager) {
        audioPlayerManager.registerSourceManager(new HttpAudioSourceManager());
    }

    @Override
    public boolean match(AudioTrack track) {
        return track.getSourceManager() instanceof HttpAudioSourceManager;
    }
}
