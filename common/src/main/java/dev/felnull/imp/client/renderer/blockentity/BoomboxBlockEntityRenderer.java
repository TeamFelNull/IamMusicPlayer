package dev.felnull.imp.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.BoomboxData;
import dev.felnull.imp.block.MusicManagerBlock;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.gui.screen.monitor.boombox.BoomboxMonitor;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.imp.client.renderer.item.AntennaItemRenderer;
import dev.felnull.imp.item.IMPItems;
import dev.felnull.imp.util.IMPItemUtil;
import dev.felnull.otyacraftengine.client.renderer.blockentity.AbstractBlockEntityRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BoomboxBlockEntityRenderer extends AbstractBlockEntityRenderer<BoomboxBlockEntity> {
    private static final Map<BoomboxData.MonitorType, BoomboxMonitor> monitors = new HashMap<>();
    private static final Minecraft mc = Minecraft.getInstance();

    protected BoomboxBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BoomboxBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        var state = blockEntity.getBlockState();
        var data = blockEntity.getBoomboxData();
        renderBoombox(poseStack, multiBufferSource, state.getValue(MusicManagerBlock.FACING), i, j, f, data, data.getHandleRaisedProgress(f) / (float) data.getHandleRaisedMax(), multiBufferSource.getBuffer(Sheets.cutoutBlockSheet()));
    }

    public static void renderBoombox(PoseStack poseStack, MultiBufferSource multiBufferSource, Direction direction, int i, int j, float f, BoomboxData data, float handleRaised, VertexConsumer vertexConsumer) {
        float lidOpen = data.getLidOpenProgress(f) / (float) data.getLidOpenProgressMax();
        var buttons = data.getButtons();
        var cassetteTape = data.getCassetteTape();
        var antenna = data.getAntenna();
        float parabolicAntennaRoted = data.getParabolicAntennaProgress(f);
        float antennaPar = data.getAntennaProgress(f) / 30f;
        boolean changeCassetteTape = data.isChangeCassetteTape();
        var oldCassetteTape = data.getOldCassetteTape();

        var handleM = IMPModels.BOOMBOX_HANDLE.get();
        var lidM = IMPModels.BOOMBOX_LID.get();
        var buttonsM = IMPModels.BOOMBOX_BUTTONS.get();

        poseStack.pushPose();
        OERenderUtils.poseRotateDirection(poseStack, direction, 1);

        if (lidOpen != 0) {
            poseStack.pushPose();
            OERenderUtils.poseTrans16(poseStack, 7.7, 3.225, 6);
            OERenderUtils.poseScaleAll(poseStack, 0.72f);
            mc.getItemRenderer().renderStatic(changeCassetteTape ? oldCassetteTape : cassetteTape, ItemDisplayContext.FIXED, i, j, poseStack, multiBufferSource, mc.level, 0);
            poseStack.popPose();
        }

        if (!IamMusicPlayer.getConfig().hideDecorativeAntenna) {
            renderAntenna(poseStack, multiBufferSource, i, j, antenna, parabolicAntennaRoted, antennaPar);
        }

        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 1, 8, 6);
        OERenderUtils.poseTrans16(poseStack, 0.5, 0.5, 0.5);
        OERenderUtils.poseRotateX(poseStack, (1f - handleRaised) * 90f);
        OERenderUtils.poseTrans16(poseStack, -0.5, -0.5, -0.5);
        OERenderUtils.renderModel(poseStack, vertexConsumer, handleM, i, j);
        poseStack.popPose();

        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 5.5, 1.5, 5);
        OERenderUtils.poseTrans16(poseStack, 0.125, 0.125, 0.125);
        OERenderUtils.poseRotateX(poseStack, lidOpen * -40f);
        OERenderUtils.poseTrans16(poseStack, -0.125, -0.125, -0.125);
        OERenderUtils.renderModel(poseStack, vertexConsumer, lidM, i, j);
        poseStack.popPose();

        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 12.25, 9, 5.75);
        OERenderUtils.renderModel(poseStack, vertexConsumer, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 11.25, 9 - (buttons.radio() ? 0.5 : 0), 5.75);
        OERenderUtils.renderModel(poseStack, vertexConsumer, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 10.25, 9 - (buttons.start() ? 0.5 : 0), 5.75);
        OERenderUtils.renderModel(poseStack, vertexConsumer, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 9.25, 9 - (buttons.pause() ? 0.5 : 0), 5.75);
        OERenderUtils.renderModel(poseStack, vertexConsumer, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 8.25, 9, 5.75);
        OERenderUtils.renderModel(poseStack, vertexConsumer, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 7.25, 9 - (buttons.loop() ? 0.5 : 0), 5.75);
        OERenderUtils.renderModel(poseStack, vertexConsumer, buttonsM, i, j);
        poseStack.popPose();

        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 5.55, 9, 5.75);
        OERenderUtils.renderModel(poseStack, vertexConsumer, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 4.55, 9, 5.75);
        OERenderUtils.renderModel(poseStack, vertexConsumer, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 3.55, 9 - (buttons.volMute() ? 0.5 : 0), 5.75);
        OERenderUtils.renderModel(poseStack, vertexConsumer, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtils.poseTrans16(poseStack, 2.55, 9 - (buttons.volMax() ? 0.5 : 0), 5.75);
        OERenderUtils.renderModel(poseStack, vertexConsumer, buttonsM, i, j);
        poseStack.popPose();

        if (!IamMusicPlayer.getConfig().hideDisplaySprite) {
            poseStack.pushPose();
            poseStack.translate(1, 0, 0);
            OERenderUtils.poseRotateY(poseStack, 180);
            OERenderUtils.poseTrans16(poseStack, 0.6, 5.6, -4.9);
            var monitor = getMonitor(data.getMonitorType());
            float px16 = 1f / 16f;
            monitor.renderAppearance(poseStack, multiBufferSource, LightTexture.FULL_BRIGHT, j, f, px16 * 14.8f, px16 * 2.8f, data);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private static void renderAntenna(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, ItemStack antenna, float roted, float antennaPar) {
        if (!IMPItemUtil.isAntenna(antenna)) return;
        if (antenna.is(IMPItems.RADIO_ANTENNA.get())) {
            poseStack.pushPose();
            float ws = 0.025f / 2f;
            OERenderUtils.poseTrans16(poseStack, 0.25, 9, 10.25);
            poseStack.translate(ws, ws, ws);
            OERenderUtils.poseRotateZ(poseStack, 90);
            poseStack.translate(-ws, -ws, -ws);
            OERenderUtils.poseScaleAll(poseStack, 0.75f);
            AntennaItemRenderer.renderAntenna(antenna, poseStack, multiBufferSource, i, j, (-0.5f + Math.max(antennaPar, 0.5f)) * 2f, -90 + 30 * Math.min(antennaPar, 0.5f) * 2f);
            poseStack.popPose();
        } else {
            poseStack.pushPose();
            OERenderUtils.poseTrans16(poseStack, 0.85, 8, 10.1);
            OERenderUtils.poseScaleAll(poseStack, 0.72f);
            OERenderUtils.poseRotateX(poseStack, 35);
            OERenderUtils.poseRotateZ(poseStack, 35);
            OERenderUtils.poseRotateY(poseStack, roted);
            OERenderUtils.poseTrans16(poseStack, 0, 1.3, 0);
            OERenderUtils.poseRotateX(poseStack, -30 + Math.abs(-0.5f + (roted % 120 / 120f)) * 2f * 60);
            OERenderUtils.poseTrans16(poseStack, 0, -1.3, 0);
            mc.getItemRenderer().renderStatic(antenna, ItemDisplayContext.GROUND, i, j, poseStack, multiBufferSource, mc.level, 0);
            poseStack.popPose();
        }
    }

    private static BoomboxMonitor getMonitor(BoomboxData.MonitorType type) {
        if (monitors.containsKey(type)) return monitors.get(type);

        var monitor = BoomboxMonitor.createdBoomBoxMonitor(type, null);
        monitors.put(type, monitor);
        return monitor;
    }
}
