package net.morimori.imp.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.morimori.imp.block.BoomboxBlock;
import net.morimori.imp.tileentity.CassetteStoringTileEntity;
import net.morimori.imp.util.RenderHelper;

@SuppressWarnings("deprecation")
public class CassetteStoringRenderer extends TileEntityRenderer<CassetteStoringTileEntity> {

	public CassetteStoringRenderer(TileEntityRendererDispatcher p_i226006_1_) {
		super(p_i226006_1_);
	}

	@Override
	public void func_225616_a_(CassetteStoringTileEntity tile, float partialTicks, MatrixStack matrix,
			IRenderTypeBuffer buff,
			int light, int overlay) {

		Direction direction = tile.getBlockState().get(BoomboxBlock.FACING);

		for (int i = 0; i < 8; ++i) {
			ItemStack cassette = tile.getCassette(i);
			if (!cassette.isEmpty()) {
				RenderHelper.matrixPush(matrix);

				RenderHelper.matrixRotateDegreefX(matrix, 90);
				RenderHelper.matrixScalf(matrix, 1.1f, 1.1f, 1.1f);

				float scale = 1f / 1.1f;

				if (direction == Direction.NORTH) {
					RenderHelper.matrixRotateDegreefZ(matrix, 90);
					RenderHelper.matrixTranslatef(matrix, 0.645f, -0.295f, -0.772f + 0.092f * i);
				} else if (direction == Direction.SOUTH) {
					RenderHelper.matrixRotateDegreefZ(matrix, 270);
					RenderHelper.matrixTranslatef(matrix, -scale + 0.645f, scale - 0.295f, -0.772f + 0.092f * i);
				} else if (direction == Direction.EAST) {
					RenderHelper.matrixRotateDegreefZ(matrix, 180);
					RenderHelper.matrixTranslatef(matrix, -scale + 0.645f, -0.295f, -0.772f + 0.092f * i);
				} else if (direction == Direction.WEST) {
					RenderHelper.matrixTranslatef(matrix, 0.645f, scale - 0.295f, -0.772f + 0.092f * i);
				}

				Minecraft.getInstance().getItemRenderer().func_229110_a_(cassette,
						TransformType.FIXED, light, overlay, matrix, buff);

				RenderHelper.matrixPop(matrix);
			}
		}

		for (int i = 0; i < 8; ++i) {
			ItemStack cassette = tile.getCassette(i + 8);
			if (!cassette.isEmpty()) {
				RenderHelper.matrixPush(matrix);

				RenderHelper.matrixRotateDegreefX(matrix, 90);
				RenderHelper.matrixScalf(matrix, 1.1f, 1.1f, 1.1f);

				float scale = 1f / 1.1f;

				if (direction == Direction.NORTH) {
					RenderHelper.matrixRotateDegreefZ(matrix, 90);
					RenderHelper.matrixTranslatef(matrix, scale - 0.645f, -0.295f, -0.772f + 0.092f * i);
				} else if (direction == Direction.SOUTH) {
					RenderHelper.matrixRotateDegreefZ(matrix, 270);
					RenderHelper.matrixTranslatef(matrix, -0.645f, scale - 0.295f, -0.772f + 0.092f * i);
				} else if (direction == Direction.EAST) {
					RenderHelper.matrixRotateDegreefZ(matrix, 180);
					RenderHelper.matrixTranslatef(matrix, -0.645f, -0.295f, -0.772f + 0.092f * i);
				} else if (direction == Direction.WEST) {
					RenderHelper.matrixTranslatef(matrix, scale - 0.645f, scale - 0.295f, -0.772f + 0.092f * i);
				}

				Minecraft.getInstance().getItemRenderer().func_229110_a_(cassette,
						TransformType.FIXED, light, overlay, matrix, buff);

				RenderHelper.matrixPop(matrix);
			}
		}

	}
}
