package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.data.ITAGSerializable;

public class MusicLocation implements ITAGSerializable {
    public static final MusicLocation EMPTY = new MusicLocation(new ResourceLocation(IamMusicPlayer.MODID, "empty"), "");
    private ResourceLocation loaderName;
    private String identifier;

    public MusicLocation(CompoundTag tag) {
        this.load(tag);
    }

    public MusicLocation(ResourceLocation loader, String identifier) {
        this.loaderName = loader;
        this.identifier = identifier;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("LoaderName", loaderName.toString());
        tag.putString("Identifier", this.identifier);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.loaderName = new ResourceLocation(tag.getString("LoaderName"));
        this.identifier = tag.getString("Identifier");
    }

    public String getIdentifier() {
        return identifier;
    }

    public ResourceLocation getLoaderName() {
        return loaderName;
    }
}
