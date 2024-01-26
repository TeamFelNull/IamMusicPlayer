package dev.felnull.imp.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.CassetteDeckBlock;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.gui.screen.monitor.cassette_deck.CassetteDeckMonitor;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.renderer.blockentity.AbstractBlockEntityRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import java.util.HashMap;
import java.util.Map;

public class CassetteDeckBlockEntityRenderer extends AbstractBlockEntityRenderer<CassetteDeckBlockEntity> {
    private static final Map<CassetteDeckBlockEntity.MonitorType, CassetteDeckMonitor> monitors = new HashMap<>();
    private static final Minecraft mc = Minecraft.getInstance();

    protected CassetteDeckBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CassetteDeckBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        renderCassetteDeck(blockEntity, poseStack, multiBufferSource, i, j, f, multiBufferSource.getBuffer(Sheets.cutoutBlockSheet()));
    }

    public static void renderCassetteDeck(CassetteDeckBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, VertexConsumer vertexConsumer) {
        var lidM = IMPModels.CASSETTE_DECK_LID.get();

        float lidOpen = blockEntity.getLidOpenProgress(f) / (float) blockEntity.getLidOpenProgressAll();

        poseStack.pushPose();
        OERenderUtils.poseRotateDirection(poseStack, blockEntity.getBlockState().getValue(CassetteDeckBlock.FACING), 1);

        if (lidOpen != 0) {
            poseStack.pushPose();
            OERenderUtils.poseTrans16(poseStack, 3.7, 2.225, 3f);
            OERenderUtils.poseScaleAll(poseStack, 0.72f);
            mc.getItemRenderer().renderStatic(blockEntity.isChangeCassetteTape() ? blockEntity.getOldCassetteTape() : blockEntity.getCassetteTape(), ItemTransforms.TransformType.FIXED, i, j, poseStack, multiBufferSource, 0);
            poseStack.popPose();
        }

        if (!IamMusicPlayer.CONFIG.hideDisplaySprite) {
            poseStack.pushPose();
            poseStack.translate(1, 0, 0);
            OERenderUtils.poseRotateY(poseStack, 180);
            OERenderUtils.poseTrans16(poseStack, 0.6f, 2.35f, -1.9f);
            var monitor = getMonitor(blockEntity.getMonitor());
            float px16 = 1f / 16f;
            monitor.renderAppearance(blockEntity, poseStack, multiBufferSource, LightTexture.FULL_BRIGHT, j, f, px16 * 7.8f, px16 * 2.275f);
            poseStack.popPose();
        }

        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 1.5f, 0.5f, 2f);
        OERenderUtils.poseTrans16(poseStack, 0.125, 0.125, 0.125);
        OERenderUtils.poseRotateX(poseStack, lidOpen * -40f);
        OERenderUtils.poseTrans16(poseStack, -0.125, -0.125, -0.125);
        OERenderUtils.renderModel(poseStack, vertexConsumer, lidM, i, j);
        poseStack.popPose();

        poseStack.popPose();
    }

    private static CassetteDeckMonitor getMonitor(CassetteDeckBlockEntity.MonitorType type) {
        if (monitors.containsKey(type))
            return monitors.get(type);

        var monitor = CassetteDeckMonitor.createdCassetteDeckMonitor(type, null);
        monitors.put(type, monitor);
        return monitor;
    }
}
