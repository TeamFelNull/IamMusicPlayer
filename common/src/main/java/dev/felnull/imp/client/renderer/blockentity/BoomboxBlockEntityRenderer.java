package dev.felnull.imp.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.MusicManagerBlock;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.model.SpecialModelLoader;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BoomboxBlockEntityRenderer implements BlockEntityRenderer<BoomboxBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public BoomboxBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(BoomboxBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        renderBoombox(blockEntity, poseStack, multiBufferSource, i, j, f);
    }

    public static void renderBoombox(BoomboxBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f) {
        var spml = SpecialModelLoader.getInstance();
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        var handle = spml.getModel(IMPModels.BOOMBOX_HANDLE);

        poseStack.pushPose();
        OERenderUtil.poseRotateDirection(poseStack, blockEntity.getBlockState().getValue(MusicManagerBlock.FACING), 1);
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 1, 8, 6);
        OERenderUtil.poseTrans16(poseStack, 0.5, 0.5, 0.5);
        OERenderUtil.poseRotateX(poseStack, (float) (System.currentTimeMillis() % 1000) / 1000f * 90f);
        OERenderUtil.poseTrans16(poseStack, -0.5, -0.5, -0.5);
        OERenderUtil.renderModel(poseStack, vc, handle, i, j);
        poseStack.popPose();
        poseStack.popPose();
    }
}
