package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.renderer.blockentity.CassetteDeckBlockEntityRenderer;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OEModelUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class CassetteDeckItemRenderer implements BEWLItemRenderer {
    private final CassetteDeckBlockEntity entity = new CassetteDeckBlockEntity(BlockPos.ZERO, IMPBlocks.CASSETTE_DECK.get().defaultBlockState());

    @Override
    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, float v, int i, int i1) {
        var model = OEModelUtils.getModel(entity.getBlockState());
        var vc = ItemRenderer.getFoilBufferDirect(multiBufferSource, Sheets.cutoutBlockSheet(), true, itemStack.hasFoil());//multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        OERenderUtils.renderModel(poseStack, vc, model, i, i1);
        CassetteDeckBlockEntityRenderer.renderCassetteDeck(entity, poseStack, multiBufferSource, i, i1, v, vc);
    }
}
