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
import red.felnull.imp.tileentity.IMPAbstractPAEquipmentTileEntity;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public abstract class IMPAbstractPAEquipmentTileEntityRenderer<T extends IMPAbstractPAEquipmentTileEntity> extends IMPAbstractEquipmentTileEntityRenderer<T> {
    protected float parabolicAntennaX;
    protected float parabolicAntennaY;
    protected float parabolicAntennaZ;

    public IMPAbstractPAEquipmentTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.render(tileEntityIn, partialTicks, matrix, bufferIn, combinedLightIn, combinedOverlayIn);
        IKSGRenderUtil.matrixPush(matrix);
        IKSGRenderUtil.matrixRotateHorizontal(tileEntityIn.getBlockState(), matrix);
        IKSGRenderUtil.matrixPush(matrix);
        horizontalRender(tileEntityIn, partialTicks, matrix, bufferIn, combinedLightIn, combinedOverlayIn);
        IKSGRenderUtil.matrixPop(matrix);
        IVertexBuilder ivb = bufferIn.getBuffer(RenderType.getSolid());
        if (!tileEntityIn.getPAntenna().isEmpty()) {
            float yaw = IKSGRenderUtil.partialTicksMisalignment(tileEntityIn.getPARotationYaw(), tileEntityIn.getPrevPARotationYaw(), partialTicks);
            float pitch = IKSGRenderUtil.partialTicksMisalignment(tileEntityIn.getPARotationPitch(), tileEntityIn.getPrevPARotationPitch(), partialTicks);
            if (tileEntityIn.getPAntenna().getItem() instanceof ParabolicAntennaItem) {
                IBakedModel parabolic_antenna = IKSGRenderUtil.getBakedModel(((ParabolicAntennaItem) tileEntityIn.getPAntenna().getItem()).getAntennaTextuer());
                IKSGRenderUtil.matrixPush(matrix);
                IKSGRenderUtil.matrixRotateDegreefY(matrix, -90f);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, 0f, -1f);
                IKSGRenderUtil.matrixTranslatef16Divisions(matrix, 0f, 4.5f, 0f);
                IKSGRenderUtil.matrixTranslatef16Divisions(matrix, parabolicAntennaX, parabolicAntennaY, parabolicAntennaZ);
                IKSGRenderUtil.matrixTranslatef16Divisions(matrix, 0f, -4.5f, 0f);
                IKSGRenderUtil.matrixRotateDegreefY(matrix, yaw);
                IKSGRenderUtil.matrixRotateDegreefZ(matrix, pitch);
                IKSGRenderUtil.matrixTranslatef16Divisions(matrix, 0f, 4.5f, 0f);
                IKSGRenderUtil.matrixScalf(matrix, 0.1f);
                IKSGRenderUtil.renderBlockBakedModel(parabolic_antenna, matrix, ivb, combinedOverlayIn, tileEntityIn);
                IKSGRenderUtil.matrixPop(matrix);
            } else {
                IKSGRenderUtil.matrixPush(matrix);
                IKSGRenderUtil.matrixRotateDegreefY(matrix, -90f);
                IKSGRenderUtil.matrixTranslatef(matrix, 0f, 0f, -1f);
                IKSGRenderUtil.matrixTranslatef16Divisions(matrix, 10.5f, 6f, 13.5f);
                IKSGRenderUtil.matrixTranslatef16Divisions(matrix, 0f, -1f, 0f);
                IKSGRenderUtil.matrixRotateDegreefY(matrix, yaw);
                IKSGRenderUtil.matrixRotateDegreefZ(matrix, pitch);
                IKSGRenderUtil.matrixTranslatef16Divisions(matrix, 0f, 1f, 0f);
                Minecraft.getInstance().getItemRenderer().renderItem(tileEntityIn.getPAntenna(), ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrix, bufferIn);
                IKSGRenderUtil.matrixPop(matrix);
            }
        }
        IKSGRenderUtil.matrixPop(matrix);
    }

    protected void horizontalRender(T tileEntityIn, float partialTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

    }
}
