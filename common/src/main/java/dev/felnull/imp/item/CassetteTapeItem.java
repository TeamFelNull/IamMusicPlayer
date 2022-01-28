package dev.felnull.imp.item;

import dev.felnull.imp.music.MusicManager;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CassetteTapeItem extends Item implements DyeableLeatherItem {
    private final BaseType type;

    public CassetteTapeItem(Properties properties, BaseType type) {
        super(properties.stacksTo(1));
        this.type = type;
    }

    public BaseType getType() {
        return type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (!level.isClientSide()) {
            MusicManager mm = MusicManager.getInstance();
            var mu = mm.getSaveData().getMusics().values().stream().findAny().get();
            setMusic(stack, mu);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    public static Music getMusic(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains("Music"))
            return OENbtUtil.readSerializable(stack.getTag(), "Music", new Music());
        return null;
    }

    public static ItemStack setMusic(ItemStack stack, Music music) {
        OENbtUtil.writeSerializable(stack.getOrCreateTag(), "Music", music);
        return stack;
    }

    public static enum BaseType {
        NORMAL, GLASS
    }
}
