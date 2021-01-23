package red.felnull.imp.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import red.felnull.imp.tileentity.CassetteStorageTileEntity;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.stream.IntStream;

public class CassetteStorageTileEntityRenderer<T extends CassetteStorageTileEntity> extends IMPAbstractTileEntityRenderer<T> {
    public CassetteStorageTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    protected void horizontalRender(CassetteStorageTileEntity tileEntityIn, float partialTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        IKSGRenderUtil.matrixPush(matrix);
        IKSGRenderUtil.matrixRotateDegreefY(matrix, 270);
        IKSGRenderUtil.matrixTranslatef(matrix, 1, 1, -1);
        IntStream.range(0, 8).forEach(n -> {
            if (!tileEntityIn.getStackInSlot(n).isEmpty()) {
                IKSGRenderUtil.matrixPush(matrix);
                IKSGRenderUtil.matrixTranslatef16Divisions(matrix, -5.25f, -1.6f * n - 2.6f, 8);
                IKSGRenderUtil.matrixScalf(matrix, 0.6f);
                IKSGRenderUtil.matrixRotateDegreefX(matrix, 90);
                Minecraft.getInstance().getItemRenderer().renderItem(tileEntityIn.getStackInSlot(n), ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrix, bufferIn);
                IKSGRenderUtil.matrixPop(matrix);
            }
        });
        IntStream.range(0, 8).forEach(n -> {
            if (!tileEntityIn.getStackInSlot(n + 8).isEmpty()) {
                IKSGRenderUtil.matrixPush(matrix);
                IKSGRenderUtil.matrixTranslatef16Divisions(matrix, -12.25f, -1.6f * n - 2.6f, 8);
                IKSGRenderUtil.matrixScalf(matrix, 0.6f);
                IKSGRenderUtil.matrixRotateDegreefX(matrix, 90);
                Minecraft.getInstance().getItemRenderer().renderItem(tileEntityIn.getStackInSlot(n), ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrix, bufferIn);
                IKSGRenderUtil.matrixPop(matrix);
            }
        });

        IKSGRenderUtil.matrixPop(matrix);
    }
}
