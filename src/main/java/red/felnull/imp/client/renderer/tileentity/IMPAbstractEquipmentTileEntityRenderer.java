package red.felnull.imp.client.renderer.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import red.felnull.imp.tileentity.IMPAbstractEquipmentTileEntity;

public abstract class IMPAbstractEquipmentTileEntityRenderer<T extends IMPAbstractEquipmentTileEntity> extends IMPAbstractTileEntityRenderer<T> {
    public IMPAbstractEquipmentTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }
}
