package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IMPHoliday;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ParabolicAntennaItemRenderer implements BEWLItemRenderer {
    @Override
    public void render(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, float f, int i, int i1) {
        var antenaModel = IMPModels.PARABOLIC_ANTENNA;

        if (IMPHoliday.isXmas() && IMPModels.XMAS_ANTENNA != null)
            antenaModel = IMPModels.XMAS_ANTENNA;
        else if (IMPHoliday.isAprilFool() && IMPModels.APRIL_FOOL_ANTENNA != null)
            antenaModel = IMPModels.APRIL_FOOL_ANTENNA;

        var name = itemStack.getHoverName().getString();
        if (name.equalsIgnoreCase("kamesuta")) {
            antenaModel = IMPModels.KAMESUTA_ANTENNA;
        } else if (name.equalsIgnoreCase("ikisugi")) {
            antenaModel = IMPModels.IKISUGI_ANTENNA;
        } else if (name.equalsIgnoreCase("f.c.o.h") || name.equalsIgnoreCase("fcoh")) {
            antenaModel = IMPModels.FCOH_ANTENNA;
        } else if (name.equalsIgnoreCase("katyou")) {
            antenaModel = IMPModels.KATYOU_ANTENNA;
        }
        var plM = antenaModel.get();
        var vc = ItemRenderer.getFoilBufferDirect(multiBufferSource, Sheets.solidBlockSheet(), true, itemStack.hasFoil());

        poseStack.pushPose();
        if (displayContext == ItemDisplayContext.HEAD) {
            if (IMPHoliday.isXmas()) {
                OERenderUtils.poseRotateX(poseStack, 180f);
                OERenderUtils.poseScaleAll(poseStack, 3f);
                poseStack.translate(0, 2.5f, 0);
            } else {
                long time = System.currentTimeMillis();
                OERenderUtils.poseRotateY(poseStack, (float) (time % 5000) / 5000f * 360f);
                poseStack.translate(0, -5, 0);
                OERenderUtils.poseRotateX(poseStack, -15f + Math.abs(-1f + ((float) (time % 3000) / 1500)) * 30);
                poseStack.translate(0, 5, 0);

                OERenderUtils.poseScaleAll(poseStack, 2f);
                poseStack.translate(0, 1.45f, 0);
            }
        }
        OERenderUtils.renderModel(poseStack, vc, plM, i, i1);

        if (IMPHoliday.isXmas()) {
            if (IMPModels.XMAS_ANTENNA_SIDE != null)
                OERenderUtils.renderModel(poseStack, vc, IMPModels.XMAS_ANTENNA_SIDE.get(), i, i1);

            if (displayContext == ItemDisplayContext.HEAD && IMPModels.XMAS_ANTENNA_TAMA != null)
                OERenderUtils.renderModel(poseStack, vc, IMPModels.XMAS_ANTENNA_TAMA.get(), i, i1);
        }

        poseStack.popPose();
    }
}
