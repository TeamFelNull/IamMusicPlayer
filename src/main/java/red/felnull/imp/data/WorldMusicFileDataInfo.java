package red.felnull.imp.data;

import net.minecraft.nbt.CompoundNBT;
import red.felnull.otyacraftengine.data.INBTReadWriter;

public class WorldMusicFileDataInfo implements INBTReadWriter {
    private long duration;
    private long byteSize;
    private float frameParSec;
    private boolean isError;

    public WorldMusicFileDataInfo(long duration, long byteSize, float frameParSec) {
        this.duration = duration;
        this.byteSize = byteSize;
        this.frameParSec = frameParSec;
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
        this.frameParSec = tag.getFloat("FrameParSec");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putLong("Duration", this.duration);
        tag.putLong("ByteSize", this.byteSize);
        tag.putFloat("FrameParSec", this.frameParSec);
        return tag;
    }

    public long getByteSize() {
        return byteSize;
    }

    public long getDuration() {
        return duration;
    }

    public float getFrameParSecond() {
        return frameParSec;
    }

    public boolean isError() {
        return isError;
    }
}