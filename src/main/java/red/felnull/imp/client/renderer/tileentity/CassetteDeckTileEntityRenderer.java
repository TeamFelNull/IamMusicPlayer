package red.felnull.imp.client.renderer.tileentity;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import red.felnull.imp.tileentity.CassetteDeckTileEntity;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class CassetteDeckTileEntityRenderer extends IMPAbstractPAEquipmentTileEntityRenderer<CassetteDeckTileEntity> {

    public CassetteDeckTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.parabolicAntennaX = 7.5f;
        this.parabolicAntennaY = 7.45f;
        this.parabolicAntennaZ = 9.5f;
    }

    @Override
    protected void horizontalRender(CassetteDeckTileEntity tileEntityIn, float partialTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.horizontalRender(tileEntityIn, partialTicks, matrix, bufferIn, combinedLightIn, combinedOverlayIn);
        if (tileEntityIn.isOn()) {
            IKSGRenderUtil.matrixPush(matrix);
            IKSGRenderUtil.matrixRotateDegreefY(matrix, 90);
            IKSGRenderUtil.matrixTranslatef(matrix, -1, 0, 1);
            IKSGRenderUtil.matrixTranslatef16Divisions(matrix, 6.175f, 1.6f, -3.49f);
            float pix = 1f / 16f;
            IKSGRenderUtil.renderSpritePanel(tileEntityIn.getScreen().getTexLocation(), matrix, bufferIn, 0, 0, 0, 0, 0, 0, pix * 3.7f, pix * 1.325f, 0, 0, 199, 122, 199, 122, combinedOverlayIn, combinedLightIn);
            IKSGRenderUtil.matrixPop(matrix);
        }
    }
}
