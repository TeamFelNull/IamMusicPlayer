package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.model.SpecialModelLoader;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public class AntennaItemRenderer implements BEWLItemRenderer {
    @Override
    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, float v, int i, int i1) {
        renderAntenna(poseStack, multiBufferSource, i, i1, 0f, 0f);
    }

    public static void renderAntenna(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float par, float rote) {
        VertexConsumer ivb = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        var spml = SpecialModelLoader.getInstance();

        BakedModel antennaModel = spml.getModel(IMPModels.ANTENNA);
        BakedModel antennaTopModel = spml.getModel(IMPModels.ANTENNA_TOP);
        BakedModel antennaRootModel = spml.getModel(IMPModels.ANTENNA_ROOT);

        float ws = 0.025f / 2f;

        poseStack.pushPose();
        poseStack.pushPose();
        poseStack.scale(0.75f, 1f, 1f);
        OERenderUtil.renderModel(poseStack, ivb, antennaRootModel, i, j);
        poseStack.popPose();

        OERenderUtil.poseTrans16(poseStack, 0.7f, 0, 0);
        poseStack.translate(0, ws, ws);
        OERenderUtil.poseRotateZ(poseStack, rote);
        poseStack.translate(0, -ws, -ws);

        poseStack.pushPose();
        poseStack.scale(0.75f, 1f, 1f);
        OERenderUtil.renderModel(poseStack, ivb, antennaModel, i, j);
        poseStack.popPose();

        poseStack.pushPose();

        OERenderUtil.poseTrans16(poseStack, 11f - (10.5f * (1f - Math.min(par, 0.5f) * 2f)), 0, 0);

        poseStack.pushPose();
        poseStack.translate(0, ws, ws);
        poseStack.scale(0.75f, 0.75f, 0.75f);
        poseStack.translate(0, -ws, -ws);
        OERenderUtil.renderModel(poseStack, ivb, antennaModel, i, j);
        poseStack.popPose();

        OERenderUtil.poseTrans16(poseStack, 11f - (10.5f * (1f - Math.max(par - 0.5f, 0) * 2f)), 0, 0);

        poseStack.pushPose();
        poseStack.translate(0, ws, ws);
        poseStack.scale(0.75f, 0.5625f, 0.5625f);
        poseStack.translate(0, -ws, -ws);
        OERenderUtil.renderModel(poseStack, ivb, antennaModel, i, j);
        poseStack.popPose();
        OERenderUtil.poseTrans16(poseStack, 11.8f, 0, 0);
        OERenderUtil.renderModel(poseStack, ivb, antennaTopModel, i, j);
        poseStack.popPose();
        poseStack.popPose();
    }
}
