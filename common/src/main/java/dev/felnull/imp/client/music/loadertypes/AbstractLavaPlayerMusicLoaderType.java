package dev.felnull.imp.client.music.loadertypes;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class AbstractLavaPlayerMusicLoaderType implements IMusicLoaderType {
    private final String rawName;
    private final Component name;
    private final ResourceLocation icon;
    private final AudioPlayerManager audioPlayerManager;

    public AbstractLavaPlayerMusicLoaderType(String name) {
        this.rawName = name;
        this.name = new TranslatableComponent("imp.loaderType." + name);
        this.icon = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/loader_types/" + name + ".png");
        this.audioPlayerManager = LavaPlayerUtil.createAudioPlayerManager();
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

    abstract protected void registerSourceManager(AudioPlayerManager audioPlayerManager);

    @Override
    public MusicLoadResult load(String sourceName) throws InterruptedException {
        try {
            var otrack = LavaPlayerUtil.loadTrack(getAudioPlayerManager(), sourceName);
            if (otrack.isPresent()) {
                var track = otrack.get();
                var ms = new MusicSource(rawName, track.getIdentifier(), track.getDuration());
                return new MusicLoadResult(ms, createThumbnail(track), track.getInfo().title, createInfos(track));
            }
        } catch (ExecutionException ignored) {
        }
        return null;
    }

    protected ImageInfo createThumbnail(AudioTrack track) {
        return null;
    }

    private List<Component> createInfos(AudioTrack track) {
        List<Component> infos = new ArrayList<>();
        infos.add(new TextComponent("test"));
        return infos;
    }
}
