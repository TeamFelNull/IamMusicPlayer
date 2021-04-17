package red.felnull.imp.music.resource;

import net.minecraft.nbt.CompoundTag;
import red.felnull.otyacraftengine.data.ITAGSerializable;

public class MusicPlayListDetailed implements ITAGSerializable {
    private boolean anyonejoin;

    public MusicPlayListDetailed(boolean anyonejoin) {
        this.anyonejoin = anyonejoin;
    }

    public MusicPlayListDetailed(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("AnyoneJoin", anyonejoin);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        this.anyonejoin = tag.getBoolean("AnyoneJoin");
    }

    public boolean isAnyonejoin() {
        return anyonejoin;
    }
}
