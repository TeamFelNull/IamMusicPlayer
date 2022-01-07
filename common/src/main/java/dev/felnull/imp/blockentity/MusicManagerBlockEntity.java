package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.inventory.MusicManagerMenu;
import dev.felnull.imp.music.MusicManager;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class MusicManagerBlockEntity extends IMPBaseEntityBlockEntity {
    protected final Map<UUID, CompoundTag> playerData = new HashMap<>();
    private CompoundTag myData = new CompoundTag();

    public MusicManagerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IMPBlockEntitys.MUSIC_MANAGER, blockPos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return IMPBlocks.MUSIC_MANAGER.getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new MusicManagerMenu(i, getBlockPos(), this, inventory);
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return null;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return null;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this)
            return false;
        return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent() {
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, MusicManagerBlockEntity blockEntity) {
        if (!level.isClientSide()) {
            blockEntity.playerData.forEach((n, m) -> {
                var monst = m.getString("Monitor");
                if (!monst.isEmpty()) {
                    var type = MonitorType.getByName(monst);
                    if ((blockEntity.isPower() && type == MonitorType.OFF) || (!blockEntity.isPower() && type != MonitorType.OFF))
                        m.putString("Monitor", MonitorType.getDefault(blockEntity, n).getName());
                }
            });

            blockEntity.sync();
        }
    }

    private void updateMonitor(ServerPlayer player, MonitorType newM, MonitorType oldM) {
        var tag = getPlayerData(player);
        tag.remove("Image");
        tag.remove("ImageURL");
        tag.remove("CreateName");
        tag.remove("Publishing");
        tag.remove("InitialAuthority");
        tag.remove("InvitePlayerName");
        tag.remove("InvitePlayers");
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        OENbtUtil.readMap(tag, "PlayerData", playerData);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        OENbtUtil.writeMap(tag, "PlayerData", playerData);
        return super.save(tag);
    }

    @Override
    public CompoundTag getSyncData(ServerPlayer player, CompoundTag tag) {
        var pltag = playerData.get(player.getGameProfile().getId());
        if (pltag != null)
            tag.put("Data", pltag);
        return tag;
    }

    @Override
    public void onSync(CompoundTag tag) {
        super.onSync(tag);
        this.myData = tag.getCompound("Data");
    }


    public CompoundTag getPlayerData(ServerPlayer player) {
        var id = player.getGameProfile().getId();
        if (!playerData.containsKey(id))
            playerData.put(id, new CompoundTag());
        return playerData.get(id);
    }

    public List<UUID> getMyInvitePlayers() {
        List<UUID> pls = new ArrayList<>();
        OENbtUtil.readUUIDList(myData, "InvitePlayers", pls);
        return pls;
    }

    public void setInvitePlayers(ServerPlayer player, List<UUID> players) {
        var tag = getPlayerData(player);
        tag.remove("InvitePlayers");
        OENbtUtil.writeUUIDList(tag, "InvitePlayers", players);
    }

    public String getMyInvitePlayerName() {
        return myData.getString("InvitePlayerName");
    }

    public void setInvitePlayerName(ServerPlayer player, String name) {
        getPlayerData(player).putString("InvitePlayerName", name);
    }

    public void setImage(ServerPlayer player, ImageInfo image) {
        OENbtUtil.writeSerializable(getPlayerData(player), "Image", image);
    }

    public ImageInfo getMyImage() {
        if (myData.getCompound("Image").isEmpty())
            return ImageInfo.EMPTY;
        return OENbtUtil.readSerializable(myData, "Image", new ImageInfo());
    }

    public String getMyInitialAuthority() {
        return myData.getString("InitialAuthority");
    }

    public void setInitialAuthority(ServerPlayer player, String initialAuthority) {
        getPlayerData(player).putString("InitialAuthority", initialAuthority);
    }

    public String getMyPublishing() {
        return myData.getString("Publishing");
    }

    public void setPublishing(ServerPlayer player, String publishing) {
        getPlayerData(player).putString("Publishing", publishing);
    }

    public String getMyCreateName() {
        return myData.getString("CreateName");
    }

    public void setCreateName(ServerPlayer player, String name) {
        getPlayerData(player).putString("CreateName", name);
    }

    public void setImageURL(ServerPlayer player, String url) {
        getPlayerData(player).putString("ImageURL", url);
    }

    public String getMyImageURL() {
        return myData.getString("ImageURL");
    }

    public void setMonitor(ServerPlayer player, MonitorType type) {
        var oldM = MonitorType.getByNameOrDefault(getPlayerData(player).getString("Monitor"), this, player.getGameProfile().getId());
        if (oldM != type)
            updateMonitor(player, type, oldM);
        getPlayerData(player).putString("Monitor", type.getName());
    }

    public MonitorType getMyMonitor(Player player) {
        var name = myData.getString("Monitor");
        return MonitorType.getByNameOrDefault(name, this, player.getGameProfile().getId());
    }

    @Override
    public CompoundTag onInstruction(ServerPlayer player, String name, int num, CompoundTag data) {
        if ("set_monitor".equals(name)) {
            var mn = data.getString("type");
            if (!mn.isEmpty())
                setMonitor(player, MonitorType.getByName(mn));
            return null;
        } else if ("add_playlist".equals(name)) {
            var pl = data.getUUID("playlist");
            MusicManager.getInstance().addPlayListToPlayer(player, pl);
            return null;
        } else if ("set_image_url".equals(name)) {
            var url = data.getString("url");
            setImageURL(player, url);
            return null;
        } else if ("set_image".equals(name)) {
            var image = OENbtUtil.readSerializable(data, "image", new ImageInfo());
            setImage(player, image);
            return null;
        } else if ("set_create_name".equals(name)) {
            var cname = data.getString("name");
            setCreateName(player, cname);
            return null;
        } else if ("set_publishing".equals(name)) {
            var pub = data.getString("publishing");
            setPublishing(player, pub);
            return null;
        } else if ("set_initial_authority".equals(name)) {
            var ina = data.getString("initial_authority");
            setInitialAuthority(player, ina);
            return null;
        } else if ("set_invite_player_name".equals(name)) {
            var pname = data.getString("name");
            setInvitePlayerName(player, pname);
            return null;
        } else if ("set_invite_players".equals(name)) {
            List<UUID> pls = new ArrayList<>();
            OENbtUtil.readUUIDList(data, "players", pls);
            setInvitePlayers(player, pls);
            return null;
        }
        return super.onInstruction(player, name, num, data);
    }

    public static enum MonitorType {
        OFF("off"),
        TEST("test"),
        PLAY_LIST("play_list"),
        ADD_PLAY_LIST("add_play_list"),
        CREATE_PLAY_LIST("create_play_list"),
        DELETE_PLAY_LIST("delete_play_list"),
        ADD_MUSIC("add_music"),
        EDIT_MUSIC("edit_music"),
        DELETE_MUSIC("delete_music");
        private final String name;

        private MonitorType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static MonitorType getByNameOrDefault(String name, MusicManagerBlockEntity blockEntity, UUID player) {
            for (MonitorType value : values()) {
                if (value.getName().equals(name))
                    return value;
            }
            return getDefault(blockEntity, player);
        }

        public static MonitorType getByName(String name) {
            for (MonitorType value : values()) {
                if (value.getName().equals(name))
                    return value;
            }
            return MonitorType.OFF;
        }

        public static MonitorType getDefault(MusicManagerBlockEntity blockEntity, UUID player) {
            return blockEntity.isPower() ? PLAY_LIST : OFF;
        }

    }

}
