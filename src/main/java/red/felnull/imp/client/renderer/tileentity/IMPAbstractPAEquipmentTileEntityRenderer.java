package red.felnull.imp.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import red.felnull.imp.tileentity.IMPAbstractPAEquipmentTileEntity;

public abstract class IMPAbstractPAEquipmentTileEntityRenderer<T extends IMPAbstractPAEquipmentTileEntity> extends IMPAbstractEquipmentTileEntityRenderer<T> {
    public IMPAbstractPAEquipmentTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.render(tileEntityIn, partialTicks, matrix, bufferIn, combinedLightIn, combinedOverlayIn);

    }
}
