package dev.felnull.imp.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.fnjl.util.FNMath;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import dev.felnull.otyacraftengine.client.util.OETextureUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

public class PlayImageRenderer {
    private static final ResourceLocation MISSING_YOUTUBE_THUMBNAIL_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/image/missing_youtube_thumbnail.png");
    private static final ResourceLocation MISSING_SOUND_CLOUD_ARTWORK_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/image/missing_sound_cloud_artwork.png");
    private static final PlayImageRenderer INSTANCE = new PlayImageRenderer();
    private static final String YOUTUBE_THUMBNAIL_URL = "https://i.ytimg.com/vi/%s/hqdefault.jpg";
    private final Map<String, String> soundCloudArtworkURLs = new HashMap<>();

    public static PlayImageRenderer getInstance() {
        return INSTANCE;
    }

    public void draw(ImageInfo imageInfo, PoseStack poseStack, float x, float y, int size) {
        draw(imageInfo, poseStack, x, y, size, true);
    }

    public void draw(ImageInfo imageInfo, PoseStack poseStack, float x, float y, float size, boolean cash) {
        if (imageInfo.isEmpty())
            return;

        if (imageInfo.getImageType() == ImageInfo.ImageType.PLAYER_FACE) {
            drawPlayerFaceImage(imageInfo.getIdentifier(), poseStack, x, y, size);
            return;
        }
        var td = getImageTexture(imageInfo, cash);
        if (td != null)
            drawTextureImage(td.getLeft(), poseStack, td.getRight().x, td.getRight().y, x, y, size);
    }

    public void drawPlayerFaceImage(String name, PoseStack poseStack, float x, float y, float size) {
        var texture = OETextureUtil.getPlayerSkinTexture(name);
        OERenderUtil.drawTexture(texture, poseStack, x, y, size, size, size, size, size * 8, size * 8);
        OERenderUtil.drawTexture(texture, poseStack, x, y, size * 5, size, size, size, size * 8, size * 8);
    }

    public void drawTextureImage(ResourceLocation location, PoseStack poseStack, float wScale, float hScale, float x, float y, float size) {
        if (location == null) return;
        float w = size * wScale;
        float h = size * hScale;
        OERenderUtil.drawTexture(location, poseStack, x + (size - w) / 2, y + (size - h) / 2, 0, 0, w, h, w, h);
    }

    public void renderSprite(ImageInfo imageInfo, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float size, int i, int j) {
        renderSprite(imageInfo, poseStack, multiBufferSource, x, y, z, size, i, j, true);
    }

    public void renderSprite(ImageInfo imageInfo, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float size, int i, int j, boolean cash) {
        if (imageInfo.isEmpty())
            return;
        if (imageInfo.getImageType() == ImageInfo.ImageType.PLAYER_FACE) {
            renderPlayerFaceImageSprite(imageInfo.getIdentifier(), poseStack, multiBufferSource, x, y, z, size, i, j);
            return;
        }
        var td = getImageTexture(imageInfo, cash);
        if (td != null)
            renderTextureImageSprite(td.getLeft(), poseStack, multiBufferSource, td.getRight().x, td.getRight().y, x, y, z, size, i, j);
    }

    private void renderPlayerFaceImageSprite(String name, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float size, int i, int j) {
        OERenderUtil.renderPlayerFaceSprite(poseStack, multiBufferSource, name, x, y, z, 0, 0, 0, size, i, j);
    }

    private void renderTextureImageSprite(ResourceLocation location, PoseStack poseStack, MultiBufferSource multiBufferSource, float wScale, float hScale, float x, float y, float z, float size, int i, int j) {
        if (location == null) return;
        float w = size * wScale;
        float h = size * hScale;
        OERenderUtil.renderTextureSprite(location, poseStack, multiBufferSource, x + (size - w) / 2, y + (size - h) / 2, z, 0, 0, 0, w, h, 0, 0, w, h, w, h, i, j);
    }


    private Pair<ResourceLocation, Vec2> getImageTexture(ImageInfo imageInfo, boolean cash) {
        if (imageInfo.getImageType() == ImageInfo.ImageType.URL) {
            var loc = OETextureUtil.getURLTextureAsyncLoad(imageInfo.getIdentifier(), cash);
            var scale = OETextureUtil.getTextureScale(loc);
            float w = 1;
            float h = 1;
            if (scale != null) {
                w = (float) scale.w();
                h = (float) scale.h();
            }
            return Pair.of(loc, new Vec2(w, h));
        } else if (imageInfo.getImageType() == ImageInfo.ImageType.YOUTUBE_THUMBNAIL) {
            var loc = OETextureUtil.getURLTextureAsyncLoad(String.format(YOUTUBE_THUMBNAIL_URL, imageInfo.getIdentifier()), cash, MISSING_YOUTUBE_THUMBNAIL_TEXTURE);
            var scale = OETextureUtil.getTextureScale(loc);
            float w = 1;
            float h = 1;
            if (loc == MISSING_YOUTUBE_THUMBNAIL_TEXTURE) {
                var st = FNMath.scale(120, 90);
                w = (float) st.getX();
                h = (float) st.getY();
            } else if (scale != null) {
                w = (float) scale.w();
                h = (float) scale.h();
            }
            return Pair.of(loc, new Vec2(w, h));
        } else if (imageInfo.getImageType() == ImageInfo.ImageType.SOUND_CLOUD_ARTWORK) {
            var idf = imageInfo.getIdentifier();
            synchronized (soundCloudArtworkURLs) {
                if (soundCloudArtworkURLs.containsKey(idf)) {
                    var url = soundCloudArtworkURLs.get(idf);
                    if (url != null) {
                        var loc = OETextureUtil.getURLTextureAsyncLoad(url, cash, MISSING_SOUND_CLOUD_ARTWORK_TEXTURE);
                        var scale = OETextureUtil.getTextureScale(loc);
                        float w = 1;
                        float h = 1;
                        if (scale != null) {
                            w = (float) scale.w();
                            h = (float) scale.h();
                        }
                        return Pair.of(loc, new Vec2(w, h));
                    }
                    return Pair.of(MISSING_SOUND_CLOUD_ARTWORK_TEXTURE, new Vec2(1, 1));
                }
                soundCloudArtworkURLs.put(idf, null);
                var lt = new SoundCloudImageLoadThread(idf);
                lt.start();
                return Pair.of(MISSING_SOUND_CLOUD_ARTWORK_TEXTURE, new Vec2(1, 1));
            }
        }
        return null;
    }

    private class SoundCloudImageLoadThread extends Thread {
        private final String url;

        public SoundCloudImageLoadThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            String src;
            try {
                var doc = Jsoup.connect(url).get();
                var img = doc.select("img").attr("itemprop", "image").first();
                src = img.attr("src");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            synchronized (soundCloudArtworkURLs) {
                soundCloudArtworkURLs.put(url, src);
            }
        }
    }
}
