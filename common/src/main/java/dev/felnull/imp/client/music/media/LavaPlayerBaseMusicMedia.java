package dev.felnull.imp.client.music.media;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.lava.LavaPlayerManager;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public abstract class LavaPlayerBaseMusicMedia implements MusicMedia {
    private static final Component ENTER_TEXT = new TranslatableComponent("imp.text.enterText.default");
    private final String name;
    private final Component componentName;
    private final ResourceLocation icon;

    protected LavaPlayerBaseMusicMedia(String name) {
        this.name = name;
        this.componentName =new TranslatableComponent("imp.loaderType." + name);
        var il = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/loader_types/" + name + ".png");
        this.icon = il;
    }

    abstract public void registerSourceManager(AudioPlayerManager audioPlayerManager);

    @Override
    public Component getMediaName() {
        return componentName;
    }

    @Override
    public Component getEnterText() {
        return ENTER_TEXT;
    }

    @Override
    public ResourceLocation getIcon() {
        return icon;
    }

    @Override
    public MusicMediaResult load(String sourceName) throws Exception {
        var lm = LavaPlayerManager.getInstance();

        var otrack = lm.loadTrack(sourceName);
        if (otrack.isPresent() && !match(otrack.get()))
            return null;

        if (otrack.isPresent() && !otrack.get().getInfo().isStream)
            return createResult(otrack.get());
        return null;
    }

    public MusicMediaResult createResult(AudioTrack track) {
        var ms = new MusicSource(name, getIdentifier(track), track.getDuration());
        return new MusicMediaResult(ms, createThumbnail(track), track.getInfo().title, track.getInfo().author);
    }

    protected ImageInfo createThumbnail(AudioTrack track) {
        return null;
    }

    protected String getIdentifier(AudioTrack track) {
        return track.getIdentifier();
    }

    abstract public boolean match(AudioTrack track);

    public int priority() {
        return 0;
    }
}
