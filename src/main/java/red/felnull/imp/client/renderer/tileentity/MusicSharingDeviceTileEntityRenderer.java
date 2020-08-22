package red.felnull.imp.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import red.felnull.imp.item.ParabolicAntennaItem;
import red.felnull.imp.tileentity.MusicSharingDeviceTileEntity;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class MusicSharingDeviceTileEntityRenderer extends TileEntityRenderer<MusicSharingDeviceTileEntity> {

    public MusicSharingDeviceTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(MusicSharingDeviceTileEntity tileEntityIn, float partialTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        IVertexBuilder ivb = bufferIn.getBuffer(RenderType.getSolid());
        BlockState state = tileEntityIn.getBlockState();
        Direction direction = state.get(HorizontalBlock.HORIZONTAL_FACING);
        float pix = 1f / 16f;
        IKSGRenderUtil.matrixPush(matrix);
        if (direction == Direction.WEST) {
            IKSGRenderUtil.matrixRotateDegreefY(matrix, 180);
            IKSGRenderUtil.matrixTranslatef(matrix, -1f, 0f, -1f);
        } else if (direction == Direction.NORTH) {
            IKSGRenderUtil.matrixRotateDegreefY(matrix, 90);
            IKSGRenderUtil.matrixTranslatef(matrix, -1f, 0f, 0f);
        } else if (direction == Direction.SOUTH) {
            IKSGRenderUtil.matrixRotateDegreefY(matrix, 270);
            IKSGRenderUtil.matrixTranslatef(matrix, 0f, 0f, -1f);
        }
        if (!tileEntityIn.getAntenna().isEmpty()) {
            if (tileEntityIn.getAntenna().getItem() instanceof ParabolicAntennaItem) {
                IBakedModel parabolic_antenna = IKSGRenderUtil.getBakedModel(((ParabolicAntennaItem) tileEntityIn.getAntenna().getItem()).getAntennaTextuer());
                IKSGRenderUtil.matrixPush(matrix);
                IKSGRenderUtil.matrixRotateDegreefY(matrix, -90f);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, 0f, -1f);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, pix * 4.5f, 0f);
                IKSGRenderUtil.matrixTranslatef(matrix, pix * 10.5f, pix * 4.3f, pix * 13.5f);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, pix * -4.5f, 0f);
                IKSGRenderUtil.matrixRotateDegreefY(matrix, tileEntityIn.rotationYaw);
                IKSGRenderUtil.matrixRotateDegreefZ(matrix, tileEntityIn.rotationPitch);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, pix * 4.5f, 0f);
                IKSGRenderUtil.matrixScalf(matrix, 0.1f);
                IKSGRenderUtil.renderBlockBakedModel(parabolic_antenna, matrix, ivb, combinedOverlayIn, tileEntityIn);
                IKSGRenderUtil.matrixPop(matrix);
            } else {
                IKSGRenderUtil.matrixPush(matrix);
                IKSGRenderUtil.matrixRotateDegreefY(matrix, -90f);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, 0f, -1f);
                IKSGRenderUtil.matrixTranslatef(matrix, pix * 10.5f, pix * 6f, pix * 13.5f);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, pix * -1f, 0f);
                IKSGRenderUtil.matrixRotateDegreefY(matrix, tileEntityIn.rotationYaw);
                IKSGRenderUtil.matrixRotateDegreefZ(matrix, tileEntityIn.rotationPitch);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, pix * 1f, 0f);
                Minecraft.getInstance().getItemRenderer().renderItem(tileEntityIn.getAntenna(), ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrix, bufferIn);
                IKSGRenderUtil.matrixPop(matrix);
            }
        }
        IKSGRenderUtil.matrixPop(matrix);
    }
}
