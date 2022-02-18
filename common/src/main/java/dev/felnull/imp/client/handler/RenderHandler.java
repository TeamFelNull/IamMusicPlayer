package dev.felnull.imp.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.client.gui.components.MusicSubtitleOverlay;
import dev.felnull.imp.client.renderer.item.hand.BoomboxHandRenderer;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.otyacraftengine.api.event.client.MoreRenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class RenderHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void init() {
        ClientGuiEvent.RENDER_HUD.register(RenderHandler::onRenderHUD);
        ClientGuiEvent.RENDER_POST.register(RenderHandler::onRenderPost);
        MoreRenderEvent.RENDER_ITEM_IN_HAND.register(RenderHandler::onRenderItemInHand);
        MoreRenderEvent.RENDER_ARM_WITH_ITEM.register(RenderHandler::onRenderArmWithItem);
    }

    private static void onRenderHUD(PoseStack poseStack, float v) {
        if (mc.screen == null) {
            poseStack.pushPose();
            poseStack.translate(0, 0, 1000);
            MusicSubtitleOverlay.SUBTITLE_OVERLAY.render(poseStack, 0, 0, v);
            poseStack.popPose();
        }
    }

    private static void onRenderPost(Screen screen, PoseStack poseStack, int i, int i1, float v) {
        poseStack.pushPose();
        poseStack.translate(0, 0, 1000);
        MusicSubtitleOverlay.SUBTITLE_OVERLAY.render(poseStack, i, i1, v);
        poseStack.popPose();
    }

    public static EventResult onRenderItemInHand(PoseStack poseStack, MultiBufferSource multiBufferSource, InteractionHand hand, int packedLight, float partialTicks, float interpolatedPitch, float swingProgress, float equipProgress, ItemStack stack) {
        if (stack.is(IMPBlocks.BOOMBOX.asItem())) {
            BoomboxHandRenderer.render(poseStack, multiBufferSource, hand, packedLight, partialTicks, interpolatedPitch, swingProgress, equipProgress, stack);
            return EventResult.interruptFalse();
        }
        return EventResult.pass();
    }

    public static EventResult onRenderArmWithItem(ItemInHandLayer<? extends LivingEntity, ? extends EntityModel<?>> itemInHandLayer, LivingEntity livingEntity, ItemStack itemStack, ItemTransforms.TransformType transformType, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        if (itemStack.is(IMPBlocks.BOOMBOX.asItem()) && BoomboxItem.getTransferProgress(itemStack) >= 1f) {
            BoomboxHandRenderer.renderArmWithItem(itemInHandLayer, livingEntity, itemStack, transformType, humanoidArm, poseStack, multiBufferSource, i);
            return EventResult.interruptFalse();
        }
        return EventResult.pass();
    }
}
