package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.block.IMPAbstractEquipmentBlock;
import red.felnull.imp.block.MusicSharingDeviceBlock;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.data.PlayMusicManeger;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.imp.util.ItemHelper;
import red.felnull.otyacraftengine.util.IKSGNBTUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;
import red.felnull.otyacraftengine.util.IKSGServerUtil;

import java.util.HashMap;
import java.util.Map;

public class MusicSharingDeviceTileEntity extends IMPAbstractEquipmentTileEntity {
    protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    private Map<String, String> plpageModes = new HashMap<>();
    private Map<String, CompoundNBT> plyerDatas = new HashMap<>();

    public MusicSharingDeviceTileEntity() {
        super(IMPTileEntityTypes.MUSIC_SHARING_DEVICE);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void readByIKSG(BlockState state, CompoundNBT tag) {
        super.readByIKSG(state, tag);
        this.plpageModes = IKSGNBTUtil.readStringMap(tag.getCompound("playerpageModes"));
        this.plyerDatas = IKSGNBTUtil.readNBTMap(tag.getCompound("plyerDatas"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.put("playerpageModes", IKSGNBTUtil.writeStringMap(new CompoundNBT(), this.plpageModes));
        tag.put("plyerDatas", IKSGNBTUtil.writeNBTMap(new CompoundNBT(), this.plyerDatas));
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
    public CompoundNBT instructionFromClient(ServerPlayerEntity serverPlayerEntity, String s, CompoundNBT tag) {
        String uuid = IKSGPlayerUtil.getUUID(serverPlayerEntity);
        if (s.equals("power")) {
            setBlockState(getBlockState().with(MusicSharingDeviceBlock.ON, tag.getBoolean("on")));
            if (tag.getBoolean("on")) {
                CompoundNBT dtag = new CompoundNBT();
                dtag.put("playlist", updatePlayList(serverPlayerEntity, "playlist"));
                dtag.put("playmusic", updatePlayMusic(serverPlayerEntity, tag.getString("listuuid")));
                return dtag;
            }
        } else if (s.equals("mode")) {
            plpageModes.put(uuid, tag.getString("name"));
            CompoundNBT dtag = new CompoundNBT();
            dtag.put("playlist", updatePlayList(serverPlayerEntity, tag.getString("name")));
            if (tag.contains("listuuid"))
                dtag.put("playmusic", updatePlayMusic(serverPlayerEntity, tag.getString("listuuid")));
            return dtag;
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
            return updatePlayList(serverPlayerEntity, tag.getString("type"));
        } else if (s.equals("playmusicupdate")) {
            return updatePlayMusic(serverPlayerEntity, tag.getString("listuuid"));
        }

        return null;
    }

    protected CompoundNBT updatePlayMusic(ServerPlayerEntity playerEntity, String uuid) {
        CompoundNBT tag = new CompoundNBT();
        tag.put("list", PlayMusicManeger.instance().getAllPlayMusicNBT(playerEntity, PlayList.getPlayListByUUID(uuid)));
        return tag;
    }

    protected CompoundNBT updatePlayList(ServerPlayerEntity serverPlayerEntity, String type) {
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
        super.tick();
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

            if (!isOn()) {
                plpageModes.entrySet().stream().filter(n -> !n.getValue().equals("playlist") && !n.getValue().equals("noantenna")).forEach(n -> plpageModes.put(n.getKey(), "playlist"));
            }
        }
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
