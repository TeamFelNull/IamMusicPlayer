package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OEModelUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class ParabolicAntennaItemRenderer implements BEWLItemRenderer {
    @Override
    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, float f, int i, int i1) {

        var antenaName = IMPModels.PARABOLIC_ANTENNA;
        var name = itemStack.getHoverName().getString();
        if (name.equalsIgnoreCase("kamesuta")) {
            antenaName = IMPModels.KAMESUTA_ANTENNA;
        } else if (name.equalsIgnoreCase("ikisugi")) {
            antenaName = IMPModels.IKISUGI_ANTENNA;
        } else if (name.equalsIgnoreCase("f.c.o.h") || name.equalsIgnoreCase("fcoh")) {
            antenaName = IMPModels.FCOH_ANTENNA;
        } else if (name.equalsIgnoreCase("katyou")) {
            antenaName = IMPModels.KATYOU_ANTENNA;
        }
        var plM = OEModelUtil.getModel(antenaName);
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        poseStack.pushPose();
        if (transformType == ItemTransforms.TransformType.HEAD) {
            long time = System.currentTimeMillis();
            OERenderUtil.poseRotateY(poseStack, (float) (time % 5000) / 5000f * 360f);
            poseStack.translate(0, -5, 0);
            OERenderUtil.poseRotateX(poseStack, -15f + Math.abs(-1f + ((float) (time % 3000) / 1500)) * 30);
            poseStack.translate(0, 5, 0);

            OERenderUtil.poseScaleAll(poseStack, 2f);
            poseStack.translate(0, 1.45f, 0);
        }
        OERenderUtil.renderModel(poseStack, vc, plM, i, i1);
        poseStack.popPose();
    }
}
