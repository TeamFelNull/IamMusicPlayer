package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.BoomboxBlock;
import dev.felnull.imp.block.IMPBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BoomboxBlockEntity extends IMPBaseEntityBlockEntity {
    protected boolean handleRaising = true;
    protected int handleRaisedProgressOld = getHandleRaisedAll();
    protected int handleRaisedProgress = getHandleRaisedAll();
    protected boolean lidOpen;
    protected int lidOpenProgressOld;
    protected int lidOpenProgress;

    public BoomboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IMPBlockEntitys.BOOMBOX, blockPos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return IMPBlocks.BOOMBOX.getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
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
        return false;
    }

    @Override
    public void clearContent() {
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.handleRaising = tag.getBoolean("HandleRaising");
        this.lidOpen = tag.getBoolean("LidOpen");
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putBoolean("HandleRaising", this.handleRaising);
        tag.putBoolean("LidOpen", this.lidOpen);
        return tag;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BoomboxBlockEntity blockEntity) {

        blockEntity.handleRaisedProgressOld = blockEntity.handleRaisedProgress;
        blockEntity.lidOpenProgressOld = blockEntity.lidOpenProgress;

        if (blockEntity.handleRaising) {
            if (blockEntity.handleRaisedProgress < blockEntity.getHandleRaisedAll())
                blockEntity.handleRaisedProgress++;
        } else {
            if (blockEntity.handleRaisedProgress > 0)
                blockEntity.handleRaisedProgress--;
        }

        if (blockEntity.lidOpen) {
            if (blockEntity.lidOpenProgress < blockEntity.getLidOpenProgressAll())
                blockEntity.lidOpenProgress++;
        } else {
            if (blockEntity.lidOpenProgress > 0)
                blockEntity.lidOpenProgress--;
        }

        if (!level.isClientSide()) {
            blockEntity.setRaisedHandleState(blockEntity.handleRaisedProgress >= blockEntity.getHandleRaisedAll());
            blockEntity.sync();
        }
    }

    @Override
    public CompoundTag getSyncData(ServerPlayer player, CompoundTag tag) {
        tag.putBoolean("HandleRaising", this.handleRaising);
        tag.putBoolean("LidOpen", this.lidOpen);
        return tag;
    }

    @Override
    public void onSync(CompoundTag tag) {
        this.handleRaising = tag.getBoolean("HandleRaising");
        this.lidOpen = tag.getBoolean("LidOpen");
    }

    public int getHandleRaisedAll() {
        return 10;
    }

    public int getHandleRaisedProgress() {
        return handleRaisedProgress;
    }

    public float getHandleRaisedProgress(float partialTicks) {
        return Mth.lerp(partialTicks, handleRaisedProgressOld, handleRaisedProgress);
    }

    public void setRaisedHandleState(boolean raised) {
        var bs = getBlockState().setValue(BoomboxBlock.RAISED, raised);
        getLevel().setBlock(getBlockPos(), bs, 2);
    }

    public boolean isRaisedHandleState() {
        return getBlockState().getValue(BoomboxBlock.RAISED);
    }

    public boolean isHandleRaising() {
        return handleRaising;
    }

    public boolean cycleRaisedHandle() {
        boolean flg = handleRaisedProgress >= getHandleRaisedAll();
        boolean flg2 = handleRaisedProgress <= 0;
        if (!flg && !flg2)
            return false;
        if (flg) {
            handleRaising = false;
        }
        if (flg2) {
            handleRaising = true;
        }
        return true;
    }

    public boolean isLidOpen() {
        return lidOpen;
    }

    public int getLidOpenProgress() {
        return lidOpenProgress;
    }

    public float getLidOpenProgress(float partialTicks) {
        return Mth.lerp(partialTicks, lidOpenProgressOld, lidOpenProgress);
    }

    public int getLidOpenProgressAll() {
        return 10;
    }

    public boolean cycleLidOpen() {
        boolean flg = lidOpenProgress >= getLidOpenProgressAll();
        boolean flg2 = lidOpenProgress <= 0;
        if (!flg && !flg2)
            return false;
        if (flg) {
            lidOpen = false;
        }
        if (flg2) {
            lidOpen = true;
        }
        return true;
    }

    public Buttons getButtons() {
        return new Buttons(isPower(), false, false, false, false, false, false, false, false);
    }

    public static record Buttons(boolean power, boolean radio, boolean start, boolean pause, boolean stop,
                                 boolean loop, boolean volUp, boolean volDown, boolean volMute) {
    }
}
