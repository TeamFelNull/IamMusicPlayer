package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.BoomboxBlock;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.data.BoomboxData;
import dev.felnull.imp.inventory.BoomboxMenu;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.imp.server.music.ringer.IBoomboxRinger;
import dev.felnull.imp.server.music.ringer.IMusicRinger;
import dev.felnull.imp.server.music.ringer.MusicRingManager;
import dev.felnull.imp.util.IMPItemUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BoomboxBlockEntity extends IMPBaseEntityBlockEntity implements IBoomboxRinger {
    private final BoomboxData boomboxData;
    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private final UUID ringerUUID = UUID.randomUUID();

    public BoomboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IMPBlockEntitys.BOOMBOX.get(), blockPos, blockState);
        this.boomboxData = new BoomboxData(new BoomboxData.DataAccess() {
            @Override
            public ItemStack getCassetteTape() {
                return BoomboxBlockEntity.this.getCassetteTape();
            }

            @Override
            public ItemStack getAntenna() {
                return BoomboxBlockEntity.this.getAntenna();
            }

            @Override
            public boolean isPower() {
                return BoomboxBlockEntity.this.isPower();
            }

            @Override
            public void setPower(boolean power) {
                BoomboxBlockEntity.this.setPower(power);
            }

            @Override
            public IMusicRinger getRinger() {
                return BoomboxBlockEntity.this;
            }

            @Override
            public Vec3 getPosition() {
                return new Vec3(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
            }

            @Override
            public void setCassetteTape(ItemStack stack) {
                setItemNoUpdate(0, stack);
            }

            @Override
            public void dataUpdate(BoomboxData data) {
                setChanged();
            }
        });
    }

    @Override
    public void setItem(int i, ItemStack stack) {
        if (i == 0)
            this.boomboxData.onCassetteTapeChange(stack, getCassetteTape());
        setItemNoUpdate(i, stack);
    }

    public void setItemNoUpdate(int i, ItemStack stack) {
        super.setItem(i, stack);
    }

    public void setBoomboxData(BoomboxData data) {
        boomboxData.load(data.save(new CompoundTag(), false, false), false, false);
    }

    @Override
    public ItemStack createRetainDropItem() {
        return BoomboxItem.createByBE(this, false);
    }

    @Override
    public boolean isRetainDrop() {
        return true;
    }

    @Override
    public boolean isRetainEmpty() {
        return false;
    }

    @Override
    protected Component getDefaultName() {
        return IMPBlocks.BOOMBOX.get().getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory) {
        return new BoomboxMenu(i, inventory, this, getBlockPos(), ItemStack.EMPTY, null);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.boomboxData.load(tag.getCompound("BoomBoxData"), false, false);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("BoomBoxData", this.boomboxData.save(new CompoundTag(), false, false));
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BoomboxBlockEntity blockEntity) {
        blockEntity.boomboxData.tick(level);
        if (!level.isClientSide()) {
            blockEntity.ringerTick();
            blockEntity.setRaisedHandleState(blockEntity.boomboxData.getHandleRaisedProgress() >= blockEntity.boomboxData.getHandleRaisedMax());
            blockEntity.sync();
            blockEntity.setChanged();
        }
    }

    @Override
    public CompoundTag getSyncData(ServerPlayer player, CompoundTag tag) {
        tag.put("BoomBoxData", this.boomboxData.save(new CompoundTag(), false, true));
        return super.getSyncData(player, tag);
    }

    @Override
    public void onSync(CompoundTag tag) {
        super.onSync(tag);
        this.boomboxData.load(tag.getCompound("BoomBoxData"), false, true);
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return (i == 0 && IMPItemUtil.isCassetteTape(itemStack)) || (i == 1 && IMPItemUtil.isAntenna(itemStack));
    }

    @Override
    public @NotNull NonNullList<ItemStack> getItems() {
        return items;
    }

    public void setRaisedHandleState(boolean raised) {
        var bs = getBlockState().setValue(BoomboxBlock.RAISED, raised);
        getLevel().setBlock(getBlockPos(), bs, 2);
    }

    public BoomboxData getBoomboxData() {
        return boomboxData;
    }

    @Override
    public CompoundTag onInstruction(ServerPlayer player, String name, int num, CompoundTag data) {
        var ret = boomboxData.onInstruction(player, name, num, data);
        if (ret != null)
            return ret;
        return super.onInstruction(player, name, num, data);
    }

    public ItemStack getCassetteTape() {
        return getItem(0);
    }

    public ItemStack getAntenna() {
        return getItem(1);
    }

    @Override
    public Component getRingerName() {
        return getDefaultName();
    }

    @Override
    public UUID getRingerUUID() {
        return ringerUUID;
    }

    @Override
    public boolean isRingerExist() {
        if (getLevel() == null || level != getLevel()) return false;
        return getBlockPos() != null && level.getBlockEntity(getBlockPos()) == this;
    }

    @Override
    public @NotNull BoomboxData getRingerBoomboxData() {
        return boomboxData;
    }

    @Override
    public ServerLevel getRingerLevel() {
        return (ServerLevel) this.level;
    }

    @Override
    public Pair<ResourceLocation, CompoundTag> getRingerTracker() {
        return Pair.of(MusicRingManager.FIXED_TRACKER, MusicRingManager.createFixedTracker(getRingerSpatialPosition()));
    }

    @Override
    public @NotNull Vec3 getRingerSpatialPosition() {
        return new Vec3(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5);
    }

    public void setByItem(ItemStack stack) {
        setPower(BoomboxItem.isPowerOn(stack));
        setItemNoUpdate(0, BoomboxItem.getCassetteTape(stack));
        setItemNoUpdate(1, BoomboxItem.getAntenna(stack));
        setBoomboxData(BoomboxItem.getData(stack));
        setPower(BoomboxItem.isPowerOn(stack));
        if (BoomboxItem.getTransferProgress(stack) == 0) {
            boomboxData.setHandleRaising(true);
            boomboxData.setHandleRaisedProgress(boomboxData.getHandleRaisedMax());
            boomboxData.setHandleRaisedProgressOld(boomboxData.getHandleRaisedMax());
        }
    }
}
