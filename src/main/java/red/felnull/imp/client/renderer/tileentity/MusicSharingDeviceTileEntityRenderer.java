package red.felnull.imp.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.tileentity.MusicSharingDeviceTileEntity;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class MusicSharingDeviceTileEntityRenderer extends IMPAbstractPAEquipmentTileEntityRenderer<MusicSharingDeviceTileEntity> {

    public MusicSharingDeviceTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.parabolicAntennaX = 10.5f;
        this.parabolicAntennaY = 4.3f;
        this.parabolicAntennaZ = 13.5f;
    }

    @Override
    protected void horizontalRender(MusicSharingDeviceTileEntity tileEntityIn, float partialTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.isOn()) {
            IKSGRenderUtil.matrixPush(matrix);
            IKSGRenderUtil.matrixRotateDegreefY(matrix, 90);
            IKSGRenderUtil.matrixTranslatef(matrix, -1, 0, 1);
            IKSGRenderUtil.matrixTranslatef16Divisions(matrix, 0.25f, 2.25f, -9.225f);
            float pix = 1f / 16f;
            IKSGRenderUtil.renderSpritePanel(tileEntityIn.getScreen(IamMusicPlayer.proxy.getMinecraft().player).getTexLocation(), matrix, bufferIn, 0, 0, 0, 0, 0, 0, pix * 10.5f, pix * 7.5f, 0, 0, 199, 122, 199, 122, combinedOverlayIn, combinedLightIn);
            IKSGRenderUtil.matrixPop(matrix);
        }
    }
}
