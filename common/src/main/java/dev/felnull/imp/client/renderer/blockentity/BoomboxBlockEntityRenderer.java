package dev.felnull.imp.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.MusicManagerBlock;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.model.SpecialModelLoader;
import dev.felnull.otyacraftengine.client.renderer.blockentity.AbstractBlockEntityRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BoomboxBlockEntityRenderer extends AbstractBlockEntityRenderer<BoomboxBlockEntity> {

    protected BoomboxBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BoomboxBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        float handleRaised = (float) blockEntity.getHandleRaisedProgress() / (float) blockEntity.getHandleRaisedAll();
        renderBoombox(blockEntity, poseStack, multiBufferSource, i, j, f, handleRaised);
    }

    public static void renderBoombox(BoomboxBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float handleRaised) {
        var spml = SpecialModelLoader.getInstance();
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        var handle = spml.getModel(IMPModels.BOOMBOX_HANDLE);

        poseStack.pushPose();
        OERenderUtil.poseRotateDirection(poseStack, blockEntity.getBlockState().getValue(MusicManagerBlock.FACING), 1);
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 1, 8, 6);
        OERenderUtil.poseTrans16(poseStack, 0.5, 0.5, 0.5);
        OERenderUtil.poseRotateX(poseStack, (1f - handleRaised) * 90f);
        OERenderUtil.poseTrans16(poseStack, -0.5, -0.5, -0.5);
        OERenderUtil.renderModel(poseStack, vc, handle, i, j);
        poseStack.popPose();
        poseStack.popPose();
    }
}
