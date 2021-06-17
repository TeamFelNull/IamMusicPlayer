package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.data.ITAGSerializable;

public class MusicSource implements ITAGSerializable {
    public static final MusicSource EMPTY = new MusicSource(new ResourceLocation(IamMusicPlayer.MODID, "empty"), "", 0);
    private ResourceLocation loaderName;
    private String identifier;
    private long duration;

    public MusicSource(CompoundTag tag) {
        this.load(tag);
    }

    public MusicSource(ResourceLocation loader, String identifier, long duration) {
        this.loaderName = loader;
        this.identifier = identifier;
        this.duration = duration;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("LoaderName", loaderName.toString());
        tag.putString("Identifier", this.identifier);
        tag.putLong("Duration", duration);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.loaderName = new ResourceLocation(tag.getString("LoaderName"));
        this.identifier = tag.getString("Identifier");
        this.duration = tag.getLong("Duration");
    }

    public String getIdentifier() {
        return identifier;
    }

    public ResourceLocation getLoaderName() {
        return loaderName;
    }

    public long getDuration() {
        return duration;
    }

}
