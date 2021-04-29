package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import red.felnull.otyacraftengine.data.ITAGSerializable;

public class MusicLocation implements ITAGSerializable {
    private ResourceLocation factoryName;
    private String identifier;

    public MusicLocation(CompoundTag tag) {
        this.load(tag);
    }

    public MusicLocation(ResourceLocation factory, String identifier) {
        this.factoryName = factory;
        this.identifier = identifier;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("FactoryName", factoryName.toString());
        tag.putString("Identifier", this.identifier);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.factoryName = new ResourceLocation(tag.getString("FactoryName"));
        this.identifier = tag.getString("Identifier");
    }
}
