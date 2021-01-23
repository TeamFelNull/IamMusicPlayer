package red.felnull.imp.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import red.felnull.imp.tileentity.IMPAbstractTileEntity;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public abstract class IMPAbstractTileEntityRenderer<T extends IMPAbstractTileEntity> extends TileEntityRenderer<T> {
    public IMPAbstractTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        IKSGRenderUtil.matrixPush(matrixStackIn);
        IKSGRenderUtil.matrixRotateHorizontal(tileEntityIn.getBlockState(), matrixStackIn);
        horizontalRender(tileEntityIn, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        IKSGRenderUtil.matrixPop(matrixStackIn);
    }

    protected void horizontalRender(T tileEntityIn, float partialTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

    }
}
