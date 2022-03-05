package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.renderer.blockentity.MusicManagerBlockEntityRenderer;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OEModelUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class MusicManagerItemRenderer implements BEWLItemRenderer {
    private final MusicManagerBlockEntity entity = new MusicManagerBlockEntity(BlockPos.ZERO, IMPBlocks.MUSIC_MANAGER.get().defaultBlockState());

    @Override
    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, float f, int i, int i1) {
        var model = OEModelUtil.getModel(entity.getBlockState());
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        OERenderUtil.renderModel(poseStack, vc, model, i, i1);
        MusicManagerBlockEntityRenderer.renderMusicManager(entity, poseStack, multiBufferSource, i, i1, 0);
    }
}
