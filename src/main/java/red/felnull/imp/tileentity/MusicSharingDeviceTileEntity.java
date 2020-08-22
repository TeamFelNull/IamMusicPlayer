package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.block.MusicSharingDeviceBlock;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.imp.util.ItemHelper;
import red.felnull.otyacraftengine.tileentity.IkisugiLockableTileEntity;
import red.felnull.otyacraftengine.util.IKSGNBTUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicSharingDeviceTileEntity extends IkisugiLockableTileEntity implements ITickableTileEntity {
    protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public int rotationPitch;//縦方向最大90度
    public int rotationYaw;//横方向最大360度
    private boolean inversionPitch;
    private Map<String, String> plpagemodes = new HashMap<>();

    public MusicSharingDeviceTileEntity() {
        super(IMPTileEntityTypes.MUSIC_SHARING_DEVICE);
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }


    @Override
    public void readByIKSG(BlockState state, CompoundNBT tag) {
        super.readByIKSG(state, tag);
        this.rotationPitch = tag.getInt("RotationPitch");
        this.rotationYaw = tag.getInt("RotationYaw");
        this.inversionPitch = tag.getBoolean("InversionPitch");
        this.plpagemodes = IKSGNBTUtil.readStringMap(tag.getCompound("playerpagemodes"));
        loadAllItemsByIKSG(tag, items);
    }

    public static void loadAllItemsByIKSG(CompoundNBT tag, NonNullList<ItemStack> list) {
        ListNBT listnbt = tag.getList("Items", 10);
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < listnbt.size(); ++i) {
            CompoundNBT compoundnbt = listnbt.getCompound(i);
            int j = compoundnbt.getByte("Slot") & 255;
            if (j >= 0 && j < list.size()) {
                list.set(j, ItemStack.read(compoundnbt));
                ints.add(j);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (!ints.contains(i)) {
                list.set(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.putInt("RotationPitch", this.rotationPitch);
        tag.putInt("RotationYaw", this.rotationYaw);
        tag.putBoolean("InversionPitch", this.inversionPitch);
        CompoundNBT plmtag = new CompoundNBT();
        IKSGNBTUtil.writeStringMap(plmtag, this.plpagemodes);
        tag.put("playerpagemodes", plmtag);
        CompoundNBT tag2 = ItemStackHelper.saveAllItems(tag, items);
        return tag2;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.music_sharing_device");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new MusicSharingDeviceContainer(id, player, this, getPos());
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

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
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0)
            return ItemHelper.isAntenna(stack);

        return false;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
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
    public void clear() {
        getItems().clear();
    }

    @Override
    public CompoundNBT instructionFromClient(ServerPlayerEntity serverPlayerEntity, String s, CompoundNBT tag) {
        String uuid = IKSGPlayerUtil.getUUID(serverPlayerEntity);
        if (s.equals("power")) {
            setBlockState(getBlockState().with(MusicSharingDeviceBlock.ON, tag.getBoolean("on")));
        } else if (s.equals("opengui")) {
            if (!plpagemodes.containsKey(uuid)) {
                plpagemodes.put(uuid, "playlist");
            }
        }
        return null;
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if (!ItemHelper.isAntenna(getAntenna())) {
                plpagemodes.entrySet().stream().filter(n -> !n.getValue().equals("noantenna")).forEach(n -> plpagemodes.put(n.getKey(), "noantenna"));
            } else {
                plpagemodes.entrySet().stream().filter(n -> n.getValue().equals("noantenna")).forEach(n -> plpagemodes.put(n.getKey(), "playlist"));
            }

            if (isOn()) {
                this.rotationYaw += 2;
                while (this.rotationYaw > 360) {
                    this.rotationYaw -= 360;
                }
                if (!inversionPitch) {
                    if (50 >= rotationPitch) {
                        this.rotationPitch += 2;
                    } else {
                        this.inversionPitch = true;
                    }
                } else {
                    if (-50 <= rotationPitch) {
                        this.rotationPitch -= 2;
                    } else {
                        this.inversionPitch = false;
                    }
                }
            }
        }
        this.syncble(this);
    }

    @Override
    public boolean canInteractWith(ServerPlayerEntity serverPlayerEntity, String s, CompoundNBT compoundNBT) {
        return this.isUsableByPlayer(serverPlayerEntity);
    }

    public boolean isOn() {
        return getBlockState().get(MusicSharingDeviceBlock.ON);
    }

    public ItemStack getAntenna() {
        return getStackInSlot(0);
    }

    public Map<String, String> getPlayerModeMap() {
        return plpagemodes;
    }

    public String getMode(PlayerEntity pl) {
        String uuid = IKSGPlayerUtil.getUUID(pl);
        if (getPlayerModeMap().containsKey(uuid)) {
            return getPlayerModeMap().get(uuid);
        }
        return null;
    }

}
