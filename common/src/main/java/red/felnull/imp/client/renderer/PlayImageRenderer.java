package red.felnull.imp.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.gui.IMPFonts;
import red.felnull.imp.data.resource.ImageLocation;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;

import java.util.Random;

public class PlayImageRenderer {
    private static final PlayImageRenderer INSTANCE = new PlayImageRenderer();
    private static final ResourceLocation PLAY_IMAGE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/play_image.png");
    private static final Minecraft mc = Minecraft.getInstance();

    public static PlayImageRenderer getInstance() {
        return INSTANCE;
    }

    public void render(ImageLocation location, PoseStack poseStack, int x, int y, int size) {
        switch (location.getImageType()) {
            case URL -> renderURLImage(location.getIdentifier(), location.getData(), poseStack, x, y, size);
            case STRING -> renderStringImage(location.getIdentifier(), poseStack, x, y, size);
            case PLAYER_FACE -> renderPlayerFaceImage(location.getIdentifier(), poseStack, x, y, size);
        }
    }

    private void renderURLImage(String url, CompoundTag data, PoseStack poseStack, int x, int y, int size) {

        float w = data.getFloat("w");
        float h = data.getFloat("h");

        if (w == 0)
            w = 1;

        if (h == 0)
            h = 1;

        if (w > h) {
            h *= 1f / w;
            w = 1;
        }
        if (h > w) {
            w *= 1f / h;
            h = 1;
        }

        float ws = size * w;
        float hs = size * h;

        try {
            IKSGRenderUtil.drawBindTextuer(IKSGTextureUtil.getURLTexture(url, true), poseStack, x + (size - ws) / 2, y + (size - hs) / 2, 0, 0, ws, hs, ws, hs);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void renderStringImage(String str, PoseStack poseStack, int x, int y, int size) {
        Random r = new Random(str.hashCode());
        IKSGRenderUtil.drawBindColorTextuer(PLAY_IMAGE, poseStack, x, y, 0, 0, size, size, size, size, r.nextFloat(), r.nextFloat(), r.nextFloat(), 1);
        Component text = new TextComponent(str).withStyle(IMPFonts.FLOPDE_SIGN_FONT);
        int txSize = Math.max(mc.font.width(text), mc.font.lineHeight);
        poseStack.pushPose();
        float sc = (float) size / txSize;
        IKSGRenderUtil.matrixScalf(poseStack, sc);
        Font font = mc.font;
        font.draw(poseStack, text, (x + size / 2f - ((float) font.width(text) / 2f) * sc) / sc, (y + size / 2f - ((float) font.lineHeight / 2f) * sc) / sc, 0);
        poseStack.popPose();
    }

    private void renderPlayerFaceImage(String name, PoseStack poseStack, int x, int y, int size) {
        ResourceLocation plskin = IKSGTextureUtil.getPlayerSkinTexture(name);
        IKSGRenderUtil.drawBindTextuer(plskin, poseStack, x, y, size, size, size, size, size * 8, size * 8);
        IKSGRenderUtil.drawBindTextuer(plskin, poseStack, x, y, size * 5, size, size, size, size * 8, size * 8);
    }
}
