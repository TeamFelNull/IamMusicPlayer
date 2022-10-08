package dev.felnull.imp.client.nmusic.media;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.otyacraftengine.client.gui.TextureSpecify;
import net.minecraft.network.chat.Component;

public class HttpMusicMedia extends LavaPlayerBaseMusicMedia {
    private static final Component URL_ENTER_TEXT = Component.translatable("imp.text.enterText.url");

    protected HttpMusicMedia(String name) {
        super(name);
    }

    @Override
    public void registerSourceManager(AudioPlayerManager audioPlayerManager) {
        audioPlayerManager.registerSourceManager(new HttpAudioSourceManager());
    }

    @Override
    public boolean isSearchable() {
        return false;
    }

    @Override
    public TextureSpecify getIcon() {
        return null;
    }

    @Override
    public boolean match(AudioTrack track) {
        return track.getSourceManager() instanceof HttpAudioSourceManager;
    }

    @Override
    public Component getEnterText() {
        return URL_ENTER_TEXT;
    }
}
