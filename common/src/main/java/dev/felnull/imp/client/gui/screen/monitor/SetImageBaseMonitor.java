package dev.felnull.imp.client.gui.screen.monitor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.fnjl.util.FNURLUtil;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.ImageSetButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.client.util.FileChooserUtil;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import dev.felnull.otyacraftengine.util.OEImageUtil;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class SetImageBaseMonitor extends MusicManagerMonitor {
    private static final Gson GSON = new Gson();
    private static final ResourceLocation SET_IMAGE_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/set_image_base.png");
    private static final Component NO_IMAGE_TEXT = new TranslatableComponent("imp.text.noImage");
    private static final Component DROP_INFO_TEXT = new TranslatableComponent("imp.text.dropInfo");
    private EditBox imageUrlEditBox;
    private Component imageSetInfo;
    private ImageUrlLoader imageUrlLoader;
    private ImageUploader imageUploader;

    public SetImageBaseMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

        addRenderWidget(new ImageSetButton(getStartX() + 149, getStartY() + 14, ImageSetButton.ImageSetType.DELETE, n -> {
            setImage(ImageInfo.EMPTY);
        }, getScreen()));

        addRenderWidget(new ImageSetButton(getStartX() + 112, getStartY() + 14, ImageSetButton.ImageSetType.FILE_OPEN, n -> openImage(FileChooserUtil.openImageFileChooser(false)), getScreen()));

        addRenderWidget(new ImageSetButton(getStartX() + 75, getStartY() + 14, ImageSetButton.ImageSetType.PLAYER_FACE, n -> setImage(new ImageInfo(ImageInfo.ImageType.PLAYER_FACE, IIMPSmartRender.mc.player.getGameProfile().getName())), getScreen()));

        this.imageUrlEditBox = new EditBox(IIMPSmartRender.mc.font, getStartX() + 112, getStartY() + 34, 69, 12, new TranslatableComponent("imp.editBox.imageUrl"));
        this.imageUrlEditBox.setMaxLength(100);
        this.imageUrlEditBox.setValue(getImageURL());
        this.imageUrlEditBox.setResponder(this::setImageURL);
        addRenderWidget(this.imageUrlEditBox);

        addRenderWidget(new ImageSetButton(getStartX() + 75, getStartY() + 33, ImageSetButton.ImageSetType.URL, n -> {
            if (this.imageUrlEditBox.getValue().isEmpty()) {
                imageSetInfo = new TranslatableComponent("imp.text.imageLoad.empty");
                return;
            }
            startImageUrlLoad(this.imageUrlEditBox.getValue());
        }, getScreen()));
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(SET_IMAGE_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        if (!getImage().isEmpty()) {
            PlayImageRenderer.getInstance().draw(getImage(), poseStack, getStartX() + 6, getStartY() + 15, 64, false);
        } else {
            drawSmartCenterString(poseStack, NO_IMAGE_TEXT, getStartX() + 38, getStartY() + 43);
        }

        drawSmartString(poseStack, DROP_INFO_TEXT, getStartX() + 5, getStartY() + 82);
        if (imageSetInfo != null)
            drawSmartFixedWidthString(poseStack, imageSetInfo, getStartX() + 75, getStartY() + 51, 107);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtil.renderTextureSprite(SET_IMAGE_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.ADD_PLAY_LIST;
    }

    @Override
    public void depose() {
        super.depose();
        stopImageUrlLoad();
    }

    private ImageInfo getImage() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return musicManagerBlockEntity.getMyImage();
        return ImageInfo.EMPTY;
    }

    private void setImage(ImageInfo image) {
        getScreen().insImage(image);
    }

    private String getImageURL() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return musicManagerBlockEntity.getMyImageURL();
        return "";
    }

    private void setImageURL(String text) {
        getScreen().insImageURL(text);
    }

    private void startImageUrlLoad(String url) {
        stopImageUrlLoad();
        imageSetInfo = new TranslatableComponent("imp.text.imageLoad.loadingImage");
        imageUrlLoader = new ImageUrlLoader(url);
        imageUrlLoader.start();
    }

    private void stopImageUrlLoad() {
        if (imageUrlLoader != null) {
            imageUrlLoader.interrupt();
            imageUrlLoader = null;
        }
        imageSetInfo = null;
    }

    private void startImageUpload(byte[] data) {
        stopImageUpload();
        imageSetInfo = new TranslatableComponent("imp.text.imageLoad.loadingImage");
        imageUploader = new ImageUploader(data);
        imageUploader.start();
    }


    private void stopImageUpload() {
        if (imageUploader != null) {
            imageUploader.interrupt();
            imageUploader = null;
        }
        imageSetInfo = null;
    }

    private void openImage(File[] files) {
        System.out.println(files);
        if (files != null) {
            for (File file : files) {
                System.out.println(file.getAbsolutePath());
            }
        }
        if (files == null || files.length == 0) return;
        if (files.length != 1) {
            imageSetInfo = new TranslatableComponent("imp.text.imageLoad.tooManyImages");
            return;
        }
        imageSetInfo = null;
        File file = files[0];
    }

    private class ImageUrlLoader extends Thread {
        private final String url;

        private ImageUrlLoader(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                var urll = FNURLUtil.getConnection(new URL(url));
                long max = 3145728L;
                if (urll.getContentLengthLong() > max) {
                    startImageUpload(urll.getInputStream().readAllBytes());
                    return;
                }
                byte[] img = urll.getInputStream().readAllBytes();
                if (!OEImageUtil.isImage(img)) {
                    imageSetInfo = new TranslatableComponent("imp.text.imageLoad.notImageUrl");
                    return;
                }
                setImage(new ImageInfo(ImageInfo.ImageType.URL, url));
                imageSetInfo = null;
            } catch (Exception e) {
                imageSetInfo = new TranslatableComponent("imp.text.imageLoad.error", e.getLocalizedMessage());
            }
        }
    }

    private class ImageUploader extends Thread {
        private byte[] data;

        private ImageUploader(byte[] data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                if (!OEImageUtil.isImage(data)) {
                    imageSetInfo = new TranslatableComponent("imp.text.imageLoad.notImage");
                    return;
                }
                long max = 3145728L;
                if (data.length > max) {
                    imageSetInfo = new TranslatableComponent("imp.text.imageLoad.optimizationImage");
                    data = OEImageUtil.reductionSize(data, max - 100);
                }
                imageSetInfo = new TranslatableComponent("imp.text.imageLoad.uploadImage");
                Files.write(Paths.get("test.gif"), data);
                var url = uploadToImgur(data);
                setImage(new ImageInfo(ImageInfo.ImageType.URL, url));
                imageSetInfo = null;
            } catch (Exception e) {
                imageSetInfo = new TranslatableComponent("imp.text.imageLoad.error", e.getLocalizedMessage());
            }
        }
    }

    private String uploadToImgur(byte[] data) throws IOException, InterruptedException {
        HttpClient hc = HttpClient.newHttpClient();
        HttpRequest hr = HttpRequest.newBuilder(URI.create("https://api.imgur.com/3/image"))
                .POST(HttpRequest.BodyPublishers.ofByteArray(data))
                .header("Authorization", "Client-ID d33f23f7c189083")
                .build();
        HttpResponse<String> res = hc.send(hr, HttpResponse.BodyHandlers.ofString());
        JsonObject upData = GSON.fromJson(res.body(), JsonObject.class);
        return upData.getAsJsonObject("data").get("link").getAsString();
    }
}
