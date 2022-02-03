package dev.felnull.imp.item;

import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.inventory.BoomboxMenu;
import dev.felnull.otyacraftengine.item.IInstructionItem;
import dev.felnull.otyacraftengine.item.ItemContainer;
import dev.felnull.otyacraftengine.item.location.HandItemLocation;
import dev.felnull.otyacraftengine.util.OEMenuUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

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
            return InteractionResultHolder.pass(itemStack);
        }

        return super.use(level, player, interactionHand);
    }

    public static void tick(Level level, Entity entity, ItemStack stack) {
        if (!level.isClientSide()) {
            if (getUUID(stack) == null)
                setUUID(stack, UUID.randomUUID());
        }
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.getMainHandItem() == stack || livingEntity.getOffhandItem() == stack) {
                boolean power = isPowerOn(stack);
                setTransferProgressOld(stack, getTransferProgress(stack));
                setTransferProgress(stack, getTransferProgress(stack) + (power ? 1 : -1));
            }
        }
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

    public static boolean isLoop(ItemStack itemStack) {
        if (getBoomboxTag(itemStack) != null)
            return getBoomboxTag(itemStack).getBoolean("Loop");
        return false;
    }

    public static void setLoop(ItemStack itemStack, boolean loop) {
        getOrCreateBoomboxTag(itemStack).putBoolean("Loop", loop);
    }

    public static BoomboxBlockEntity.Buttons getButtons(ItemStack stack) {
        return new BoomboxBlockEntity.Buttons(false, false, false, false, isLoop(stack), false, false);
    }

    public static NonNullList<ItemStack> getContainItem(ItemStack stack) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);
        ItemContainer.loadItemList(stack, stacks, "BoomboxItems");
        return stacks;
    }

    public static ItemStack getCassetteTape(ItemStack stack) {
        return getContainItem(stack).get(0);
    }

    public static ItemStack getAntenna(ItemStack stack) {
        return getContainItem(stack).get(1);
    }

    @Override
    public CompoundTag onInstruction(ItemStack itemStack, ServerPlayer player, String name, int num, CompoundTag data) {
        if ("ButtonsPress".equals(name)) {
            BoomboxBlockEntity.ButtonType type = BoomboxBlockEntity.ButtonType.getByName(data.getString("Type"));
            switch (type) {
                case POWER -> setPowerOn(itemStack, !isPowerOn(itemStack));
                case LOOP -> setLoop(itemStack, !isLoop(itemStack));
              /*   case VOL_DOWN -> {
                    if (isPower())
                        setVolume(Math.max(volume - 10, 0));
                }
                case VOL_UP -> {
                    if (isPower())
                        setVolume(Math.min(volume + 10, 200));
                    setMute(false);
                }
                case VOL_MUTE -> setMute(!isMute());
                case VOL_MAX -> {
                    if (isPower())
                        setVolume(200);
                    setMute(false);
                }*/
            }
            return null;
        }
        return null;
    }
}
