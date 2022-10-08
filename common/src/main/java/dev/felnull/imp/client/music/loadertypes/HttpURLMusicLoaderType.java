package dev.felnull.imp.client.music.loadertypes;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HttpURLMusicLoaderType extends AbstractLavaPlayerMusicLoaderType {
    private static final Component URL_ENTER_TEXT = Component.translatable("imp.text.enterText.url");

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

    @Override
    public Component getEnterText() {
        return URL_ENTER_TEXT;
    }

    @Override
    public ResourceLocation getIcon() {
        return null;
    }
}
