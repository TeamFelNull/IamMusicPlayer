package dev.felnull.imp.item;

import dev.felnull.imp.client.music.MusicTest;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class SoundTestItem extends Item {
    private static final UUID ID = UUID.randomUUID();

    public SoundTestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (level.isClientSide()) {
            System.out.println("Sound test");

            if (!player.isCrouching()) {
                try {
                    MusicTest.test6(player, itemStack.getHoverName().getString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    MusicTest.test5(player, itemStack.getHoverName().getString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
    }
}
