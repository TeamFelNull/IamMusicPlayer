package net.morimori.imp.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.morimori.imp.IkisugiMusicPlayer;

public class RenderHelper {
	private static Minecraft mc = Minecraft.getInstance();
	private static TextureManager tm = mc.getTextureManager();

	private static Map<byte[], ResourceLocation> byteloactions = new HashMap<byte[], ResourceLocation>();

	public static void drawTexture(ResourceLocation rl, int p_innerBlit_0_, int p_innerBlit_1_, int p_innerBlit_2_,
			int p_innerBlit_3_, int p_innerBlit_4_, float p_innerBlit_5_, float p_innerBlit_6_, float p_innerBlit_7_,
			float p_innerBlit_8_) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		tm.bindTexture(rl);

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.func_225582_a_((double) p_innerBlit_0_, (double) p_innerBlit_3_, (double) p_innerBlit_4_)
				.func_225583_a_(p_innerBlit_5_, p_innerBlit_8_).endVertex();
		bufferbuilder.func_225582_a_((double) p_innerBlit_1_, (double) p_innerBlit_3_, (double) p_innerBlit_4_)
				.func_225583_a_(p_innerBlit_6_, p_innerBlit_8_).endVertex();
		bufferbuilder.func_225582_a_((double) p_innerBlit_1_, (double) p_innerBlit_2_, (double) p_innerBlit_4_)
				.func_225583_a_(p_innerBlit_6_, p_innerBlit_7_).endVertex();
		bufferbuilder.func_225582_a_((double) p_innerBlit_0_, (double) p_innerBlit_2_, (double) p_innerBlit_4_)
				.func_225583_a_(p_innerBlit_5_, p_innerBlit_7_).endVertex();
		tessellator.draw();
		RenderSystem.enableAlphaTest();

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

	public static ResourceLocation getBytePictuerLocation(byte[] bytes) {

		if (byteloactions.containsKey(bytes)) {
			return byteloactions.get(bytes);
		}

		ResourceLocation imagelocation = new ResourceLocation(IkisugiMusicPlayer.MODID,
				"byteimage/" + UUID.randomUUID().toString());

		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

			NativeImage NI = NativeImage.read(bis);

			tm.func_229263_a_(imagelocation, new DynamicTexture(NI));
			byteloactions.put(bytes, imagelocation);
			return imagelocation;
		} catch (IOException e) {

		}

		return null;
	}
}
