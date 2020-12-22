package red.felnull.imp.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.container.MusicSharingDeviceContainer;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.util.ItemHelper;
import red.felnull.otyacraftengine.util.IKSGNBTUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;
import red.felnull.otyacraftengine.util.IKSGServerUtil;

import java.util.HashMap;
import java.util.Map;

public class MusicSharingDeviceTileEntity extends IMPAbstractPAPLEquipmentTileEntity {
    protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
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
        this.plyerDatas = IKSGNBTUtil.readNBTMap(tag.getCompound("plyerDatas"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
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
    public CompoundNBT instructionFromClient(ServerPlayerEntity player, String s, CompoundNBT tag) {
        if (s.equals("Mode")) {
            setScreen(player, Screen.getScreenByName(tag.getString("name")));
        } else if (s.equals("PathSet")) {
            setPath(player, tag.getString("path"));
        } else if (s.equals("CanJoinPlayListUpdate")) {
            return getUpdateCanJoinPlayListTag(player);
        }
        return super.instructionFromClient(player, s, tag);
    }

    private CompoundNBT getUpdateCanJoinPlayListTag(ServerPlayerEntity player) {
        return PlayListGuildManeger.instance().getAllPlayListNBT(player, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote) {
            plyerDatas.forEach((n, m) -> {
                if (!IKSGServerUtil.isOnlinePlayer(n)) {
                    setPath(n, null);
                }
                if (ItemHelper.isAntenna(getPAntenna())) {
                    if (getScreen(n) == Screen.NO_ANTENNA) {
                        setScreen(n, Screen.PLAYLIST);
                    }
                } else {
                    if (getScreen(n) != Screen.NO_ANTENNA) {
                        setScreen(n, Screen.NO_ANTENNA);
                    }
                }
                if (isOn()) {
                    if (getScreen(n) == Screen.OFF) {
                        setScreen(n, Screen.PLAYLIST);
                    }
                } else {
                    if (getScreen(n) != Screen.OFF) {
                        setScreen(n, Screen.OFF);
                    }
                }
            });
        }
    }

    public void setPath(String pluuid, String path) {
        if (path == null)
            getPlayerData(pluuid).remove("Path");
        else
            getPlayerData(pluuid).putString("Path", path);
    }

    public void setPath(PlayerEntity pl, String path) {
        if (path == null || path.isEmpty())
            getPlayerData(pl).remove("Path");
        else
            getPlayerData(pl).putString("Path", path);
    }

    public String getPlayerPath(PlayerEntity pl) {
        return getPlayerData(pl).getString("Path");
    }

    public void setScreen(String pluuid, Screen screen) {
        getPlayerData(pluuid).putString("PlayerCurrentScreen", screen.getName());
    }

    public void setScreen(PlayerEntity pl, Screen screen) {
        getPlayerData(pl).putString("PlayerCurrentScreen", screen.getName());
    }

    public Screen getScreen(String pluuid) {
        return Screen.getScreenByName(getPlayerData(pluuid).getString("PlayerCurrentScreen"));
    }

    public Screen getScreen(PlayerEntity pl) {
        return Screen.getScreenByName(getPlayerData(pl).getString("PlayerCurrentScreen"));
    }

    private CompoundNBT getPlayerData(String pluuid) {
        if (!plyerDatas.containsKey(pluuid))
            plyerDatas.put(pluuid, new CompoundNBT());
        return plyerDatas.get(pluuid);
    }

    private CompoundNBT getPlayerData(PlayerEntity pl) {
        String uuid = IKSGPlayerUtil.getUUID(pl);
        if (!plyerDatas.containsKey(uuid))
            plyerDatas.put(uuid, new CompoundNBT());
        return plyerDatas.get(uuid);
    }

    public enum Screen {
        OFF("off"),
        ON("on"),
        PLAYLIST("playlist"),
        NO_ANTENNA("no_antenna"),
        CREATE_PLAYLIST("create_playlist"),
        ADD_PLAYLIST("add_playlist"),
        JOIN_PLAYLIST("join_playlist"),
        ADD_PLAYMUSIC_1("add_playmusic_1"),
        ADD_PLAYMUSIC_2("add_playmusic_2"),
        YOUTUBE_SEARCH("youtube_search");
        private final String name;

        private Screen(String name) {
            this.name = name;
        }

        public static Screen getScreenByName(String name) {
            for (Screen sc : values()) {
                if (sc.getName().equals(name))
                    return sc;
            }
            return OFF;
        }

        public String getName() {
            return name;
        }

        public ResourceLocation getTexLocation() {
            return new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_sharing_device_screen/" + getName() + ".png");
        }
    }
}
