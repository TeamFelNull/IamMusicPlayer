package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.block.MusicSharingDeviceBlock;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.util.ItemHelper;
import red.felnull.otyacraftengine.tileentity.IkisugiLockableTileEntity;
import red.felnull.otyacraftengine.util.IKSGNBTUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;
import red.felnull.otyacraftengine.util.IKSGServerUtil;

import java.util.HashMap;
import java.util.Map;

public class MusicSharingDeviceTileEntity extends IkisugiLockableTileEntity implements ITickableTileEntity {
    protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public int rotationPitch;//縦方向最大90度
    public int rotationYaw;//横方向最大360度
    private boolean inversionPitch;
    private Map<String, String> plpageModes = new HashMap<>();
    private Map<String, CompoundNBT> plyerDatas = new HashMap<>();


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
        this.plpageModes = IKSGNBTUtil.readStringMap(tag.getCompound("playerpageModes"));
        this.plyerDatas = readNBTMap(tag.getCompound("plyerDatas"));
        IKSGNBTUtil.loadAllItemsByIKSG(tag, items);
    }


    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.putInt("RotationPitch", this.rotationPitch);
        tag.putInt("RotationYaw", this.rotationYaw);
        tag.putBoolean("InversionPitch", this.inversionPitch);
        tag.put("playerpageModes", IKSGNBTUtil.writeStringMap(new CompoundNBT(), this.plpageModes));
        tag.put("plyerDatas", writeNBTMap(new CompoundNBT(), this.plyerDatas));
        IKSGNBTUtil.saveAllItemsByIKSG(tag, items);
        return tag;
    }


    public static Map<String, CompoundNBT> readNBTMap(CompoundNBT tag) {
        Map<String, CompoundNBT> map = new HashMap<>();
        tag.keySet().forEach(n -> map.put(n, tag.getCompound(n)));
        return map;
    }

    public static CompoundNBT writeNBTMap(CompoundNBT tag, Map<String, CompoundNBT> map) {
        map.forEach((n, m) -> tag.put(n, m));
        return tag;
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
        } else if (s.equals("mode")) {
            plpageModes.put(uuid, tag.getString("name"));
            return updatePlaylist(serverPlayerEntity,tag.getString("name"));
        } else if (s.equals("opengui")) {
            if (!plpageModes.containsKey(uuid)) {
                plpageModes.put(uuid, "playlist");
            }
        } else if (s.equals("pathset")) {
            if (!tag.isEmpty()) {
                CompoundNBT taga = new CompoundNBT();
                taga.putString("path", tag.getString("path"));
                setPlayerData(uuid, taga);
            } else {
                CompoundNBT taga = new CompoundNBT();
                taga.putString("path", "null");
                setPlayerData(uuid, taga);
            }

        } else if (s.equals("playlistupdate")) {
            return updatePlaylist(serverPlayerEntity, tag.getString("type"));
        }

        return null;
    }

    protected CompoundNBT updatePlaylist(ServerPlayerEntity serverPlayerEntity, String type) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("type", type);
        if (type.equals("joinplaylist")) {
            tag.put("list", PlayListGuildManeger.instance().getAllPlayListNBT(serverPlayerEntity, false));
        } else if (type.equals("playlist")) {
            tag.put("list", PlayListGuildManeger.instance().getJoinedPlayListsNBT(serverPlayerEntity));
        }
        return tag;
    }

    private void setPlayerData(String uuid, CompoundNBT tag) {
        if (!plyerDatas.containsKey(uuid)) {
            plyerDatas.put(uuid, tag);
        }
        tag.keySet().forEach(n -> plyerDatas.get(uuid).put(n, tag.get(n)));
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if (!ItemHelper.isAntenna(getAntenna())) {
                plpageModes.entrySet().stream().filter(n -> !n.getValue().equals("noantenna")).forEach(n -> plpageModes.put(n.getKey(), "noantenna"));
            } else {
                plpageModes.entrySet().stream().filter(n -> n.getValue().equals("noantenna")).forEach(n -> plpageModes.put(n.getKey(), "playlist"));
            }

            plyerDatas.entrySet().stream().filter(n -> !IKSGServerUtil.isOnlinePlayer(n.getKey())).forEach(n -> {
                CompoundNBT tagaa = new CompoundNBT();
                tagaa.putString("path", "null");
                setPlayerData(n.getKey(), tagaa);
            });
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
            } else {
                plpageModes.entrySet().stream().filter(n -> !n.getValue().equals("playlist") && !n.getValue().equals("noantenna")).forEach(n -> plpageModes.put(n.getKey(), "playlist"));
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
        return plpageModes;
    }

    public String getPlayerPath(String uuid) {

        if (!plyerDatas.containsKey(uuid) || !plyerDatas.get(uuid).contains("path") || plyerDatas.get(uuid).getString("path").equals("null"))
            return null;

        return plyerDatas.get(uuid).getString("path");
    }


    public String getMode(PlayerEntity pl) {
        String uuid = IKSGPlayerUtil.getUUID(pl);
        if (getPlayerModeMap().containsKey(uuid)) {
            return getPlayerModeMap().get(uuid);
        }
        return null;
    }

}
