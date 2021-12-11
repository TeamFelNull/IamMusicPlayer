package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.inventory.MusicManagerMenu;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        return super.getSyncData(player, tag);
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

    public MonitorType getMonitor(ServerPlayer player) {
        var mn = getPlayerData(player).getString("Monitor");
        if (!mn.isEmpty())
            return MonitorType.getByName(mn);
        return MonitorType.getDefault(this, player.getGameProfile().getId());
    }

    public void setMonitor(ServerPlayer player, MonitorType type) {
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
