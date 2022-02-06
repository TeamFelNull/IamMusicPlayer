package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.inventory.CassetteDeckMenu;
import dev.felnull.imp.util.IMPItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CassetteDeckBlockEntity extends IMPBaseEntityBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private MonitorType monitor = MonitorType.OFF;
    private ItemStack lastCassetteTape = ItemStack.EMPTY;
    private ItemStack oldCassetteTape = ItemStack.EMPTY;
    private boolean changeCassetteTape;
    private boolean lidOpen;
    private int lidOpenProgressOld;
    private int lidOpenProgress;

    public CassetteDeckBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IMPBlockEntitys.CASSETTE_DECK, blockPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, CassetteDeckBlockEntity blockEntity) {

        blockEntity.lidOpenProgressOld = blockEntity.lidOpenProgress;

        if (blockEntity.lidOpen) {
            if (blockEntity.lidOpenProgress < blockEntity.getLidOpenProgressAll())
                blockEntity.lidOpenProgress++;
        } else {
            if (blockEntity.lidOpenProgress > 0)
                blockEntity.lidOpenProgress--;
        }

        if (!level.isClientSide()) {

            if (blockEntity.isPower()) {
                if (blockEntity.monitor == MonitorType.OFF)
                    blockEntity.monitor = MonitorType.MENU;
            } else {
                blockEntity.monitor = MonitorType.OFF;
            }

            if (!ItemStack.matches(blockEntity.lastCassetteTape, blockEntity.getCassetteTape()))
                blockEntity.changeCassetteTape(blockEntity.lastCassetteTape);

            blockEntity.lastCassetteTape = blockEntity.getCassetteTape().copy();

            if (blockEntity.changeCassetteTape) {

                if (!blockEntity.isLidOpen())
                    blockEntity.startLidOpen(true);

                if (blockEntity.lidOpenProgress >= blockEntity.getLidOpenProgressAll()) {
                    blockEntity.changeCassetteTape = false;
                    blockEntity.startLidOpen(false);
                }
            }

            blockEntity.sync();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.lidOpen = tag.getBoolean("LidOpen");
        this.lidOpenProgress = tag.getInt("LidOpenProgress");
        this.monitor = MonitorType.getByName(tag.getString("Monitor"));
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putBoolean("LidOpen", this.lidOpen);
        tag.putInt("LidOpenProgress", this.lidOpenProgress);
        tag.putString("Monitor", monitor.getName());
        return tag;
    }

    @Override
    public CompoundTag getSyncData(ServerPlayer player, CompoundTag tag) {
        tag.putBoolean("LidOpen", this.lidOpen);
        tag.put("OldCassetteTape", this.oldCassetteTape.save(new CompoundTag()));
        tag.putBoolean("ChangeCassetteTape", this.changeCassetteTape);
        tag.putString("Monitor", monitor.getName());
        return super.getSyncData(player, tag);
    }

    @Override
    public void onSync(CompoundTag tag) {
        super.onSync(tag);
        this.lidOpen = tag.getBoolean("LidOpen");
        this.oldCassetteTape = ItemStack.of(tag.getCompound("OldCassetteTape"));
        this.changeCassetteTape = tag.getBoolean("ChangeCassetteTape");
        this.monitor = MonitorType.getByName(tag.getString("Monitor"));
    }

    public int getLidOpenProgressAll() {
        return 10;
    }

    public MonitorType getMonitor() {
        return monitor;
    }

    public float getLidOpenProgress(float partialTicks) {
        return Mth.lerp(partialTicks, lidOpenProgressOld, lidOpenProgress);
    }

    public void startLidOpen(boolean open) {
        lidOpen = open;
        level.playSound(null, getBlockPos(), isLidOpen() ? SoundEvents.WOODEN_DOOR_OPEN : SoundEvents.WOODEN_DOOR_CLOSE, SoundSource.BLOCKS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public boolean isLidOpen() {
        return lidOpen;
    }

    public boolean isChangeCassetteTape() {
        return changeCassetteTape;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected Component getDefaultName() {
        return IMPBlocks.CASSETTE_DECK.getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new CassetteDeckMenu(i, inventory, this, getBlockPos());
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        if (i == 0)
            return IMPItemUtil.isCassetteTape(itemStack);
        return super.canPlaceItem(i, itemStack);
    }

    public ItemStack getCassetteTape() {
        return getItem(0);
    }

    public ItemStack getOldCassetteTape() {
        return oldCassetteTape;
    }

    public void changeCassetteTape(ItemStack old) {
        this.oldCassetteTape = old;
        this.changeCassetteTape = true;
    }

    @Override
    public CompoundTag onInstruction(ServerPlayer player, String name, int num, CompoundTag data) {
        if ("monitor".equals(name)) {
            this.monitor = MonitorType.getByName(data.getString("name"));
            return null;
        }
        return super.onInstruction(player, name, num, data);
    }

    public static enum MonitorType {
        OFF("off"),
        MENU("menu"),
        WRITE("write"),
        PLAYBACK("playback");
        private final String name;

        private MonitorType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static MonitorType getByName(String name) {
            for (MonitorType value : values()) {
                if (value.getName().equals(name))
                    return value;
            }
            return MonitorType.OFF;
        }
    }
}
