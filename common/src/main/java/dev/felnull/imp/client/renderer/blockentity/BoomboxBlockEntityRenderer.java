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
        float handleRaised = blockEntity.getHandleRaisedProgress(f) / (float) blockEntity.getHandleRaisedAll();
        float lidOpen = blockEntity.getLidOpenProgress(f) / (float) blockEntity.getLidOpenProgressAll();
        renderBoombox(blockEntity, poseStack, multiBufferSource, i, j, f, handleRaised, lidOpen, blockEntity.getButtons());
    }

    public static void renderBoombox(BoomboxBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float handleRaised, float lidOpen, BoomboxBlockEntity.Buttons buttons) {
        var spml = SpecialModelLoader.getInstance();
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        var handleM = spml.getModel(IMPModels.BOOMBOX_HANDLE);
        var lidM = spml.getModel(IMPModels.BOOMBOX_LID);
        var buttonsM = spml.getModel(IMPModels.BOOMBOX_BUTTONS);

        poseStack.pushPose();
        OERenderUtil.poseRotateDirection(poseStack, blockEntity.getBlockState().getValue(MusicManagerBlock.FACING), 1);

        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 1, 8, 6);
        OERenderUtil.poseTrans16(poseStack, 0.5, 0.5, 0.5);
        OERenderUtil.poseRotateX(poseStack, (1f - handleRaised) * 90f);
        OERenderUtil.poseTrans16(poseStack, -0.5, -0.5, -0.5);
        OERenderUtil.renderModel(poseStack, vc, handleM, i, j);
        poseStack.popPose();

        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 5.5, 1.5, 5);
        OERenderUtil.poseTrans16(poseStack, 0.125, 0.125, 0.125);
        OERenderUtil.poseRotateX(poseStack, lidOpen * -40f);
        OERenderUtil.poseTrans16(poseStack, -0.125, -0.125, -0.125);
        OERenderUtil.renderModel(poseStack, vc, lidM, i, j);
        poseStack.popPose();

        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 12.25, 9 - (buttons.power() ? 0.5 : 0), 5.75);
        OERenderUtil.renderModel(poseStack, vc, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 11.25, 9 - (buttons.radio() ? 0.5 : 0), 5.75);
        OERenderUtil.renderModel(poseStack, vc, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 10.25, 9 - (buttons.start() ? 0.5 : 0), 5.75);
        OERenderUtil.renderModel(poseStack, vc, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 9.25, 9 - (buttons.pause() ? 0.5 : 0), 5.75);
        OERenderUtil.renderModel(poseStack, vc, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 8.25, 9 - (buttons.stop() ? 0.5 : 0), 5.75);
        OERenderUtil.renderModel(poseStack, vc, buttonsM, i, j);
        poseStack.popPose();

        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 5.95, 9 - (buttons.loop() ? 0.5 : 0), 5.75);
        OERenderUtil.renderModel(poseStack, vc, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 4.95, 9 - (buttons.volUp() ? 0.5 : 0), 5.75);
        OERenderUtil.renderModel(poseStack, vc, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 3.95, 9 - (buttons.volDown() ? 0.5 : 0), 5.75);
        OERenderUtil.renderModel(poseStack, vc, buttonsM, i, j);
        poseStack.popPose();
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 2.95, 9 - (buttons.volMute() ? 0.5 : 0), 5.75);
        OERenderUtil.renderModel(poseStack, vc, buttonsM, i, j);
        poseStack.popPose();


        poseStack.popPose();
    }


}
