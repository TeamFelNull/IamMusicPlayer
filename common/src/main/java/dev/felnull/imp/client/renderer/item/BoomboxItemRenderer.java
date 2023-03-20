package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.BoomboxBlock;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.renderer.blockentity.BoomboxBlockEntityRenderer;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OEModelUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BoomboxItemRenderer implements BEWLItemRenderer {
    private final BoomboxBlockEntity onEntity = new BoomboxBlockEntity(BlockPos.ZERO, IMPBlocks.BOOMBOX.get().defaultBlockState().setValue(BoomboxBlock.POWERED, true));
    private final BoomboxBlockEntity offEntity = new BoomboxBlockEntity(BlockPos.ZERO, IMPBlocks.BOOMBOX.get().defaultBlockState());

    @Override
    public void render(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, float f, int i, int i1) {
        boolean power = BoomboxItem.isPowered(itemStack);
        boolean radio = false;

        var state = power ? onEntity.getBlockState() : offEntity.getBlockState();
        var model = OEModelUtils.getModel(state);
        var vc = ItemRenderer.getFoilBufferDirect(multiBufferSource, Sheets.cutoutBlockSheet(), true, itemStack.hasFoil());//multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        OERenderUtils.renderModel(poseStack, vc, model, i, i1);

        float handleRaised = 1;

        if (displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
            handleRaised = 1f - BoomboxItem.getTransferProgress(itemStack, f);
        }

        BoomboxBlockEntityRenderer.renderBoombox(poseStack, multiBufferSource, state.getValue(BoomboxBlock.FACING), i, i1, f, BoomboxItem.getData(itemStack), handleRaised, vc);
    }
}
