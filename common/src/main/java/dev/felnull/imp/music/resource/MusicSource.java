package dev.felnull.imp.music.resource;

import dev.felnull.otyacraftengine.data.ITAGSerializable;
import net.minecraft.nbt.CompoundTag;

public class MusicSource implements ITAGSerializable {
    public static final MusicSource EMPTY = new MusicSource("", "", 0);
    private String loaderType;
    private String identifier;
    private long duration;

    public MusicSource(String loaderType, String identifier, long duration) {
        this.loaderType = loaderType;
        this.identifier = identifier;
        this.duration = duration;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("LoaderType", loaderType);
        tag.putString("Identifier", identifier);
        tag.putLong("Duration", duration);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.loaderType = tag.getString("LoaderType");
        this.identifier = tag.getString("Identifier");
        this.duration = tag.getLong("Duration");
    }

    public long getDuration() {
        return duration;
    }

    public String getLoaderType() {
        return loaderType;
    }

    public String getIdentifier() {
        return identifier;
    }
}
