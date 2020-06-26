package net.morimori.imp.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.Vector3f;

public class RenderHelper {
	private static Minecraft mc = Minecraft.getInstance();

	public static void drawPlayerFase(String name, int x, int y) {
		RenderSystem.pushMatrix();
		mc.getTextureManager().bindTexture(TextureHelper.getPlayerSkinTexture(name));
		AbstractGui.blit(x, y, 8, 8, 8, 8, 64,
				64);
		mc.getTextureManager().bindTexture(TextureHelper.getPlayerSkinTexture(name));
		AbstractGui.blit(x, y, 40, 8, 8, 8, 64,
				64);
		RenderSystem.popMatrix();
	}

	public static void matrixTranslatef(MatrixStack ms, float x, float y, float z) {
		ms.func_227861_a_(x, y, z);
	}

	public static void matrixScalf(MatrixStack ms, float x, float y, float z) {
		ms.func_227862_a_(x, y, z);
	}

	public static void matrixPush(MatrixStack ms) {
		ms.func_227860_a_();
	}

	public static void matrixPop(MatrixStack ms) {
		ms.func_227865_b_();
	}

	public static void matrixRotateDegreefX(MatrixStack ms, float x) {
		ms.func_227863_a_(new Vector3f(1, 0, 0).func_229187_a_(x));
	}

	public static void matrixRotateDegreefY(MatrixStack ms, float y) {
		ms.func_227863_a_(new Vector3f(0, 1, 0).func_229187_a_(y));
	}

	public static void matrixRotateDegreefZ(MatrixStack ms, float z) {
		ms.func_227863_a_(new Vector3f(0, 0, 1).func_229187_a_(z));
	}

}
