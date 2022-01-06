package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.client.music.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.renderer.blockentity.BoomboxBlockEntityRenderer;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class BoomboxItemRenderer implements BEWLItemRenderer {
    private final BoomboxBlockEntity entity = new BoomboxBlockEntity(BlockPos.ZERO, IMPBlocks.BOOMBOX.defaultBlockState());

    @Override
    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        var model = OERenderUtil.getBlockModel(entity.getBlockState());
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        OERenderUtil.renderModel(poseStack, vc, model, i, i1);
        var buttons = new BoomboxBlockEntity.Buttons(false, false, false, false, false, false, false, false, false);
        BoomboxBlockEntityRenderer.renderBoombox(entity, poseStack, multiBufferSource, i, i1, 0, 1, 0, buttons);
    }
}
