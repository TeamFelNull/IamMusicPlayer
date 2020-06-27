package red.felnull.imp.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import red.felnull.imp.block.SoundfileUploaderBlock;
import red.felnull.imp.tileentity.SoundFileUploaderTileEntity;
import red.felnull.imp.util.RenderHelper;

public class SoundFileUploaderTileEntityRenderer extends TileEntityRenderer<SoundFileUploaderTileEntity> {

    public SoundFileUploaderTileEntityRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);

    }

    @Override
    public void render(SoundFileUploaderTileEntity tile, float partialTicks, MatrixStack matrix,
                       IRenderTypeBuffer buff, int light, int overlay) {

        if (!tile.getAntenna().isEmpty()) {
            Direction direction = tile.getBlockState().get(SoundfileUploaderBlock.FACING);

            RenderHelper.matrixPush(matrix);

            if (direction == Direction.NORTH) {
                RenderHelper.matrixTranslatef(matrix, 0.5f, 0.85f, 0.1225f);
            } else if (direction == Direction.SOUTH) {
                RenderHelper.matrixTranslatef(matrix, 0.5f, 0.85f, 0.8725f);
            } else if (direction == Direction.EAST) {
                RenderHelper.matrixTranslatef(matrix, 0.8725f, 0.85f, 0.5f);
            } else if (direction == Direction.WEST) {
                RenderHelper.matrixTranslatef(matrix, 0.1225f, 0.85f, 0.5f);
            }
            RenderHelper.matrixRotateDegreefY(matrix, tile.rotationYaw);

            RenderHelper.matrixRotateDegreefZ(matrix, tile.rotationPitch);

            if (tile.rotationPitch >= 0) {
                float parpich = tile.rotationPitch / 90f;
                RenderHelper.matrixTranslatef(matrix, 0.13f * parpich, -0.1f * parpich, 0);
            } else {
                float parpich = tile.rotationPitch / -90f;
                RenderHelper.matrixTranslatef(matrix, -0.13f * parpich, -0.1f * parpich, 0);
            }

            Minecraft.getInstance().getItemRenderer().renderItem(tile.getAntenna(),
                    TransformType.GROUND, light, overlay, matrix, buff);
            RenderHelper.matrixPop(matrix);

        }
    }

}
