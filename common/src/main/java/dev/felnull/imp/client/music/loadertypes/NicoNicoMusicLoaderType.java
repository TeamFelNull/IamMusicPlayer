package dev.felnull.imp.client.music.loadertypes;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.nico.NicoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.minecraft.resources.ResourceLocation;

public class NicoNicoMusicLoaderType extends AbstractLavaPlayerMusicLoaderType {
    public NicoNicoMusicLoaderType() {
        super("");
        //super(IMPMusicLoaderTypes.NICONICO);
    }

    @Override
    public void registerSourceManager(AudioPlayerManager audioPlayerManager) {
        audioPlayerManager.registerSourceManager(new NicoAudioSourceManager("", ""));
    }

    @Override
    public boolean match(AudioTrack track) {
        return track.getSourceManager() instanceof NicoAudioSourceManager;
    }

    @Override
    public boolean isSearchable() {
        return false;
    }

    @Override
    public ResourceLocation getIcon() {
        return null;
    }

    @Override
    public String getIdentifier(AudioTrack track) {
        return track.getInfo().uri;
    }
}
