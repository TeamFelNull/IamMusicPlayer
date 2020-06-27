package red.felnull.imp.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.vector.Vector3f;

public class RenderHelper {
    private static Minecraft mc = Minecraft.getInstance();

    public static void drawPlayerFase(MatrixStack maxtx, String name, int x, int y) {
        RenderSystem.pushMatrix();
        mc.getTextureManager().bindTexture(TextureHelper.getPlayerSkinTexture(name));
        AbstractGui.func_238463_a_(maxtx, x, y, 8, 8, 8, 8, 64, 64);
        mc.getTextureManager().bindTexture(TextureHelper.getPlayerSkinTexture(name));
        AbstractGui.func_238463_a_(maxtx, x, y, 40, 8, 8, 8, 64, 64);
        RenderSystem.popMatrix();
    }

    public static void matrixTranslatef(MatrixStack ms, float x, float y, float z) {
        ms.translate(x, y, z);
    }

    public static void matrixScalf(MatrixStack ms, float x, float y, float z) {
        ms.scale(x, y, z);
    }

    public static void matrixPush(MatrixStack ms) {
        ms.push();
    }

    public static void matrixPop(MatrixStack ms) {
        ms.pop();
    }

    public static void matrixRotateDegreefX(MatrixStack ms, float x) {
        ms.rotate(new Vector3f(1, 0, 0).rotationDegrees(x));
    }

    public static void matrixRotateDegreefY(MatrixStack ms, float y) {
        ms.rotate(new Vector3f(0, 1, 0).rotationDegrees(y));
    }

    public static void matrixRotateDegreefZ(MatrixStack ms, float z) {
        ms.rotate(new Vector3f(0, 0, 1).rotationDegrees(z));
    }

}
