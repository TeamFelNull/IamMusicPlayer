package dev.felnull.imp.client.music.loadertypes;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;

public class HttpURLMusicLoaderType extends AbstractLavaPlayerMusicLoaderType {
    public HttpURLMusicLoaderType() {
        super(IMPMusicLoaderTypes.HTTP);
    }

    @Override
    public boolean isSearchable() {
        return false;
    }

    @Override
    protected void registerSourceManager(AudioPlayerManager audioPlayerManager) {
        audioPlayerManager.registerSourceManager(new HttpAudioSourceManager());
    }
}
