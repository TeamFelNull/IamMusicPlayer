package dev.felnull.imp.music.resource;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.otyacraftengine.data.ITAGSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class MusicSource implements ITAGSerializable {
    public static final MusicSource EMPTY = new MusicSource(new ResourceLocation(IamMusicPlayer.MODID, "empty"), "", 0);
    private ResourceLocation loader;
    private String identifier;
    private long duration;

    public MusicSource(ResourceLocation loader, String identifier, long duration) {
        this.loader = loader;
        this.identifier = identifier;
        this.duration = duration;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("Loader", loader.toString());
        tag.putString("Identifier", identifier);
        tag.putLong("Duration", duration);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.loader = new ResourceLocation(tag.getString("Loader"));
        this.identifier = tag.getString("Identifier");
        this.duration = tag.getLong("Duration");
    }

    public long getDuration() {
        return duration;
    }

    public ResourceLocation getLoader() {
        return loader;
    }

    public String getIdentifier() {
        return identifier;
    }
}
