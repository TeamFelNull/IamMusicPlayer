package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.otyacraftengine.data.ITAGSerializable;

public class MusicPlayListDetailed implements ITAGSerializable {
    private boolean publiced;

    public MusicPlayListDetailed(boolean publiced) {
        this.publiced = publiced;
    }

    public MusicPlayListDetailed(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("Publiced", publiced);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.publiced = tag.getBoolean("Publiced");
    }

    public boolean isPubliced() {
        return publiced;
    }
}
