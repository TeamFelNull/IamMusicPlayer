package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public abstract class IMPAbstractPAEquipmentTileEntity extends IMPAbstractEquipmentTileEntity {

    private int parabolicAntennaRotationPitch;
    private int parabolicAntennaRotationYaw;
    private boolean parabolicAntennaInversionPitch;

    protected IMPAbstractPAEquipmentTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }


    public int getPARotationPitch() {
        return parabolicAntennaRotationPitch;
    }

    public int getPARotationYaw() {
        return parabolicAntennaRotationYaw;
    }

    protected boolean isPAntennaRotation() {
        return isOn();
    }

    protected int getPAntennaIndex() {
        return 0;
    }

    public ItemStack getPAntenna() {
        return getStackInSlot(getPAntennaIndex());
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.putInt("AntennaRotationPitch", this.parabolicAntennaRotationPitch);
        tag.putInt("AntennaRotationYaw", this.parabolicAntennaRotationYaw);
        tag.putBoolean("AntennaInversionPitch", this.parabolicAntennaInversionPitch);
        return tag;
    }

    @Override
    public void readByIKSG(BlockState state, CompoundNBT tag) {
        super.readByIKSG(state, tag);
        this.parabolicAntennaRotationPitch = tag.getInt("AntennaRotationPitch");
        this.parabolicAntennaRotationYaw = tag.getInt("AntennaRotationYaw");
        this.parabolicAntennaInversionPitch = tag.getBoolean("AntennaInversionPitch");
    }

    @Override
    public void tick() {
        super.tick();
        if (isPAntennaRotation()) {
            this.parabolicAntennaRotationYaw += 2;
            while (this.parabolicAntennaRotationYaw > 360) {
                this.parabolicAntennaRotationYaw -= 360;
            }
            if (!parabolicAntennaInversionPitch) {
                if (50 >= parabolicAntennaRotationPitch) {
                    this.parabolicAntennaRotationPitch += 2;
                } else {
                    this.parabolicAntennaInversionPitch = true;
                }
            } else {
                if (-50 <= parabolicAntennaRotationPitch) {
                    this.parabolicAntennaRotationPitch -= 2;
                } else {
                    this.parabolicAntennaInversionPitch = false;
                }
            }
        }
    }
}
