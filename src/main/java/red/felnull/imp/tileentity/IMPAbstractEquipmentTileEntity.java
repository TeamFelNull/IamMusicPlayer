package red.felnull.imp.tileentity;


import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import red.felnull.imp.block.IMPAbstractEquipmentBlock;

public abstract class IMPAbstractEquipmentTileEntity extends IMPAbstractTileEntity {


    protected IMPAbstractEquipmentTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public boolean isOn() {
        return getBlockState().get(IMPAbstractEquipmentBlock.ON);
    }

    @Override
    public CompoundNBT instructionFromClient(ServerPlayerEntity serverPlayerEntity, String s, CompoundNBT tag) {
        if (s.equals("power")) {
            setBlockState(getBlockState().with(IMPAbstractEquipmentBlock.ON, tag.getBoolean("on")));
        }
        return null;
    }
}
