package red.felnull.imp.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import red.felnull.imp.tileentity.IMPAbstractEquipmentTileEntity;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public abstract class IMPAbstractEquipmentTileEntityRenderer<T extends IMPAbstractEquipmentTileEntity> extends TileEntityRenderer<T> {
    public IMPAbstractEquipmentTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

    }

    public void matrixRotateHorizontal(BlockState state, MatrixStack matrix) {
        Direction direction = state.get(HorizontalBlock.HORIZONTAL_FACING);
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
    }
}
