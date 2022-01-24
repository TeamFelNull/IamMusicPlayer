package dev.felnull.imp.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.client.gui.components.MusicSubtitleOverlay;
import dev.felnull.imp.client.renderer.hand.BoomboxHandRenderer;
import dev.felnull.otyacraftengine.api.event.client.RenderPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class RenderHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void init() {
        ClientGuiEvent.RENDER_HUD.register(RenderHandler::onRenderHUD);
        ClientGuiEvent.RENDER_POST.register(RenderHandler::onRenderPost);
        RenderPlayerEvent.RENDER_HAND.register(RenderHandler::onRenderHand);
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

    public static EventResult onRenderHand(PoseStack poseStack, MultiBufferSource multiBufferSource, InteractionHand hand, int packedLight, float partialTicks, float interpolatedPitch, float swingProgress, float equipProgress, ItemStack stack) {
        if (stack.is(IMPBlocks.BOOMBOX.asItem())) {
            BoomboxHandRenderer.render(poseStack, multiBufferSource, hand, packedLight, partialTicks, interpolatedPitch, swingProgress, equipProgress, stack);
            return EventResult.interruptFalse();
        }
        return EventResult.pass();
    }
}
