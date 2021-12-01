package dev.felnull.imp.blockentity;

import dev.felnull.imp.block.BoomboxBlock;
import dev.felnull.imp.block.IMPBlocks;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BoomboxBlockEntity extends IMPBaseEntityBlockEntity {
    protected int handleRaisedProgress = 20;

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
        this.handleRaisedProgress = tag.getInt("HandleRaisedTime");
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putInt("HandleRaisedTime", this.handleRaisedProgress);
        return tag;
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BoomboxBlockEntity blockEntity) {
        if (!level.isClientSide()) {
            boolean raised = blockState.getValue(BoomboxBlock.RAISED);
            if (raised) {
                if (blockEntity.handleRaisedProgress < blockEntity.getHandleRaisedAll()) {
                    blockEntity.handleRaisedProgress++;
                    blockEntity.sync();
                }
            } else {
                if (blockEntity.handleRaisedProgress > 0) {
                    blockEntity.handleRaisedProgress--;
                    blockEntity.sync();
                }
            }
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(getBlockPos(), 4, this.getUpdateTag());
    }

    @Override
    public CompoundTag getSyncData(ServerPlayer player, CompoundTag tag) {
        tag.putInt("HandleRaisedTime", this.handleRaisedProgress);
        return tag;
    }

    @Override
    public void onSync(CompoundTag tag) {
        this.handleRaisedProgress = tag.getInt("HandleRaisedTime");
    }

    public int getHandleRaisedAll() {
        return 10;
    }

    public int getHandleRaisedProgress() {
        return handleRaisedProgress;
    }
}
