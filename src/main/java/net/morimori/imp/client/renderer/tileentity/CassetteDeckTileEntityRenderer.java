package net.morimori.imp.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.morimori.imp.block.SoundfileUploaderBlock;
import net.morimori.imp.tileentity.CassetteDeckTileEntity;
import net.morimori.imp.util.RenderHelper;

public class CassetteDeckTileEntityRenderer extends TileEntityRenderer<CassetteDeckTileEntity> {

	public CassetteDeckTileEntityRenderer(TileEntityRendererDispatcher terd) {
		super(terd);

	}

	@Override
	public void func_225616_a_(CassetteDeckTileEntity tile, float partialTicks, MatrixStack matrix,
			IRenderTypeBuffer buff, int light, int overlay) {
		if (!tile.getAntenna().isEmpty()) {
			Direction direction = tile.getBlockState().get(SoundfileUploaderBlock.FACING);

			RenderHelper.matrixPush(matrix);

			if (direction == Direction.NORTH) {
				RenderHelper.matrixTranslatef(matrix, 0.75f, 0.42f, 0.094f);
			} else if (direction == Direction.SOUTH) {
				RenderHelper.matrixTranslatef(matrix, 1 - 0.75f, 0.42f, 1 - 0.094f);
			} else if (direction == Direction.EAST) {
				RenderHelper.matrixTranslatef(matrix, 1 - 0.094f, 0.42f, 0.75f);
			} else if (direction == Direction.WEST) {
				RenderHelper.matrixTranslatef(matrix, 0.094f, 0.42f, 1 - 0.75f);
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

			Minecraft.getInstance().getItemRenderer().func_229110_a_(tile.getAntenna(),
					TransformType.GROUND, light, overlay, matrix, buff);
			RenderHelper.matrixPop(matrix);
		}
	}

}
