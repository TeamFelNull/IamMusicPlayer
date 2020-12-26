package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public abstract class IMPAbstractPAEquipmentTileEntity extends IMPAbstractEquipmentTileEntity {

    private int parabolicAntennaRotationPitch;
    private int parabolicAntennaRotationYaw;
    private int prevParabolicAntennaRotationPitch;
    private int prevParabolicAntennaRotationYaw;
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

    public boolean isPAInversionPitch() {
        return parabolicAntennaInversionPitch;
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
        tag.putInt("PrevAntennaRotationPitch", this.prevParabolicAntennaRotationPitch);
        tag.putInt("PrevAntennaRotationYaw", this.prevParabolicAntennaRotationYaw);
        tag.putBoolean("AntennaInversionPitch", this.parabolicAntennaInversionPitch);
        return tag;
    }

    @Override
    public void readByIKSG(BlockState state, CompoundNBT tag) {
        super.readByIKSG(state, tag);
        this.parabolicAntennaRotationPitch = tag.getInt("AntennaRotationPitch");
        this.parabolicAntennaRotationYaw = tag.getInt("AntennaRotationYaw");
        this.prevParabolicAntennaRotationPitch = tag.getInt("PrevAntennaRotationPitch");
        this.prevParabolicAntennaRotationYaw = tag.getInt("PrevAntennaRotationYaw");
        this.parabolicAntennaInversionPitch = tag.getBoolean("AntennaInversionPitch");
    }

    @Override
    public void tick() {
        super.tick();
        if (isPAntennaRotation()) {
            int spped = 2;

            this.parabolicAntennaRotationYaw += spped;

            int i = parabolicAntennaRotationYaw / 360;
            this.parabolicAntennaRotationYaw -= 360 * i;

            this.prevParabolicAntennaRotationYaw = parabolicAntennaRotationYaw + spped;

            int i2 = parabolicAntennaRotationYaw / 360;
            this.prevParabolicAntennaRotationYaw -= 360 * i2;

            if (!parabolicAntennaInversionPitch) {
                this.parabolicAntennaRotationPitch += spped;
                if (50 < parabolicAntennaRotationPitch) {
                    this.parabolicAntennaRotationPitch = 50;
                    this.parabolicAntennaInversionPitch = true;
                }
            } else {
                this.parabolicAntennaRotationPitch -= spped;
                if (-50 > parabolicAntennaRotationPitch) {
                    this.parabolicAntennaRotationPitch = -50;
                    this.parabolicAntennaInversionPitch = false;
                }
            }

            prevParabolicAntennaRotationPitch = parabolicAntennaRotationPitch;

            if (!parabolicAntennaInversionPitch) {
                this.prevParabolicAntennaRotationPitch += spped;
                if (50 < prevParabolicAntennaRotationPitch) {
                    this.prevParabolicAntennaRotationPitch = 50;
                }
            } else {
                this.prevParabolicAntennaRotationPitch -= spped;
                if (-50 > prevParabolicAntennaRotationPitch) {
                    this.prevParabolicAntennaRotationPitch = -50;
                }
            }

        } else {
            prevParabolicAntennaRotationPitch = parabolicAntennaRotationPitch;
            prevParabolicAntennaRotationYaw = parabolicAntennaRotationYaw;
        }
    }

    public int getPrevPARotationPitch() {
        return prevParabolicAntennaRotationPitch;
    }

    public int getPrevPARotationYaw() {
        return prevParabolicAntennaRotationYaw;
    }


}
