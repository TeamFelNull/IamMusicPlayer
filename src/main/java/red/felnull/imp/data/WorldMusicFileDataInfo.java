package red.felnull.imp.data;

import net.minecraft.nbt.CompoundNBT;
import red.felnull.otyacraftengine.data.INBTReadWriter;

public class WorldMusicFileDataInfo implements INBTReadWriter {
    private long duration;
    private int byteSize;

    public WorldMusicFileDataInfo(long duration, int byteSize) {
        this.duration = duration;
        this.byteSize = byteSize;
    }

    @Override
    public void read(CompoundNBT tag) {
        this.duration = tag.getLong("Duration");
        this.byteSize = tag.getInt("ByteSize");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putLong("Duration", this.duration);
        tag.putInt("ByteSize", this.byteSize);
        return tag;
    }

    public int getByteSize() {
        return byteSize;
    }

    public long getDuration() {
        return duration;
    }
}