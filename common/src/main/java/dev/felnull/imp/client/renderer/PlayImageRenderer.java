package dev.felnull.imp.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import dev.felnull.otyacraftengine.client.util.OETextureUtil;

public class PlayImageRenderer {
    private static final PlayImageRenderer INSTANCE = new PlayImageRenderer();
    private static final String YOUTUBE_THUMBNAIL_URL = "https://i.ytimg.com/vi/%s/hqdefault.jpg";

    public static PlayImageRenderer getInstance() {
        return INSTANCE;
    }

    public void draw(ImageInfo imageInfo, PoseStack poseStack, int x, int y, int size) {
        draw(imageInfo, poseStack, x, y, size, true);
    }

    public void draw(ImageInfo imageInfo, PoseStack poseStack, int x, int y, int size, boolean cash) {
        if (imageInfo.isEmpty())
            return;

        switch (imageInfo.getImageType()) {
            case PLAYER_FACE -> drawPlayerFaceImage(imageInfo.getIdentifier(), poseStack, x, y, size);
            case URL -> drawURLImage(imageInfo.getIdentifier(), poseStack, x, y, size, cash);
            case YOUTUBE_THUMBNAIL -> drawURLImage(String.format(YOUTUBE_THUMBNAIL_URL, imageInfo.getIdentifier()), poseStack, x, y, size, cash);
        }
    }

    public void drawPlayerFaceImage(String name, PoseStack poseStack, int x, int y, int size) {
        var texture = OETextureUtil.getPlayerSkinTexture(name);
        OERenderUtil.drawTexture(texture, poseStack, x, y, size, size, size, size, size * 8, size * 8);
        OERenderUtil.drawTexture(texture, poseStack, x, y, size * 5, size, size, size, size * 8, size * 8);
    }

    public void drawURLImage(String url, PoseStack poseStack, int x, int y, int size, boolean cash) {
        var texture = OETextureUtil.getURLTextureAsyncLoad(url, cash);
        if (texture == null) return;
        var scale = OETextureUtil.getTextureScale(texture);
        float w = size;
        float h = size;
        if (scale != null) {
            w *= scale.getX();
            h *= scale.getY();
        }
        OERenderUtil.drawTexture(texture, poseStack, x, y, 0, 0, w, h, w, h);
    }
}
