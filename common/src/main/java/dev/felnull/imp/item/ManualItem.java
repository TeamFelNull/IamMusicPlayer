package dev.felnull.imp.item;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.integration.PatchouliIntegration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ManualItem extends Item {
    public static final ResourceLocation MANUAL_BOOK = new ResourceLocation(IamMusicPlayer.MODID, "manual");

    public ManualItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (PatchouliIntegration.isEnableIntegration()) {
            var item = player.getItemInHand(interactionHand);
           /* if (!level.isClientSide()) {
                PatchouliWrapper.openBookGUI((ServerPlayer) player, MANUAL_BOOK);
            }*/
            return InteractionResultHolder.success(item);
        }
        return super.use(level, player, interactionHand);
    }
}
