package dev.felnull.imp.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.CassetteDeckBlock;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.model.SpecialModelLoader;
import dev.felnull.otyacraftengine.client.renderer.blockentity.AbstractBlockEntityRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CassetteDeckBlockEntityRenderer extends AbstractBlockEntityRenderer<CassetteDeckBlockEntity> {
    private static final Minecraft mc = Minecraft.getInstance();

    protected CassetteDeckBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CassetteDeckBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        renderCassetteDeck(blockEntity, poseStack, multiBufferSource, i, j, f);
    }

    public static void renderCassetteDeck(CassetteDeckBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f) {
        var spml = SpecialModelLoader.getInstance();
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        var lidM = spml.getModel(IMPModels.CASSETTE_DECK_LID);

        float lidOpen = blockEntity.getLidOpenProgress(f) / (float) blockEntity.getLidOpenProgressAll();

        poseStack.pushPose();
        OERenderUtil.poseRotateDirection(poseStack, blockEntity.getBlockState().getValue(CassetteDeckBlock.FACING), 1);

        if (lidOpen != 0) {
            poseStack.pushPose();
            OERenderUtil.poseTrans16(poseStack, 3.7, 2.225, 3f);
            OERenderUtil.poseScaleAll(poseStack, 0.72f);
            mc.getItemRenderer().renderStatic(blockEntity.isChangeCassetteTape() ? blockEntity.getOldCassetteTape() : blockEntity.getCassetteTape(), ItemTransforms.TransformType.FIXED, i, j, poseStack, multiBufferSource, 0);
            poseStack.popPose();
        }

        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 1.5f, 0.5f, 2f);
        OERenderUtil.poseTrans16(poseStack, 0.125, 0.125, 0.125);
        OERenderUtil.poseRotateX(poseStack, lidOpen * -40f);
        OERenderUtil.poseTrans16(poseStack, -0.125, -0.125, -0.125);
        OERenderUtil.renderModel(poseStack, vc, lidM, i, j);
        poseStack.popPose();

        poseStack.popPose();
    }
}
