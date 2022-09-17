package dev.felnull.imp.item;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.integration.PatchouliIntegration;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManualItem extends Item {
    public static final ResourceLocation MANUAL_BOOK = new ResourceLocation(IamMusicPlayer.MODID, "manual");
    private static final Component INVALID_ITEM = Component.translatable("item.iammusicplayer.invalid_item.desc").withStyle(ChatFormatting.DARK_RED);
    private static final Component INVALID_ITEM_PATCHOULI = Component.translatable("item.iammusicplayer.invalid_item.patchouli.desc");
    private static final Component INVALID_ITEM_PATCHOULI_CONFIG = Component.translatable("item.iammusicplayer.invalid_item.patchouli.config.desc");

    public ManualItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (PatchouliIntegration.INSTANCE.isEnable()) {
            var item = player.getItemInHand(interactionHand);
            if (!level.isClientSide()) {
                PatchouliIntegration.INSTANCE.openBookGUI((ServerPlayer) player, MANUAL_BOOK);
            }
            return InteractionResultHolder.success(item);
        } else {
            if (level.isClientSide())
                player.sendSystemMessage(PatchouliIntegration.INSTANCE.isConfigEnabled() ? INVALID_ITEM_PATCHOULI : INVALID_ITEM_PATCHOULI_CONFIG);
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        if (!PatchouliIntegration.INSTANCE.isEnable()) {
            list.add(INVALID_ITEM);
            list.add(PatchouliIntegration.INSTANCE.isConfigEnabled() ? INVALID_ITEM_PATCHOULI : INVALID_ITEM_PATCHOULI_CONFIG);
        }
    }
}
