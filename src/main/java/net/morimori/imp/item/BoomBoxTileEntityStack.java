package net.morimori.imp.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class BoomBoxTileEntityStack extends TileEntityStack {
    protected ItemStack cassette = ItemStack.EMPTY;
    public int openProgress;
    public long position;

    public BoomBoxTileEntityStack(ItemStack item) {
        super(item);

    }

    public void read(CompoundNBT tag) {
        super.read(tag);
        this.cassette = ItemStack.read(tag.getCompound("CassetteItem"));
        this.openProgress = tag.getInt("OpenProgress");
        this.position = tag.getLong("Position");

    }

    public CompoundNBT write(CompoundNBT tag) {
        tag.put("CassetteItem", cassette.write(new CompoundNBT()));
        tag.putInt("OpenProgress", this.openProgress);
        tag.putLong("Position", this.position);
        return super.write(tag);
    }

}
