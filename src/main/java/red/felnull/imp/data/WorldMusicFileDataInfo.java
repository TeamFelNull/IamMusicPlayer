package red.felnull.imp.data;

import net.minecraft.nbt.CompoundNBT;
import red.felnull.otyacraftengine.data.INBTReadWriter;

public class WorldMusicFileDataInfo implements INBTReadWriter {
    private long duration;
    private long byteSize;
    private boolean isError;

    public WorldMusicFileDataInfo(long duration, long byteSize) {
        this.duration = duration;
        this.byteSize = byteSize;
    }

    public WorldMusicFileDataInfo(boolean error) {
        this.isError = error;
    }

    public WorldMusicFileDataInfo(CompoundNBT tag) {
        read(tag);
    }

    @Override
    public void read(CompoundNBT tag) {
        this.duration = tag.getLong("Duration");
        this.byteSize = tag.getLong("ByteSize");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putLong("Duration", this.duration);
        tag.putLong("ByteSize", this.byteSize);
        return tag;
    }

    public long getByteSize() {
        return byteSize;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isError() {
        return isError;
    }
}