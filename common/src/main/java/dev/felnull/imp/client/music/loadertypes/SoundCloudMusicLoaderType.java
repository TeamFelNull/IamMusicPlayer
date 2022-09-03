package dev.felnull.imp.client.music.loadertypes;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.music.resource.ImageInfo;
import net.minecraft.network.chat.Component;

public class SoundCloudMusicLoaderType extends AbstractLavaPlayerMusicLoaderType {
    private static final Component SC_ENTER_TEXT = Component.translatable("imp.text.enterText.soundcloud");

    public SoundCloudMusicLoaderType() {
        super(IMPMusicLoaderTypes.SOUNDCLOUD);
    }

    @Override
    public boolean isSearchable() {
        return false;
    }

    @Override
    public void registerSourceManager(AudioPlayerManager audioPlayerManager) {
        audioPlayerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
    }

    @Override
    public boolean match(AudioTrack track) {
        return track.getSourceManager() instanceof SoundCloudAudioSourceManager;
    }

    @Override
    public String getIdentifier(AudioTrack track) {
        return track.getInfo().uri;
    }

    @Override
    protected ImageInfo createThumbnail(AudioTrack track) {
        return new ImageInfo(ImageInfo.ImageType.SOUND_CLOUD_ARTWORK, track.getInfo().uri);
    }

    @Override
    public Component getEnterText() {
        return SC_ENTER_TEXT;
    }
}
