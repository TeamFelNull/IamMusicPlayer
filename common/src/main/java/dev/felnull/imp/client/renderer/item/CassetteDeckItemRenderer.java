package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.renderer.blockentity.CassetteDeckBlockEntityRenderer;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class CassetteDeckItemRenderer implements BEWLItemRenderer {
    private final CassetteDeckBlockEntity entity = new CassetteDeckBlockEntity(BlockPos.ZERO, IMPBlocks.CASSETTE_DECK.defaultBlockState());

    @Override
    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, float v, int i, int i1) {
        var model = OERenderUtil.getBlockModel(entity.getBlockState());
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        OERenderUtil.renderModel(poseStack, vc, model, i, i1);
        CassetteDeckBlockEntityRenderer.renderCassetteDeck(entity, poseStack, multiBufferSource, i, i1, v);
    }
}
