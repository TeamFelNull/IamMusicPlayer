package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.model.SpecialModelLoader;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class ParabolicAntennaItemRenderer implements BEWLItemRenderer {
    @Override
    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        var spml = SpecialModelLoader.getInstance();
        var plM = spml.getModel(IMPModels.PARABOLIC_ANTENNA);
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        OERenderUtil.renderModel(poseStack, vc, plM, i, i1);
    }
}
