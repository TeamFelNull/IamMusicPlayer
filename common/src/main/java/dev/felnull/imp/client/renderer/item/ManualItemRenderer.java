package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.architectury.platform.Platform;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class ManualItemRenderer implements BEWLItemRenderer {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final Component FELNULL_DEV_TEXT = Component.literal("FelNull DEV");
    private static final Component MANUAL_TEXT = Component.translatable("imp.text.manual");
    private static final Component COVER_INFO_TEXT = Component.translatable("imp.text.manual.coverInfo");
    private final Component MOD_NAME_TEXT;
    private final Component MOD_VERSION_TEXT;
    private int openProgress;
    private int openProgressO;

    public ManualItemRenderer() {
        var mod = Platform.getMod(IamMusicPlayer.MODID);
        this.MOD_NAME_TEXT = Component.literal(mod.getName());
        this.MOD_VERSION_TEXT = Component.literal("v" + mod.getVersion());
    }

    @Override
    public void render(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, float f, int light, int overlay) {
        var model = IMPModels.MANUAL.get();
        var vc = ItemRenderer.getFoilBufferDirect(multiBufferSource, Sheets.solidBlockSheet(), true, stack.hasFoil()); //multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        float par = Mth.lerp(f, openProgressO, openProgress) / 10f;
        poseStack.pushPose();
        if (transformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
            OERenderUtils.poseTrans16(poseStack, 11, 0, 0);
            OERenderUtils.poseRotateZ(poseStack, -25 * par);
            OERenderUtils.poseTrans16(poseStack, -11, 0, 0);
        }

        OERenderUtils.renderModel(poseStack, vc, model, light, overlay);

        if (transformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
            OERenderUtils.poseTrans16(poseStack, 0, 0.125 * par, 0);
            OERenderUtils.poseTrans16(poseStack, 10, 0.125, 0);
            OERenderUtils.poseRotateZ(poseStack, -135 * par);
            OERenderUtils.poseTrans16(poseStack, -10, -0.125, 0);
        }

        OERenderUtils.poseTrans16(poseStack, 0, 0.125, 0);
        renderTurning(poseStack, multiBufferSource, vc, light, overlay);
        poseStack.popPose();
    }

    public void tick() {
        if (mc.level == null || mc.player == null) {
            openProgress = 0;
            openProgressO = 0;
            return;
        }
        if (mc.isPaused()) return;
        openProgressO = openProgress;
      /*  if (PatchouliIntegration.isEnableIntegration() && ManualItem.MANUAL_BOOK.equals(PatchouliWrapper.getOpenBookGui())) {
            openProgress = Math.min(openProgress + 1, 10);
        } else {
            openProgress = Math.max(openProgress - 1, 0);
        }*/
    }

    private void renderTurning(PoseStack poseStack, MultiBufferSource multiBufferSource, VertexConsumer vc, int light, int overlay) {
        var model = IMPModels.MANUAL_TURNING.get();
        poseStack.pushPose();
        OERenderUtils.renderModel(poseStack, vc, model, light, overlay);
        renderText(poseStack, multiBufferSource, FELNULL_DEV_TEXT, light, 9f, 15.75f, 0.4f, false, 0);
        renderText(poseStack, multiBufferSource, MANUAL_TEXT, light, 4.225f / 2f, 15.75f, 0.54f, true, 0xFFFFFFFF);
        renderText(poseStack, multiBufferSource, MOD_NAME_TEXT, light, 9.85f, 14.5f, 0.7f, false, 0);
        renderText(poseStack, multiBufferSource, MOD_VERSION_TEXT, light, 9.85f, 13.5f, 0.4f, false, 0);
        renderText(poseStack, multiBufferSource, COVER_INFO_TEXT, light, 11f / 2f, 12, 0.4f, true, 0);
        poseStack.popPose();
    }

    private void renderText(PoseStack poseStack, MultiBufferSource multiBufferSource, Component text, int light, float x, float y, float scale, boolean center, int color) {
        poseStack.pushPose();
        poseStack.translate(0, OERenderUtils.MIN_BREADTH, 0);
        OERenderUtils.poseTrans16(poseStack, x, 0.125, y);
        OERenderUtils.poseRotateX(poseStack, -90f);
        OERenderUtils.poseRotateZ(poseStack, 180f);
        if (center) {
            OERenderUtils.renderCenterTextSprite(poseStack, multiBufferSource, text, 0, 0, 0, scale, 0, mc.font.lineHeight, color, light);
        } else {
            OERenderUtils.renderTextSprite(poseStack, multiBufferSource, text, 0, 0, 0, scale, 0, mc.font.lineHeight, color, light);
        }
        poseStack.popPose();
    }
}
