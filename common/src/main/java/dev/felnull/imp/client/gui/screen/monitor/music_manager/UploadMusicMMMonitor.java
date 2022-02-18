package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mpatric.mp3agic.Mp3File;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.util.FileChooserUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import dev.felnull.otyacraftengine.util.OEURLUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UploadMusicMMMonitor extends MusicManagerMonitor {
    private static final Gson GSON = new Gson();
    private static final String RELAY_SERVER_URL = "https://www.morimori0317.net/imp-relay-server";
    private static final Component BACK_TEXT = new TranslatableComponent("gui.back");
    private static final Component RELAY_SERVER_TEXT = new TranslatableComponent("imp.text.relayServer");
    private static final Component CONNECTING_CHECKING = new TranslatableComponent("imp.text.relayServer.connectingChecking");
    private static final Component DROP_INFO_TEXT = new TranslatableComponent("imp.text.uploadDropInfo");
    private static final Component OPEN_FILE_TEXT = new TranslatableComponent("imp.button.openFile");
    private static final Component UPLOADING_TEXT = new TranslatableComponent("imp.text.relayServer.uploading");
    private static final Component WARNING_TEXT = new TranslatableComponent("imp.text.relayServer.warning");
    private static final Component RESPONSIBILITY_TEXT = new TranslatableComponent("imp.text.relayServer.responsibility");
    private SmartButton openFileButton;
    private Component RELAY_SERVER_URL_TEXT;
    private Component UPLOAD_INFO_TEXT;
    private Component UPLOAD_ERROR_TEXT;
    private boolean connectingChecking;
    private boolean connected;
    private Component SERVER_STATUS_TEXT;
    private ServerConnectingCheckThread connectingCheckThread;
    private UploadThread uploadThread;
    private long maxFileSize;

    public UploadMusicMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        RELAY_SERVER_URL_TEXT = new TextComponent(getRelayServerURL());
        startConnectingCheck();
        this.addRenderWidget(new SmartButton(getStartX() + (width - 270) / 2, getStartY() + 180, 270, 15, BACK_TEXT, n -> {
            insMonitor(getParentType());
        }));

        this.openFileButton = this.addRenderWidget(new SmartButton(getStartX() + (width - 270) / 2, getStartY() + 103, 270, 15, OPEN_FILE_TEXT, n -> {
            uploadFile(FileChooserUtil.openMusicFileChooser(false));
        }));
        this.openFileButton.setIcon(WIDGETS_TEXTURE, 73, 19, 11, 11);
        this.openFileButton.visible = canUpload();
    }

    @Override
    public void depose() {
        super.depose();
        stopConnectingCheckThread();
        stopUploadThread();

        RELAY_SERVER_URL_TEXT = null;
        UPLOAD_INFO_TEXT = null;
        UPLOAD_ERROR_TEXT = null;
        connectingChecking = false;
        connected = false;
        SERVER_STATUS_TEXT = null;
    }

    @Override
    public void onFilesDrop(List<Path> list) {
        File[] files = new File[list.size()];
        for (int i = 0; i < list.size(); i++) {
            files[i] = list.get(i).toFile();
        }
        uploadFile(files);
    }

    private void uploadFile(File[] files) {
        if (!canUpload() || files == null || files.length == 0) return;
        if (files.length != 1) {
            UPLOAD_ERROR_TEXT = new TranslatableComponent("imp.text.fileUpload.tooManyFiles");
            return;
        }
        UPLOAD_ERROR_TEXT = null;
        File file = files[0];
        if (file.isDirectory()) {
            UPLOAD_ERROR_TEXT = new TranslatableComponent("imp.text.fileUpload.directory");
            return;
        }
        if (file.exists()) {
            if (file.length() > maxFileSize) {
                UPLOAD_ERROR_TEXT = new TranslatableComponent("imp.text.fileUpload.sizeOver");
                return;
            }
            startUploadThread(file);
        } else {
            UPLOAD_ERROR_TEXT = new TranslatableComponent("imp.text.fileUpload.fileNotFound");
        }
    }

    private boolean canUpload() {
        return !connectingChecking && connected;
    }

    private boolean isUploading() {
        return uploadThread != null && uploadThread.isAlive();
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        float st = getStartX() + ((float) width - 270f) / 2f;
        drawSmartText(poseStack, RELAY_SERVER_TEXT, st, getStartY() + 13);
        drawSmartText(poseStack, RELAY_SERVER_URL_TEXT, st, getStartY() + 23, 0xFF008000);


        drawSmartFixedWidthText(poseStack, connectingChecking ? CONNECTING_CHECKING : SERVER_STATUS_TEXT, st, getStartY() + 33, 270);

        if (canUpload()) {
            if (!isUploading())
                drawSmartText(poseStack, DROP_INFO_TEXT, st, getStartY() + 120);
            if (UPLOAD_INFO_TEXT != null)
                drawSmartText(poseStack, UPLOAD_INFO_TEXT, st, getStartY() + 43);
            if (UPLOAD_ERROR_TEXT != null && !isUploading())
                drawSmartText(poseStack, UPLOAD_ERROR_TEXT, st, getStartY() + 73, 0xFFFF0000);

            drawSmartFixedWidthText(poseStack, WARNING_TEXT, st, getStartY() + 53, 270, 0xFFFF0000);
            drawSmartFixedWidthText(poseStack, RESPONSIBILITY_TEXT, st, getStartY() + 63, 270, 0xFFFF0000);

            if (isUploading())
                drawSmartText(poseStack, UPLOADING_TEXT, st, getStartY() + 73);
        }
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;

        renderSmartButtonSprite(poseStack, multiBufferSource, ((float) width - 270f) / 2f, 180, OERenderUtil.MIN_BREADTH * 2, 270, 15, i, j, onPxW, onPxH, monitorHeight, BACK_TEXT, true, false);

        float st = ((float) width - 270f) / 2f;
        renderSmartTextSprite(poseStack, multiBufferSource, RELAY_SERVER_TEXT, st, 13, OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);
        renderSmartTextSpriteColorSprite(poseStack, multiBufferSource, new TextComponent(getRelayServerURL()), st, 23, OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, 0xFF008000, i);

    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.ADD_MUSIC;
    }

    private String getRelayServerURL() {
        return RELAY_SERVER_URL;
    }

    private void startConnectingCheck() {
        stopConnectingCheckThread();
        connected = false;
        connectingChecking = true;
        connectingCheckThread = new ServerConnectingCheckThread();
        connectingCheckThread.start();
    }

    private void stopConnectingCheckThread() {
        connectingChecking = false;
        if (connectingCheckThread != null) {
            connectingCheckThread.interrupt();
            connectingCheckThread = null;
        }
    }

    private void startUploadThread(File file) {
        stopUploadThread();
        uploadThread = new UploadThread(file);
        uploadThread.start();
    }

    private void stopUploadThread() {
        if (uploadThread != null) {
            uploadThread.interrupt();
            uploadThread = null;
        }
    }

    @Override
    public void tick() {
        super.tick();
        openFileButton.visible = canUpload() && !isUploading();
    }

    private class ServerConnectingCheckThread extends Thread {
        @Override
        public void run() {
            try {
                long st = System.currentTimeMillis();
                var res = OEURLUtil.getJson(new URL(getRelayServerURL() + "/status"));
                var time = res.getAsJsonObject("Time");
                long rt = time.get("ResponseSpeed").getAsLong();
                connected = true;
                SERVER_STATUS_TEXT = new TranslatableComponent("imp.text.relayServer.response", (System.currentTimeMillis() - st), rt);
                maxFileSize = 1024 * 1024 * 8;
                UPLOAD_INFO_TEXT = new TranslatableComponent("imp.text.relayServer.uploadInfo", FNStringUtil.getByteDisplay(maxFileSize, 1024));
            } catch (Exception ex) {
                SERVER_STATUS_TEXT = new TranslatableComponent("imp.text.relayServer.error", ex.getMessage()).withStyle(ChatFormatting.RED);
                ex.printStackTrace();
            }

            connectingChecking = false;
        }
    }

    private void setMusicSourceName(String name) {
        getScreen().insMusicSourceName(name);
    }

    private void setCreateName(String name) {
        getScreen().insCreateName(name);
    }

    private class UploadThread extends Thread {
        private final File file;

        private UploadThread(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            try {
                var url = uploadToFile(Files.readAllBytes(file.toPath()));
                if (url.isEmpty()) {
                    UPLOAD_ERROR_TEXT = new TranslatableComponent("imp.text.fileUpload.noURL");
                } else {
                    UPLOAD_ERROR_TEXT = null;
                    final byte[] img = getMusicImage(file);
                    IIMPSmartRender.mc.submit(() -> {
                        setMusicSourceName(url);
                        setCreateName(file.getName());
                        if (img != null)
                            getScreen().musicFileImage = img;
                        insMonitor(getParentType());
                    });
                }
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                UPLOAD_ERROR_TEXT = new TranslatableComponent("imp.text.fileUpload.error", e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    private String uploadToFile(byte[] data) throws IOException, InterruptedException {
        var url = getRelayServerURL() + "/music-upload";
        var client = HttpClient.newHttpClient();
        var req = HttpRequest.newBuilder(URI.create(url)).header("mc-uuid", IIMPSmartRender.mc.player.getGameProfile().getId().toString()).POST(HttpRequest.BodyPublishers.ofByteArray(data)).build();
        var res = client.send(req, HttpResponse.BodyHandlers.ofString());
        var jo = GSON.fromJson(res.body(), JsonObject.class);
        return jo.get("url").getAsString();
    }

    private byte[] getMusicImage(File file) {
        try {
            var mp3 = new Mp3File(file);
            return mp3.getId3v2Tag().getAlbumImage();
        } catch (Exception ignored) {
        }
        return null;
    }
}
