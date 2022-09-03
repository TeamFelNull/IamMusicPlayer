package dev.felnull.imp.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.MusicManagerBlock;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.renderer.blockentity.AbstractBlockEntityRenderer;
import dev.felnull.otyacraftengine.client.util.OEModelUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MusicManagerBlockEntityRenderer extends AbstractBlockEntityRenderer<MusicManagerBlockEntity> {
    private static final Map<MusicManagerBlockEntity.MonitorType, MusicManagerMonitor> monitors = new HashMap<>();
    private static final Minecraft mc = Minecraft.getInstance();
    private static final Random random = new Random();

    protected MusicManagerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MusicManagerBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        renderMusicManager(blockEntity, poseStack, multiBufferSource, i, j, f);
    }

    public static void renderMusicManager(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f) {
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        var acLmp = OEModelUtils.getModel(IMPModels.MUSIC_MANAGER_OFF_ACCESS_LAMP);

        if (blockEntity.isPowered() && random.nextBoolean())
            acLmp = OEModelUtils.getModel(IMPModels.MUSIC_MANAGER_ACCESS_LAMP);

        poseStack.pushPose();
        OERenderUtils.poseRotateDirection(poseStack, blockEntity.getBlockState().getValue(MusicManagerBlock.FACING), 1);

        poseStack.pushPose();
        OERenderUtils.renderModel(poseStack, vc, acLmp, i, j);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(1, 0, 0);
        OERenderUtils.poseRotateY(poseStack, 180);
        OERenderUtils.poseTrans16(poseStack, 0.25f, 1.75f, -12.375f);
        var monitor = getMonitor(blockEntity.getMyMonitor(mc.player));
        float px16 = 1f / 16f;
        monitor.renderAppearance(blockEntity, poseStack, multiBufferSource, LightTexture.FULL_BRIGHT, j, f, px16 * 10.5f, px16 * 6.5f);
        poseStack.popPose();

        poseStack.popPose();
    }

    private static MusicManagerMonitor getMonitor(MusicManagerBlockEntity.MonitorType type) {
        if (monitors.containsKey(type))
            return monitors.get(type);

        var monitor = MusicManagerMonitor.createdMusicMonitor(type, null);
        monitors.put(type, monitor);
        return monitor;
    }
}
