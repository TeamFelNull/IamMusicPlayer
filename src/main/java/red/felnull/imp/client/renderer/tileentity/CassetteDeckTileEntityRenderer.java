package red.felnull.imp.client.renderer.tileentity;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import red.felnull.imp.item.ParabolicAntennaItem;
import red.felnull.imp.tileentity.CassetteDeckTileEntity;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class CassetteDeckTileEntityRenderer extends IMPAbstractEquipmentTileEntityRenderer<CassetteDeckTileEntity> {

    public CassetteDeckTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(CassetteDeckTileEntity tileEntityIn, float partialTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.render(tileEntityIn, partialTicks, matrix, bufferIn, combinedLightIn, combinedOverlayIn);
        IKSGRenderUtil.matrixPush(matrix);
        matrixRotateHorizontal(tileEntityIn.getBlockState(), matrix);
        IVertexBuilder ivb = bufferIn.getBuffer(RenderType.getSolid());
        float pix = 1f / 16f;
        if (!tileEntityIn.getPAntenna().isEmpty()) {
            if (tileEntityIn.getPAntenna().getItem() instanceof ParabolicAntennaItem) {
                IBakedModel parabolic_antenna = IKSGRenderUtil.getBakedModel(((ParabolicAntennaItem) tileEntityIn.getPAntenna().getItem()).getAntennaTextuer());
                IKSGRenderUtil.matrixPush(matrix);
                IKSGRenderUtil.matrixRotateDegreefY(matrix, -90f);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, 0f, -1f);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, pix * 4.5f, 0f);
                IKSGRenderUtil.matrixTranslatef(matrix, pix * 10.5f, pix * 4.3f, pix * 13.5f);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, pix * -4.5f, 0f);
                IKSGRenderUtil.matrixRotateDegreefY(matrix, tileEntityIn.getPARotationYaw());
                IKSGRenderUtil.matrixRotateDegreefZ(matrix, tileEntityIn.getPARotationPitch());
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
                IKSGRenderUtil.matrixRotateDegreefY(matrix, tileEntityIn.getPARotationYaw());
                IKSGRenderUtil.matrixRotateDegreefZ(matrix, tileEntityIn.getPARotationPitch());
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, pix * 1f, 0f);
                Minecraft.getInstance().getItemRenderer().renderItem(tileEntityIn.getPAntenna(), ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrix, bufferIn);
                IKSGRenderUtil.matrixPop(matrix);
            }
        }
        IKSGRenderUtil.matrixPop(matrix);
    }
}
