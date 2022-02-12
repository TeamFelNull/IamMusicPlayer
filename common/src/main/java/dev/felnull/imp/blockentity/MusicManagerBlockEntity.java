package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.inventory.MusicManagerMenu;
import dev.felnull.imp.music.resource.AuthorityInfo;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.server.music.MusicManager;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MusicManagerBlockEntity extends IMPBaseEntityBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(0, ItemStack.EMPTY);
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
        return new MusicManagerMenu(i, inventory, this, getBlockPos());
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, MusicManagerBlockEntity blockEntity) {
        if (!level.isClientSide()) {
            blockEntity.playerData.forEach((n, m) -> {
                var monst = m.getString("Monitor");
                if (!monst.isEmpty()) {
                    var type = MonitorType.getByName(monst);
                    if ((blockEntity.isPower() && type == MonitorType.OFF) || (!blockEntity.isPower() && type != MonitorType.OFF))
                        m.putString("Monitor", MonitorType.getDefault(blockEntity, n).getName());
                    if (type == MonitorType.OFF)
                        m.remove("SelectedPlayList");
                    if (type != null && type.isNeedSelectPlayList() && !m.contains("SelectedPlayList")) {
                        m.putString("Monitor", MonitorType.PLAY_LIST.getName());
                    }
                }
            });
            blockEntity.setChanged();
            blockEntity.sync();
        }
    }

    private void updateMonitor(ServerPlayer player, MonitorType newM, MonitorType oldM) {
        var tag = getPlayerData(player);

        boolean keepFlg = (oldM == MonitorType.ADD_MUSIC && newM == MonitorType.SEARCH_MUSIC) || (oldM == MonitorType.SEARCH_MUSIC && newM == MonitorType.ADD_MUSIC);
        boolean keepFlg2 = (oldM == MonitorType.ADD_MUSIC && newM == MonitorType.UPLOAD_MUSIC) || (oldM == MonitorType.UPLOAD_MUSIC && newM == MonitorType.ADD_MUSIC);

        if (!keepFlg && !keepFlg2) {
            tag.remove("Image");
            tag.remove("ImageURL");
            tag.remove("CreateName");
            tag.remove("Publishing");
            tag.remove("InitialAuthority");
            tag.remove("InvitePlayerName");
            tag.remove("InvitePlayers");
            tag.remove("MusicLoaderType");
            tag.remove("MusicSourceName");
            tag.remove("MusicSource");
            tag.remove("MusicAuthor");
        }

        tag.remove("MusicSearchName");

        var pl = getSelectedPlayList(player);
        if (oldM == MonitorType.DETAIL_PLAY_LIST && newM == MonitorType.EDIT_PLAY_LIST && pl != null) {
            var mm = MusicManager.getInstance();
            var pls = mm.getSaveData().getPlayLists().get(pl);
            if (pls != null) {
                setImage(player, pls.getImage());
                setCreateName(player, pls.getName());
                setInitialAuthority(player, pls.getAuthority().getInitialAuthority() == AuthorityInfo.AuthorityType.MEMBER ? "member" : "read_only");
                setPublishing(player, pls.getAuthority().isPublic() ? "public" : "private");
                setInvitePlayers(player, pls.getAuthority().getRawAuthority().entrySet().stream().filter(n -> n.getValue().isInvitation()).map(Map.Entry::getKey).toList());
            }
        }
        setChanged();
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

    @Nullable
    public UUID getSelectedPlayList(@NotNull ServerPlayer player) {
        var tag = getPlayerData(player);
        if (tag.contains("SelectedPlayList"))
            return tag.getUUID("SelectedPlayList");
        return null;
    }

    public CompoundTag getPlayerData(ServerPlayer player) {
        var id = player.getGameProfile().getId();
        if (!playerData.containsKey(id))
            playerData.put(id, new CompoundTag());
        return playerData.get(id);
    }


    public UUID getMySelectedPlayList() {
        if (!myData.contains("SelectedPlayList"))
            return null;
        return myData.getUUID("SelectedPlayList");
    }

    public MusicSource getMyMusicSource() {
        if (myData.contains("MusicSource"))
            return OENbtUtil.readSerializable(myData, "MusicSource", new MusicSource());
        return MusicSource.EMPTY;
    }

    public void setMusicSource(ServerPlayer player, MusicSource source) {
        OENbtUtil.writeSerializable(getPlayerData(player), "MusicSource", source);
        setChanged();
    }

    public String getMyMusicSourceName() {
        return myData.getString("MusicSourceName");
    }

    public void setMusicSourceName(ServerPlayer player, String name) {
        getPlayerData(player).putString("MusicSourceName", name);
        setChanged();
    }

    public String getMyMusicAuthor() {
        return myData.getString("MusicAuthor");
    }

    public void setMusicAuthor(ServerPlayer player, String name) {
        getPlayerData(player).putString("MusicAuthor", name);
        setChanged();
    }

    public String getMyMusicSearchName() {
        return myData.getString("MusicSearchName");
    }

    public void setMusicSearchName(ServerPlayer player, String name) {
        getPlayerData(player).putString("MusicSearchName", name);
        setChanged();
    }

    public String getMyMusicLoaderType() {
        return myData.getString("MusicLoaderType");
    }

    public void setMusicLoaderType(ServerPlayer player, String name) {
        getPlayerData(player).putString("MusicLoaderType", name);
        setChanged();
    }

    public void setSelectedPlayList(ServerPlayer player, UUID selectedPlayList) {
        var type = getMonitor(player);
        if (type != null && type.isNeedSelectPlayList()) {
            if (getPlayerData(player).contains("SelectedPlayList")) {
                var old = getPlayerData(player).getUUID("SelectedPlayList");
                if (selectedPlayList == null || !selectedPlayList.equals(old))
                    setMonitor(player, MonitorType.PLAY_LIST);
            }
        }
        if (selectedPlayList != null) {
            getPlayerData(player).putUUID("SelectedPlayList", selectedPlayList);
        } else {
            getPlayerData(player).remove("SelectedPlayList");
        }
        setChanged();
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
        setChanged();
    }

    public String getMyInvitePlayerName() {
        return myData.getString("InvitePlayerName");
    }

    public void setInvitePlayerName(ServerPlayer player, String name) {
        getPlayerData(player).putString("InvitePlayerName", name);
        setChanged();
    }

    public void setImage(ServerPlayer player, ImageInfo image) {
        OENbtUtil.writeSerializable(getPlayerData(player), "Image", image);
        setChanged();
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
        setChanged();
    }

    public String getMyPublishing() {
        return myData.getString("Publishing");
    }

    public void setPublishing(ServerPlayer player, String publishing) {
        getPlayerData(player).putString("Publishing", publishing);
        setChanged();
    }

    public String getMyCreateName() {
        return myData.getString("CreateName");
    }

    public void setCreateName(ServerPlayer player, String name) {
        getPlayerData(player).putString("CreateName", name);
        setChanged();
    }

    public void setImageURL(ServerPlayer player, String url) {
        getPlayerData(player).putString("ImageURL", url);
        setChanged();
    }

    public String getMyImageURL() {
        return myData.getString("ImageURL");
    }


    public MonitorType getMonitor(ServerPlayer player) {
        return MonitorType.getByNameOrDefault(getPlayerData(player).getString("Monitor"), this, player.getGameProfile().getId());
    }

    public void setMonitor(ServerPlayer player, MonitorType type) {
        var oldM = MonitorType.getByNameOrDefault(getPlayerData(player).getString("Monitor"), this, player.getGameProfile().getId());
        if (oldM != type)
            updateMonitor(player, type, oldM);
        getPlayerData(player).putString("Monitor", type.getName());
        setChanged();
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
            if (data.contains("playlist")) {
                var pl = data.getUUID("playlist");
                MusicManager.getInstance().addPlayListToPlayer(pl, player);
            }
            return data;
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
        } else if ("set_selected_playlist".equals(name)) {
            if (data.contains("playlist")) {
                var id = data.getUUID("playlist");
                setSelectedPlayList(player, id);
            } else {
                setSelectedPlayList(player, null);
            }
            return data;
        } else if ("set_music_loader_type".equals(name)) {
            var lname = data.getString("name");
            setMusicLoaderType(player, lname);
            return null;
        } else if ("set_music_source_name".equals(name)) {
            var mname = data.getString("name");
            setMusicSourceName(player, mname);
            return null;
        } else if ("set_music_source".equals(name)) {
            var ms = OENbtUtil.readSerializable(data, "MusicSource", new MusicSource());
            setMusicSource(player, ms);
            return null;
        } else if ("set_music_search_name".equals(name)) {
            var sname = data.getString("name");
            setMusicSearchName(player, sname);
            return null;
        } else if ("set_music_author".equals(name)) {
            var author = data.getString("author");
            setMusicAuthor(player, author);
            return null;
        }
        return super.onInstruction(player, name, num, data);
    }

    @Override
    public @NotNull NonNullList<ItemStack> getItems() {
        return items;
    }

    public static enum MonitorType {
        OFF("off", false),
        TEST("test", false),
        PLAY_LIST("play_list", false),
        ADD_PLAY_LIST("add_play_list", false),
        ADD_ONLINE_PLAY_LIST("add_online_play_list", false),
        EDIT_PLAY_LIST("edit_play_list", true),
        DETAIL_PLAY_LIST("detail_play_list", true),
        CREATE_PLAY_LIST("create_play_list", false),
        DELETE_PLAY_LIST("delete_play_list", true),
        ADD_MUSIC("add_music", true),
        SEARCH_MUSIC("search_music", true),
        UPLOAD_MUSIC("upload_music", true),
        EDIT_MUSIC("edit_music", true),
        DELETE_MUSIC("delete_music", true),
        IMPORT_YOUTUBE_PLAY_LIST("import_youtube_play_list", false);
        private final String name;
        private final boolean needSelectPlayList;

        private MonitorType(String name, boolean needSelectPlayList) {
            this.name = name;
            this.needSelectPlayList = needSelectPlayList;
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

        public boolean isNeedSelectPlayList() {
            return needSelectPlayList;
        }

        public static MonitorType getDefault(MusicManagerBlockEntity blockEntity, UUID player) {
            return blockEntity.isPower() ? PLAY_LIST : OFF;
        }
    }
}
