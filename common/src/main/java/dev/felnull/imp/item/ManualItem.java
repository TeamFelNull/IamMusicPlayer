package dev.felnull.imp.item;

import dev.architectury.platform.Platform;
import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.PatchouliAPI;

public class ManualItem extends Item {
    public static final ResourceLocation MANUAL_BOOK = new ResourceLocation(IamMusicPlayer.MODID, "manual");

    public ManualItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (Platform.isModLoaded("patchouli")) {
            var item = player.getItemInHand(interactionHand);
            if (!level.isClientSide()) {
                PatchouliAPI.get().openBookGUI((ServerPlayer) player, MANUAL_BOOK);
            }
            return InteractionResultHolder.success(item);
        }
        return super.use(level, player, interactionHand);
    }
}
