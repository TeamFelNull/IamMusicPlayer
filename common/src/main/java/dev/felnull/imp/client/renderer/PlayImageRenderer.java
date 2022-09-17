package dev.felnull.imp.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.fnjl.util.FNMath;
import dev.felnull.fnjl.util.FNURLUtil;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import dev.felnull.otyacraftengine.client.util.OETextureUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.lang3.tuple.Pair;

import java.net.URL;
import java.util.regex.Pattern;

public class PlayImageRenderer {
    private static final ResourceLocation MISSING_YOUTUBE_THUMBNAIL_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/image/missing_youtube_thumbnail.png");
    private static final ResourceLocation MISSING_SOUND_CLOUD_THUMBNAIL_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/image/missing_sound_cloud_artwork.png");
    private static final PlayImageRenderer INSTANCE = new PlayImageRenderer();
    private static final String YOUTUBE_THUMBNAIL_URL = "https://i.ytimg.com/vi/%s/hqdefault.jpg";
    private static final Pattern YOUTUBE_THUMBNAIL_URL_REGEX = Pattern.compile("https://i.ytimg.com/vi/.+/hqdefault.jpg");
    private static final Pattern SOUND_CLOUD_URL_REGEX = Pattern.compile("https://soundcloud.com/.+");
    private static final String SCT_IMG_ST = "<img ";
    private static final String SCT_IMG_END = ">";
    private static final String SCT_SRC_ST = "src=\"";
    private static final String SCT_SRC_END = "\"";

    public static PlayImageRenderer getInstance() {
        return INSTANCE;
    }

    public String getSwapURL(String url) {
        if (url.startsWith("imp_sct_")) {
            var scurl = url.substring("imp_sct_".length());
            if (SOUND_CLOUD_URL_REGEX.matcher(scurl).matches()) {
                String ret = null;
                try {
                    ret = extractSoundCloudImage(FNURLUtil.getResponse(new URL(scurl)));
                } catch (Exception ignored) {
                }
                return ret;
            }
        }
        return null;
    }

    public boolean isAllowURL(String url) {
        if (YOUTUBE_THUMBNAIL_URL_REGEX.matcher(url).matches()) return true;
        if (url.startsWith("imp_sct_")) {
            var scurl = url.substring("imp_sct_".length());
            return SOUND_CLOUD_URL_REGEX.matcher(scurl).matches();
        }
        return false;
    }

    public void draw(ImageInfo imageInfo, PoseStack poseStack, float x, float y, int size) {
        draw(imageInfo, poseStack, x, y, size, true);
    }

    public void draw(ImageInfo imageInfo, PoseStack poseStack, float x, float y, float size, boolean cash) {
        if (imageInfo.isEmpty()) return;

        if (imageInfo.getImageType() == ImageInfo.ImageType.PLAYER_FACE) {
            drawPlayerFaceImage(imageInfo.getIdentifier(), poseStack, x, y, size);
            return;
        }
        var td = getImageTexture(imageInfo, cash);
        if (td != null) drawTextureImage(td.getLeft(), poseStack, td.getRight().x, td.getRight().y, x, y, size);
    }

    public void drawPlayerFaceImage(String name, PoseStack poseStack, float x, float y, float size) {
        var texture = OETextureUtils.getPlayerSkinTexture(name);
        OERenderUtils.drawTexture(texture, poseStack, x, y, size, size, size, size, size * 8, size * 8);
        OERenderUtils.drawTexture(texture, poseStack, x, y, size * 5, size, size, size, size * 8, size * 8);
    }

    private void drawTextureImage(ResourceLocation location, PoseStack poseStack, float wScale, float hScale, float x, float y, float size) {
        if (location == null) return;
        float w = size * wScale;
        float h = size * hScale;
        OERenderUtils.drawTexture(location, poseStack, x + (size - w) / 2, y + (size - h) / 2, 0, 0, w, h, w, h);
    }

    public void renderSprite(ImageInfo imageInfo, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float size, int i, int j) {
        renderSprite(imageInfo, poseStack, multiBufferSource, x, y, z, size, i, j, true);
    }

    public void renderSprite(ImageInfo imageInfo, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float size, int i, int j, boolean cash) {
        if (imageInfo.isEmpty()) return;
        if (imageInfo.getImageType() == ImageInfo.ImageType.PLAYER_FACE) {
            renderPlayerFaceImageSprite(imageInfo.getIdentifier(), poseStack, multiBufferSource, x, y, z, size, i, j);
            return;
        }
        var td = getImageTexture(imageInfo, cash);
        if (td != null)
            renderTextureImageSprite(td.getLeft(), poseStack, multiBufferSource, td.getRight().x, td.getRight().y, x, y, z, size, i, j);
    }

    private void renderPlayerFaceImageSprite(String name, PoseStack poseStack, MultiBufferSource multiBufferSource, float x, float y, float z, float size, int i, int j) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        OERenderUtils.renderPlayerFaceSprite(poseStack, multiBufferSource, name, size, i, j);
        poseStack.popPose();
    }

    private void renderTextureImageSprite(ResourceLocation location, PoseStack poseStack, MultiBufferSource multiBufferSource, float wScale, float hScale, float x, float y, float z, float size, int i, int j) {
        if (location == null) return;
        float w = size * wScale;
        float h = size * hScale;
        OERenderUtils.renderTextureSprite(location, poseStack, multiBufferSource, x + (size - w) / 2, y + (size - h) / 2, z, 0, 0, 0, w, h, 0, 0, w, h, w, h, i, j);
    }


    private Pair<ResourceLocation, Vec2> getImageTexture(ImageInfo imageInfo, boolean cash) {
        if (imageInfo.getImageType() == ImageInfo.ImageType.URL) {
            var loc = OETextureUtils.getAndLoadURLTextureAsync(imageInfo.getIdentifier(), cash).of();
            var scale = OETextureUtils.getTextureScale(loc);
            float w = 1;
            float h = 1;
            if (scale != null) {
                w = (float) scale.w();
                h = (float) scale.h();
            }
            return Pair.of(loc, new Vec2(w, h));
        } else if (imageInfo.getImageType() == ImageInfo.ImageType.YOUTUBE_THUMBNAIL) {
            var loc = OETextureUtils.getAndLoadURLTextureAsync(String.format(YOUTUBE_THUMBNAIL_URL, imageInfo.getIdentifier()), cash).of(MISSING_YOUTUBE_THUMBNAIL_TEXTURE);
            var scale = OETextureUtils.getTextureScale(loc);
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
            var loc = OETextureUtils.getAndLoadURLTextureAsync("imp_sct_" + idf, cash).of(MISSING_SOUND_CLOUD_THUMBNAIL_TEXTURE);
            var scale = OETextureUtils.getTextureScale(loc);
            float w = 1;
            float h = 1;
            if (loc == MISSING_SOUND_CLOUD_THUMBNAIL_TEXTURE) {
                var st = FNMath.scale(1, 1);
                w = (float) st.getX();
                h = (float) st.getY();
            } else if (scale != null) {
                w = (float) scale.w();
                h = (float) scale.h();
            }
            return Pair.of(loc, new Vec2(w, h));
        }
        return null;
    }


    private static String extractSoundCloudImage(String html) {
        var ts = html.substring(html.indexOf(SCT_IMG_ST) + SCT_IMG_ST.length());
        ts = ts.substring(0, ts.indexOf(SCT_IMG_END));
        ts = ts.substring(ts.indexOf(SCT_SRC_ST) + SCT_SRC_ST.length());
        ts = ts.substring(0, ts.indexOf(SCT_SRC_END));
        return ts;
    }
}
