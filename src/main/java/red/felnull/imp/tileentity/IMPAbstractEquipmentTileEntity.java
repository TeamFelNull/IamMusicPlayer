package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import red.felnull.imp.block.IMPAbstractEquipmentBlock;
import red.felnull.otyacraftengine.tileentity.IkisugiLockableTileEntity;
import red.felnull.otyacraftengine.util.IKSGNBTUtil;

public abstract class IMPAbstractEquipmentTileEntity extends IkisugiLockableTileEntity implements ITickableTileEntity {
    private int antennaRotationPitch;//縦方向最大90度
    private int antennaRotationYaw;//横方向最大360度
    private boolean antennaInversionPitch;

    protected IMPAbstractEquipmentTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    public abstract NonNullList<ItemStack> getItems();

    @Override
    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.getItems().get(index);
    }


    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(getItems(), index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(getItems(), index);
    }

    @Override
    public int getSizeInventory() {
        return getItems().size();
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = getItems().get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        getItems().set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        if (flag)
            this.markDirty();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean canInteractWith(ServerPlayerEntity serverPlayerEntity, String s, CompoundNBT compoundNBT) {
        return this.isUsableByPlayer(serverPlayerEntity);
    }

    public boolean isOn() {
        return getBlockState().get(IMPAbstractEquipmentBlock.ON);
    }

    @Override
    public void clear() {
        getItems().clear();
    }

    @Override
    public void tick() {
        if (isAntennaRotation()) {
            this.antennaRotationYaw += 2;
            while (this.antennaRotationYaw > 360) {
                this.antennaRotationYaw -= 360;
            }
            if (!antennaInversionPitch) {
                if (50 >= antennaRotationPitch) {
                    this.antennaRotationPitch += 2;
                } else {
                    this.antennaInversionPitch = true;
                }
            } else {
                if (-50 <= antennaRotationPitch) {
                    this.antennaRotationPitch -= 2;
                } else {
                    this.antennaInversionPitch = false;
                }
            }
        }
        this.syncble(this);
    }


    @Override
    public void readByIKSG(BlockState state, CompoundNBT tag) {
        super.readByIKSG(state, tag);
        IKSGNBTUtil.loadAllItemsByIKSG(tag, getItems());
        this.antennaRotationPitch = tag.getInt("AntennaRotationPitch");
        this.antennaRotationYaw = tag.getInt("AntennaRotationYaw");
        this.antennaInversionPitch = tag.getBoolean("AntennaInversionPitch");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        IKSGNBTUtil.saveAllItemsByIKSG(tag, getItems());
        tag.putInt("AntennaRotationPitch", this.antennaRotationPitch);
        tag.putInt("AntennaRotationYaw", this.antennaRotationYaw);
        tag.putBoolean("AntennaInversionPitch", this.antennaInversionPitch);
        return tag;
    }

    protected boolean isAntennaRotation() {
        return isOn();
    }

    protected int getAntennaIndex() {
        return 0;
    }

    public ItemStack getAntenna() {
        return getStackInSlot(getAntennaIndex());
    }

    public int getAntennaRotationPitch() {
        return antennaRotationPitch;
    }

    public int getAntennaRotationYaw() {
        return antennaRotationYaw;
    }
}
