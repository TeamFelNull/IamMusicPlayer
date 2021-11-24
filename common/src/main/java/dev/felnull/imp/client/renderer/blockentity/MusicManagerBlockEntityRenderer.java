package dev.felnull.imp.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Blocks;

public class MusicManagerBlockEntityRenderer implements BlockEntityRenderer<MusicManagerBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public MusicManagerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(MusicManagerBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        renderMM(poseStack, multiBufferSource, i, j, f);
    }

    public static void renderMM(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f) {
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        var model = OERenderUtil.getBlockModel(Blocks.HOPPER.defaultBlockState());
        OERenderUtil.renderModel(poseStack, vc, model, i, j);
    }
}
