package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.client.renderer.blockentity.MusicManagerBlockEntityRenderer;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class MusicManagerItemRenderer implements BEWLItemRenderer {
    @Override
    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        var model = OERenderUtil.getBlockModel(IMPBlocks.MUSIC_MANAGER.defaultBlockState());
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        OERenderUtil.renderModel(poseStack, vc, model, i, i1);
        MusicManagerBlockEntityRenderer.renderMM(poseStack, multiBufferSource, i, i1, 0);
    }
}
