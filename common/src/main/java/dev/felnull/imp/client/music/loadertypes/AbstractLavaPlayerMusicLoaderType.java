package dev.felnull.imp.client.music.loadertypes;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.music.loader.LavaPlayerMusicLoader;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.ExecutionException;

public abstract class AbstractLavaPlayerMusicLoaderType implements IMusicLoaderType {
    private static final Component ENTER_TEXT = new TranslatableComponent("imp.text.enterText.default");
    private final String rawName;
    private final Component name;
    private final ResourceLocation icon;
    private final AudioPlayerManager audioPlayerManager;

    public AbstractLavaPlayerMusicLoaderType(String name) {
        this.rawName = name;
        this.name = new TranslatableComponent("imp.loaderType." + name);
        this.icon = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/loader_types/" + name + ".png");
        this.audioPlayerManager = LavaPlayerUtil.createAudioPlayerManager();
        audioPlayerManager.getConfiguration().setOutputFormat(LavaPlayerMusicLoader.COMMON_PCM_S16_LE_C2);
        registerSourceManager(this.audioPlayerManager);
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public ResourceLocation getIcon() {
        return icon;
    }

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    abstract public void registerSourceManager(AudioPlayerManager audioPlayerManager);

    @Override
    public MusicLoadResult load(String sourceName) throws InterruptedException {
        try {
            var otrack = LavaPlayerUtil.loadTrack(getAudioPlayerManager(), sourceName);
            if (otrack.isPresent() && !otrack.get().getInfo().isStream)
                return createResult(otrack.get());
        } catch (ExecutionException ignored) {
        }
        return null;
    }

    public MusicLoadResult createResult(AudioTrack track) {
        var ms = new MusicSource(rawName, getIdentifier(track), track.getDuration());
        return new MusicLoadResult(ms, createThumbnail(track), track.getInfo().title, track.getInfo().author);
    }

    protected ImageInfo createThumbnail(AudioTrack track) {
        return null;
    }

    abstract public boolean match(AudioTrack track);

    public String getIdentifier(AudioTrack track) {
        return track.getIdentifier();
    }

    public String getRawName() {
        return rawName;
    }

    @Override
    public Component getEnterText() {
        return ENTER_TEXT;
    }
}
