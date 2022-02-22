package dev.felnull.imp.item;

import dev.felnull.imp.music.resource.Music;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CassetteTapeItem extends Item implements DyeableLeatherItem {
    private final BaseType type;

    public CassetteTapeItem(Properties properties, BaseType type) {
        super(properties.stacksTo(1));
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (getTapePercentage(itemStack) != 0) {
            if (!level.isClientSide())
                setTapePercentage(itemStack, 0f);
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }
        return super.use(level, player, interactionHand);
    }

    public BaseType getType() {
        return type;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        var m = getMusic(itemStack);
        if (m != null)
            list.add(new TextComponent(m.getName()).withStyle(ChatFormatting.GRAY));
    }

    @Nullable
    public static Music getMusic(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains("Music"))
            return OENbtUtil.readSerializable(stack.getTag(), "Music", new Music());
        return null;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (getMusic(stack) != null)
            return new TranslatableComponent(this.getDescriptionId(stack) + ".written");
        return super.getName(stack);
    }

    public static ItemStack setMusic(ItemStack stack, Music music) {
        OENbtUtil.writeSerializable(stack.getOrCreateTag(), "Music", music);
        return stack;
    }

    public static float getTapePercentage(ItemStack stack) {
        if (stack.getTag() != null)
            return stack.getTag().getFloat("TapePercentage");
        return 0;
    }

    public static ItemStack setTapePercentage(ItemStack stack, float par) {
        stack.getOrCreateTag().putFloat("TapePercentage", par);
        return stack;
    }

    public static boolean isSameCassetteTape(ItemStack stack, ItemStack stack2) {
        if (ItemStack.matches(stack, stack2)) return true;
        if (!stack.is(stack2.getItem())) return false;
        if (stack.getItem() instanceof CassetteTapeItem && stack2.getItem() instanceof CassetteTapeItem) {
            var m1 = getMusic(stack);
            var m2 = getMusic(stack2);
            if (m1 == null && m2 == null) return true;
            if (m1 == null || m2 == null) return false;
            return m1.equals(m2);
        }
        return false;
    }

    public static enum BaseType {
        NORMAL, GLASS
    }
}
