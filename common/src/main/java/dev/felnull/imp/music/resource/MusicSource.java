package dev.felnull.imp.music.resource;

import dev.felnull.otyacraftengine.data.ITAGSerializable;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public class MusicSource implements ITAGSerializable {
    public static final MusicSource EMPTY = new MusicSource("", "", 0);
    private String loaderType;
    private String identifier;
    private long duration;

    public MusicSource() {

    }

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

    public boolean isLive() {
        return duration < 0;
    }

    public boolean isEmpty() {
        return this == EMPTY || (loaderType.isEmpty() && identifier.isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicSource that = (MusicSource) o;
        return duration == that.duration && Objects.equals(loaderType, that.loaderType) && Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loaderType, identifier, duration);
    }

    @Override
    public String toString() {
        return "MusicSource{" +
                "loaderType='" + loaderType + '\'' +
                ", identifier='" + identifier + '\'' +
                ", duration=" + duration +
                '}';
    }
}
