package dev.felnull.imp.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.felnull.imp.client.gui.components.MusicSubtitleOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class RenderHandler {
    private static final MusicSubtitleOverlay SUBTITLE_OVERLAY = new MusicSubtitleOverlay();
    private static final Minecraft mc = Minecraft.getInstance();

    public static void init() {
        ClientGuiEvent.RENDER_HUD.register(RenderHandler::onRenderHUD);
        ClientGuiEvent.RENDER_POST.register(RenderHandler::onRenderPost);
    }

    private static void onRenderHUD(PoseStack poseStack, float v) {
        if (mc.screen == null) {
            poseStack.pushPose();
            poseStack.translate(0, 0, 1000);
            SUBTITLE_OVERLAY.render(poseStack, 0, 0, v);
            poseStack.popPose();
        }
    }

    private static void onRenderPost(Screen screen, PoseStack poseStack, int i, int i1, float v) {
        poseStack.pushPose();
        poseStack.translate(0, 0, 1000);
        SUBTITLE_OVERLAY.render(poseStack, i, i1, v);
        poseStack.popPose();
    }
}
