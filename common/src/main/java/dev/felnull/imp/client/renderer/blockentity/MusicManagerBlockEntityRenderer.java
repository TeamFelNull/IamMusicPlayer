package dev.felnull.imp.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.MusicManagerBlock;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.otyacraftengine.client.model.SpecialModelLoader;
import dev.felnull.otyacraftengine.client.renderer.blockentity.AbstractBlockEntityRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import java.util.Random;

public class MusicManagerBlockEntityRenderer extends AbstractBlockEntityRenderer<MusicManagerBlockEntity> {
    private static final Random random = new Random();

    protected MusicManagerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(MusicManagerBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        renderMusicManager(blockEntity, poseStack, multiBufferSource, i, j, f);
    }

    public static void renderMusicManager(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f) {
        var spml = SpecialModelLoader.getInstance();
        var vc = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        var acLmp = spml.getModel(IMPModels.MUSIC_MANAGER_OFF_ACCESS_LAMP);

        if (blockEntity.isPower() && random.nextBoolean())
            acLmp = spml.getModel(IMPModels.MUSIC_MANAGER_ACCESS_LAMP);

        poseStack.pushPose();
        OERenderUtil.poseRotateDirection(poseStack, blockEntity.getBlockState().getValue(MusicManagerBlock.FACING), 1);
        OERenderUtil.renderModel(poseStack, vc, acLmp, i, j);
        poseStack.popPose();
    }
}
