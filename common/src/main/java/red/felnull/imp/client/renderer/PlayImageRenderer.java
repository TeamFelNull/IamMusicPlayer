package red.felnull.imp.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.gui.IMPFonts;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;
import red.felnull.otyacraftengine.util.IKSGColorUtil;

import java.util.Random;

public class PlayImageRenderer {
    private static final PlayImageRenderer INSTANCE = new PlayImageRenderer();
    private static final ResourceLocation PLAY_IMAGE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/play_image.png");
    private static final Minecraft mc = Minecraft.getInstance();

    public static PlayImageRenderer getInstance() {
        return INSTANCE;
    }

    public void draw(ImageInfo location, PoseStack poseStack, int x, int y, int size) {
        draw(location, poseStack, x, y, size, true);
    }

    public void draw(ImageInfo location, PoseStack poseStack, int x, int y, int size, boolean cash) {
        switch (location.getImageType()) {
            case URL -> drawURLImage(location.getIdentifier(), poseStack, x, y, location.getWidthScale(), location.getHeightScale(), size, cash);
            case STRING -> drawStringImage(location.getIdentifier(), poseStack, x, y, size);
            case PLAYER_FACE -> drawPlayerFaceImage(location.getIdentifier(), poseStack, x, y, size);
            case YOUTUBE_THUMBNAIL -> drawURLImage(String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", location.getIdentifier()), poseStack, x, y, location.getWidthScale(), location.getHeightScale(), size, cash);
        }
    }

    private void drawURLImage(String url, PoseStack poseStack, int x, int y, float w, float h, int size, boolean cash) {

        if (w > h) {
            h *= 1f / w;
            w = 1;
        }
        if (h > w) {
            w *= 1f / h;
            h = 1;
        }

        float width = size * w;
        float height = size * h;

        ResourceLocation location = IKSGTextureUtil.getURLTextureAsync(url, cash, PLAY_IMAGE);

        if (location == PLAY_IMAGE) {
            drawStringColorImage(I18n.get("Loading"), poseStack, x, y, width, height, size, 0xFFFFFF);
        } else {
            IKSGRenderUtil.drawTexture(location, poseStack, x + (size - width) / 2, y + (size - height) / 2, 0, 0, width, height, width, height);
        }
    }

    private void drawStringImage(String str, PoseStack poseStack, int x, int y, int size) {
        Random r = new Random(str.hashCode());
        drawStringColorImage(str, poseStack, x, y, size, size, size, r.nextInt(0xFFFFFF));
    }

    private void drawStringColorImage(String str, PoseStack poseStack, int x, int y, float width, float height, int size, int color) {
        IKSGRenderUtil.drawColorTexture(PLAY_IMAGE, poseStack, x + (size - width) / 2, y + (size - height) / 2, 0, 0, width, height, width, height, IKSGColorUtil.toSRGB(color));
        Component text = new TextComponent(str).withStyle(IMPFonts.FLOPDE_SIGN_FONT);
        int txSize = Math.max(mc.font.width(text), mc.font.lineHeight);
        poseStack.pushPose();
        float sc = (float) size / txSize;
        IKSGRenderUtil.poseScaleAll(poseStack, sc);
        Font font = mc.font;
        font.draw(poseStack, text, (x + size / 2f - ((float) font.width(text) / 2f) * sc) / sc, (y + size / 2f - ((float) font.lineHeight / 2f) * sc) / sc, 0);
        poseStack.popPose();
    }

    private void drawPlayerFaceImage(String name, PoseStack poseStack, int x, int y, int size) {
        ResourceLocation plskin = IKSGTextureUtil.getPlayerSkinTexture(name);
        IKSGRenderUtil.drawTexture(plskin, poseStack, x, y, size, size, size, size, size * 8, size * 8);
        IKSGRenderUtil.drawTexture(plskin, poseStack, x, y, size * 5, size, size, size, size * 8, size * 8);
    }

    public void renderSprite(ImageInfo location, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float pitch, float yaw, float roll, float size, int combinedLightIn, int combinedOverlayIn) {
        renderSprite(location, poseStack, multiBufferSource, x, y, z, pitch, yaw, roll, size, true, combinedLightIn, combinedOverlayIn);
    }

    public void renderSprite(ImageInfo location, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float pitch, float yaw, float roll, float size, boolean cash, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        IKSGRenderUtil.poseRotateAll(poseStack, pitch, yaw, roll);
        switch (location.getImageType()) {
            case URL -> renderURLImageSprite(location.getIdentifier(), poseStack, multiBufferSource, x, y, z, location.getWidthScale(), location.getHeightScale(), size, cash, combinedLightIn, combinedOverlayIn);
            case STRING -> renderStringImageSprite(location.getIdentifier(), poseStack, multiBufferSource, x, y, z, size, combinedLightIn, combinedOverlayIn);
            case PLAYER_FACE -> renderPlayerFaceImageSprite(location.getIdentifier(), poseStack, multiBufferSource, x, y, z, size, combinedLightIn, combinedOverlayIn);
            case YOUTUBE_THUMBNAIL -> renderURLImageSprite(String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", location.getIdentifier()), poseStack, multiBufferSource, x, y, z, location.getWidthScale(), location.getHeightScale(), size, cash, combinedLightIn, combinedOverlayIn);
        }
        poseStack.popPose();
    }

    private void renderStringImageSprite(String name, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float size, int combinedLightIn, int combinedOverlayIn) {
        Random r = new Random(name.hashCode());
        renderStringImageSprite(name, poseStack, multiBufferSource, x, y, z, size, size, size, r.nextInt(0xFFFFFF), combinedLightIn, combinedOverlayIn);
    }

    private void renderStringImageSprite(String str, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float width, float height, float size, int color, int combinedLightIn, int combinedOverlayIn) {
        IKSGRenderUtil.renderColorTextureSprite(PLAY_IMAGE, poseStack, multiBufferSource, x + (size - width) / 2, y + (size - height) / 2, z, IKSGColorUtil.getRed(color), IKSGColorUtil.getGreen(color), IKSGColorUtil.getBlue(color), 1, 0, 0, 0, width, height, 0, 0, width, height, width, height, combinedLightIn, combinedOverlayIn);
        Component text = new TextComponent(str).withStyle(IMPFonts.FLOPDE_SIGN_FONT);
        int txSize = Math.max(mc.font.width(text), mc.font.lineHeight);
        float brit = 70f;
        float sc = (size * brit) / txSize;
        IKSGRenderUtil.renderCenterTextSprite(poseStack, multiBufferSource, text, x + size / 2f, y + size / 2f, z + Mth.EPSILON, sc, 0, (float) mc.font.lineHeight / 2f);
    }

    private void renderPlayerFaceImageSprite(String name, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float size, int combinedLightIn, int combinedOverlayIn) {
        IKSGRenderUtil.renderPlayerFaceSprite(poseStack, multiBufferSource, name, x, y, z, 0, 0, 0, size, combinedLightIn, combinedOverlayIn);
    }

    private void renderURLImageSprite(String url, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float w, float h, float size, boolean cash, int combinedLightIn, int combinedOverlayIn) {

        if (w > h) {
            h *= 1f / w;
            w = 1;
        }
        if (h > w) {
            w *= 1f / h;
            h = 1;
        }

        float width = size * w;
        float height = size * h;

        ResourceLocation location = IKSGTextureUtil.getURLTextureAsync(url, cash, PLAY_IMAGE);
        if (location == PLAY_IMAGE) {
            renderStringImageSprite(I18n.get("Loading"), poseStack, multiBufferSource, x, y, z, width, height, size, 0x114514, combinedLightIn, combinedOverlayIn);
        } else {
            IKSGRenderUtil.renderTextureSprite(location, poseStack, multiBufferSource, x + (size - width) / 2, y + (size - height) / 2, z, 0, 0, 0, width, height, 0, 0, width, height, width, height, combinedLightIn, combinedOverlayIn);
        }
    }
}
