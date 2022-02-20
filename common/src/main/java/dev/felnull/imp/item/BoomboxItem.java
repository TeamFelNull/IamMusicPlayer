package dev.felnull.imp.item;

import dev.felnull.imp.block.BoomboxBlock;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.data.BoomboxData;
import dev.felnull.imp.inventory.BoomboxMenu;
import dev.felnull.imp.server.music.ringer.IMusicRinger;
import dev.felnull.imp.server.music.ringer.MusicRingManager;
import dev.felnull.otyacraftengine.item.IInstructionItem;
import dev.felnull.otyacraftengine.item.ItemContainer;
import dev.felnull.otyacraftengine.item.location.HandItemLocation;
import dev.felnull.otyacraftengine.util.OEMenuUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BoomboxItem extends BlockItem implements IInstructionItem {

    public BoomboxItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
        tick(level, entity, itemStack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (getTransferProgress(itemStack) == 0 || getTransferProgress(itemStack) == 10) {
            if (!level.isClientSide()) {
                if (player.isCrouching()) {
                    setPowerOn(itemStack, !isPowerOn(itemStack));
                } else {
                    if (isPowerOn(itemStack)) {
                        var loc = new HandItemLocation(interactionHand);
                        OEMenuUtil.openItemMenu((ServerPlayer) player, ItemContainer.createMenuProvider(itemStack, loc, 2, "BoomboxItems", BoomboxMenu::new), loc, itemStack, 2);
                    }
                }
            }
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }

        return super.use(level, player, interactionHand);
    }

    @Override
    public InteractionResult place(BlockPlaceContext blockPlaceContext) {
        var itemStack = blockPlaceContext.getItemInHand();
        var player = blockPlaceContext.getPlayer();
        if (player != null && !(!player.isCrouching() && !isPowerOn(itemStack))) {
            if (getTransferProgress(itemStack) == 0 || getTransferProgress(itemStack) == 10) {
                if (!blockPlaceContext.getLevel().isClientSide()) {
                    if (player.isCrouching()) {
                        if (getTransferProgress(itemStack) == 0 || getTransferProgress(itemStack) == 10) {
                            setPowerOn(itemStack, !isPowerOn(itemStack));
                        }
                    } else {
                        if (isPowerOn(itemStack)) {
                            var loc = new HandItemLocation(blockPlaceContext.getHand());
                            OEMenuUtil.openItemMenu((ServerPlayer) player, ItemContainer.createMenuProvider(itemStack, loc, 2, "BoomboxItems", BoomboxMenu::new), loc, itemStack, 2);
                        }
                    }
                }
                return InteractionResult.sidedSuccess(blockPlaceContext.getLevel().isClientSide);
            } else {
                return InteractionResult.FAIL;
            }
        }
        return super.place(blockPlaceContext);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext blockPlaceContext, BlockState blockState) {
        return !isPowerOn(blockPlaceContext.getItemInHand()) && super.canPlace(blockPlaceContext, blockState);
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        if (this.getBlock() instanceof BoomboxBlock) {
            ItemUtils.onContainerDestroyed(itemEntity, getContainItem(itemEntity.getItem()).stream());
        }
        super.onDestroyed(itemEntity);
    }

    public static void tick(Level level, Entity entity, ItemStack stack) {
        if (entity instanceof LivingEntity livingEntity) {
            if (!level.isClientSide()) {
                if (getUUID(stack) == null)
                    setUUID(stack, UUID.randomUUID());

                var mr = MusicRingManager.getInstance();
                var uuid = getUUID(stack);
                if (uuid != null && !mr.isExistRinger((ServerLevel) level, uuid)) {
                    mr.addRinger((ServerLevel) level, new BoomboxItemRinger(livingEntity, uuid));
                }
            }

            var data = getData(stack);
            data.tick(level);
            setData(stack, data);

            if (livingEntity.getMainHandItem() == stack || livingEntity.getOffhandItem() == stack) {
                boolean power = isPowerOn(stack);
                setTransferProgressOld(stack, getTransferProgress(stack));
                setTransferProgress(stack, getTransferProgress(stack) + (power ? 1 : -1));
            }
        }
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos blockPos, Level level, @Nullable Player player, ItemStack itemStack, BlockState blockState) {
        var server = level.getServer();
        if (server != null) {
            var be = level.getBlockEntity(blockPos);
            if (be instanceof BoomboxBlockEntity boomboxBlockEntity) {
                boomboxBlockEntity.setByItem(itemStack);
                boomboxBlockEntity.setChanged();
                super.updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState);
                return true;
            }
        }
        return super.updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState);
    }

    public static boolean matches(ItemStack src, ItemStack target) {
        if (src == target)
            return true;
        var uid = getUUID(src);
        return uid != null && uid.equals(getUUID(target));
    }

    public static CompoundTag getBoomboxTag(ItemStack stack) {
        if (stack.getTag() != null)
            return stack.getTag().getCompound("BoomboxTag");
        return null;
    }

    public static CompoundTag getOrCreateBoomboxTag(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        if (!tag.contains("BoomboxTag"))
            tag.put("BoomboxTag", new CompoundTag());
        return tag.getCompound("BoomboxTag");
    }

    public static BoomboxData getData(ItemStack stack) {
        var data = new BoomboxData(new BoomboxData.DataAccess() {
            @Override
            public ItemStack getCassetteTape() {
                return BoomboxItem.getCassetteTape(stack);
            }

            @Override
            public ItemStack getAntenna() {
                return BoomboxItem.getAntenna(stack);
            }

            @Override
            public boolean isPower() {
                return isPowerOn(stack);
            }

            @Override
            public void setPower(boolean power) {
                BoomboxItem.setPowerOn(stack, power);
            }

            @Override
            public IMusicRinger getRinger() {
                var uuid = getUUID(stack);
                var mr = MusicRingManager.getInstance();
                if (uuid != null)
                    return mr.getRinger(uuid);
                return null;
            }

            @Override
            public Vec3 getPosition() {
                if (getRinger() == null)
                    return Vec3.ZERO;
                return getRinger().getRingerSpatialPosition(getRinger().getRingerLevel());
            }

            @Override
            public void setCassetteTape(ItemStack cassette) {
                BoomboxItem.setCassetteTape(stack, cassette);
            }

            @Override
            public void dataUpdate(BoomboxData data) {
                setData(stack, data);
            }
        });
        var tag = getBoomboxTag(stack);
        if (tag != null)
            data.load(tag.getCompound("BoomBoxData"), true, true);
        return data;
    }

    public static void setData(ItemStack stack, BoomboxData data) {
        getOrCreateBoomboxTag(stack).put("BoomBoxData", data.save(new CompoundTag(), true, true));
    }

    public static boolean isPowerOn(ItemStack itemStack) {
        if (getBoomboxTag(itemStack) != null)
            return getBoomboxTag(itemStack).getBoolean("Power");
        return false;
    }

    public static void setPowerOn(ItemStack itemStack, boolean power) {
        getOrCreateBoomboxTag(itemStack).putBoolean("Power", power);
    }

    public static int getTransferProgress(ItemStack stack) {
        if (getBoomboxTag(stack) != null)
            return getBoomboxTag(stack).getInt("Transfer");
        return 0;
    }

    public static void setTransferProgress(ItemStack stack, int num) {
        getOrCreateBoomboxTag(stack).putInt("Transfer", Mth.clamp(num, 0, 10));
    }

    public static int getTransferProgressOld(ItemStack stack) {
        if (getBoomboxTag(stack) != null)
            return getBoomboxTag(stack).getInt("TransferOld");
        return 0;
    }

    public static void setTransferProgressOld(ItemStack stack, int num) {
        getOrCreateBoomboxTag(stack).putInt("TransferOld", Mth.clamp(num, 0, 10));
    }

    public static float getTransferProgress(ItemStack stack, float partialTicks) {
        return Mth.lerp(partialTicks, BoomboxItem.getTransferProgressOld(stack), BoomboxItem.getTransferProgress(stack)) / 10f;
    }

    public static UUID getUUID(ItemStack stack) {
        if (getBoomboxTag(stack) != null && getBoomboxTag(stack).contains("Identification"))
            return getBoomboxTag(stack).getUUID("Identification");
        return null;
    }

    public static void setUUID(ItemStack stack, UUID id) {
        getOrCreateBoomboxTag(stack).putUUID("Identification", id);
    }

    public static NonNullList<ItemStack> getContainItem(ItemStack stack) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);
        ItemContainer.loadItemList(stack, stacks, "BoomboxItems");
        return stacks;
    }

    public static void setContainItem(ItemStack stack, NonNullList<ItemStack> stacks) {
        ItemContainer.saveItemList(stack, stacks, "BoomboxItems");
    }

    public static void setCassetteTape(ItemStack stack, ItemStack cassette) {
        var itms = getContainItem(stack);
        itms.set(0, cassette);
        setContainItem(stack, itms);
    }

    public static ItemStack getCassetteTape(ItemStack stack) {
        return getContainItem(stack).get(0);
    }

    public static ItemStack getAntenna(ItemStack stack) {
        return getContainItem(stack).get(1);
    }

    @Override
    public CompoundTag onInstruction(ItemStack itemStack, ServerPlayer player, String name, int num, CompoundTag data) {
        return BoomboxItem.getData(itemStack).onInstruction(player, name, num, data);
    }

    public static ItemStack createByBE(BoomboxBlockEntity blockEntity, boolean stopMusic) {
        var itemStack = new ItemStack(IMPBlocks.BOOMBOX);
        setData(itemStack, blockEntity.getBoomboxData());
        var d = getData(itemStack);
        d.setNoChangeCassetteTape(true);
        if (stopMusic) {
            d.setPlaying(false);
            d.setMusicPosition(0);
        }
        setData(itemStack, d);
        setContainItem(itemStack, blockEntity.getItems());
        setPowerOn(itemStack, blockEntity.isPower());
        if (blockEntity.isPower()) {
            setTransferProgress(itemStack, 10);
            setTransferProgressOld(itemStack, 10);
        }
        setUUID(itemStack, UUID.randomUUID());
        return itemStack;
    }
}
