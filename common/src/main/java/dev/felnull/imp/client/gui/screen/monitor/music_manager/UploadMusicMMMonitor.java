package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mpatric.mp3agic.Mp3File;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.util.FileChooserUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import dev.felnull.otyacraftengine.util.FlagThread;
import dev.felnull.otyacraftengine.util.OEUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

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
    private static final int relayServerVersion = 1;
    private static final Component BACK_TEXT = Component.translatable("gui.back");
    private static final Component RELAY_SERVER_TEXT = Component.translatable("imp.text.relayServer");
    private static final Component CONNECTING_CHECKING = Component.translatable("imp.text.relayServer.connectingChecking");
    private static final Component DROP_INFO_TEXT = Component.translatable("imp.text.uploadDropInfo");
    private static final Component OPEN_FILE_TEXT = Component.translatable("imp.button.openFile");
    private static final Component UPLOADING_TEXT = Component.translatable("imp.text.relayServer.uploading");
    private static final Component WARNING_TEXT = Component.translatable("imp.text.relayServer.warning");
    private static final Component RESPONSIBILITY_TEXT = Component.translatable("imp.text.relayServer.responsibility");
    private static final Component HOW_TEXT = Component.translatable("imp.text.relayServer.how");
    private SmartButton openFileButton;
    private Component RELAY_SERVER_NAME_TEXT;
    private Component UPLOAD_INFO_TEXT;
    private Component UPLOAD_ERROR_TEXT;
    private boolean connected;
    private Component SERVER_STATUS_TEXT;
    private ServerConnectingCheckThread connectingCheckThread;
    private UploadThread uploadThread;
    private long maxFileSize;
    private String uploadUrl;
    private boolean error;

    public UploadMusicMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        startConnectingCheck();
        this.addRenderWidget(new SmartButton(getStartX() + (width - 270) / 2, getStartY() + 180, 270, 15, BACK_TEXT, n -> {
            insMonitor(getParentType());
        }));

        this.openFileButton = this.addRenderWidget(new SmartButton(getStartX() + (width - 270) / 2, getStartY() + 103, 270, 15, OPEN_FILE_TEXT, n -> {
            uploadFile(FileChooserUtils.openMusicFileChooser(false));
        }));
        this.openFileButton.setIcon(WIDGETS_TEXTURE, 73, 19, 11, 11);
        this.openFileButton.visible = canUpload();
    }

    @Override
    public void depose() {
        super.depose();
        stopConnectingCheckThread();
        stopUploadThread();

        RELAY_SERVER_NAME_TEXT = null;
        UPLOAD_INFO_TEXT = null;
        UPLOAD_ERROR_TEXT = null;
        connected = false;
        error = false;
        SERVER_STATUS_TEXT = null;
        uploadUrl = null;
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
            UPLOAD_ERROR_TEXT = Component.translatable("imp.text.fileUpload.tooManyFiles");
            return;
        }
        UPLOAD_ERROR_TEXT = null;
        File file = files[0];
        if (file.isDirectory()) {
            UPLOAD_ERROR_TEXT = Component.translatable("imp.text.fileUpload.directory");
            return;
        }
        if (file.exists()) {
            if (file.length() > maxFileSize) {
                UPLOAD_ERROR_TEXT = Component.translatable("imp.text.fileUpload.sizeOver");
                return;
            }
            startUploadThread(file);
        } else {
            UPLOAD_ERROR_TEXT = Component.translatable("imp.text.fileUpload.fileNotFound");
        }
    }

    private boolean isConnectChecking() {
        return this.connectingCheckThread != null && this.connectingCheckThread.isAlive();
    }

    private boolean canUpload() {
        return !isConnectChecking() && connected && uploadUrl != null;
    }

    private boolean isUploading() {
        return uploadThread != null && uploadThread.isAlive();
    }

    @Override
    public void render(GuiGraphics guiGraphics, float f, int mouseX, int mouseY) {
        super.render(guiGraphics, f, mouseX, mouseY);
        float st = getStartX() + ((float) width - 270f) / 2f;
        drawSmartText(guiGraphics, RELAY_SERVER_TEXT, st, getStartY() + 13);

        if (RELAY_SERVER_NAME_TEXT != null)
            drawSmartText(guiGraphics, RELAY_SERVER_NAME_TEXT, st, getStartY() + 23, 0xFF008000);

        var tx = SERVER_STATUS_TEXT;
        int py = error ? 0 : 10;
        if (isConnectChecking()) {
            tx = CONNECTING_CHECKING;
            py = 0;
        }
        if (tx != null) drawSmartFixedWidthText(guiGraphics, tx, st, getStartY() + 23 + py, 270);

        if (canUpload()) {
            if (!isUploading()) drawSmartText(guiGraphics, DROP_INFO_TEXT, st, getStartY() + 120);
            if (UPLOAD_INFO_TEXT != null) drawSmartText(guiGraphics, UPLOAD_INFO_TEXT, st, getStartY() + 43);
            if (UPLOAD_ERROR_TEXT != null && !isUploading())
                drawSmartText(guiGraphics, UPLOAD_ERROR_TEXT, st, getStartY() + 83, 0xFFFF0000);

            drawSmartFixedWidthText(guiGraphics, HOW_TEXT, st, getStartY() + 53, 270, 0xFF0000FF);
            drawSmartFixedWidthText(guiGraphics, WARNING_TEXT, st, getStartY() + 63, 270, 0xFFFF0000);
            drawSmartFixedWidthText(guiGraphics, RESPONSIBILITY_TEXT, st, getStartY() + 73, 270, 0xFFFF0000);

            if (isUploading()) drawSmartText(guiGraphics, UPLOADING_TEXT, st, getStartY() + 83);
        }
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;

        renderSmartButtonSprite(poseStack, multiBufferSource, ((float) width - 270f) / 2f, 180, OERenderUtils.MIN_BREADTH * 2, 270, 15, i, j, onPxW, onPxH, monitorHeight, BACK_TEXT, true, false);

        float st = ((float) width - 270f) / 2f;
        renderSmartTextSprite(poseStack, multiBufferSource, RELAY_SERVER_TEXT, st, 13, OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);
        // renderSmartTextSpriteColorSprite(poseStack, multiBufferSource, Component.literal(getRelayServerURL()), st, 23, OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, 0xFF008000, i);

    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.ADD_MUSIC;
    }

    private void startConnectingCheck() {
        stopConnectingCheckThread();
        connected = false;
        error = false;
        connectingCheckThread = new ServerConnectingCheckThread();
        connectingCheckThread.start();
    }

    private void stopConnectingCheckThread() {
        if (connectingCheckThread != null) {
            connectingCheckThread.stopped();
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
            uploadThread.stopped();
            uploadThread = null;
        }
    }

    @Override
    public void tick() {
        super.tick();
        openFileButton.visible = canUpload() && !isUploading();
    }

   /* private class ServerConnectingCheckThreadOld extends Thread {
        @Override
        public void run() {
            try {
                long st = System.currentTimeMillis();
                var res = OEURLUtil.getJson(new URL(getRelayServerURL() + "/status"));
                var time = res.getAsJsonObject("Time");
                long rt = time.get("ResponseSpeed").getAsLong();
                connected = true;
                SERVER_STATUS_TEXT = Component.translatable("imp.text.relayServer.response", (System.currentTimeMillis() - st), rt);
                maxFileSize = 1024 * 1024 * 8;
                UPLOAD_INFO_TEXT = Component.translatable("imp.text.relayServer.uploadInfo", FNStringUtil.getByteDisplay(maxFileSize, 1024));
            } catch (Exception ex) {
                SERVER_STATUS_TEXT = Component.translatable("imp.text.relayServer.error", ex.getMessage()).withStyle(ChatFormatting.RED);
                ex.printStackTrace();
            }
        }
    }*/

    private class ServerConnectingCheckThread extends FlagThread {
        @Override
        public void run() {
            if (isStopped()) return;
            try {
                var url = IamMusicPlayer.getConfig().relayServerURL;
                String status = null;
                JsonObject lastJo = null;
                long eqTime = 0;
                while (status == null) {
                    if (isStopped()) return;
                    var jop = getResponse(url);
                    eqTime = jop.getLeft();
                    var jo = jop.getRight();
                    if (jo == null || !jo.has("Status")) {
                        status = "Offline";
                        lastJo = jo;
                    } else {
                        var st = jo.get("Status").getAsString();
                        if ("Transfer".equalsIgnoreCase(st)) {
                            var v = String.valueOf(relayServerVersion);
                            if (jo.has(v)) {
                                var vjo = jo.get(v).getAsJsonObject();
                                if (vjo.has("url")) {
                                    url = vjo.get("url").getAsString();
                                } else {
                                    status = "Transfer Failure";
                                    lastJo = jo;
                                }
                            } else {
                                status = "Transfer Failure";
                                lastJo = jo;
                            }
                        } else {
                            status = st;
                            lastJo = jo;
                        }
                    }
                }
                if (isStopped()) return;
                if ("Ok".equalsIgnoreCase(status)) {
                    var name = "No Name";
                    if (lastJo.has("Name")) name = lastJo.get("Name").getAsString();
                    RELAY_SERVER_NAME_TEXT = Component.literal(name);

                    if (isStopped()) return;
                    JsonObject time = null;
                    if (lastJo.has("Time")) time = lastJo.getAsJsonObject("Time");
                    long rt = 0;
                    if (time != null && time.has("ResponseSpeed")) rt = time.get("ResponseSpeed").getAsLong();
                    if (time != null && time.has("ResponseSpeed"))
                        SERVER_STATUS_TEXT = Component.translatable("imp.text.relayServer.response", eqTime, rt);
                    maxFileSize = lastJo.get("MaxFileSize").getAsLong();
                    UPLOAD_INFO_TEXT = Component.translatable("imp.text.relayServer.uploadInfo", FNStringUtil.getByteDisplay(maxFileSize, 1024));
                    String v = null;
                    if (isStopped()) return;
                    if (lastJo.has("Version")) v = lastJo.get("Version").getAsString();
                    if (v != null)
                        RELAY_SERVER_NAME_TEXT = Component.literal(RELAY_SERVER_NAME_TEXT.getString() + " V" + v);// ((LiteralContents) RELAY_SERVER_NAME_TEXT.getContents()).append(" V" + v);
                    uploadUrl = url;
                    connected = true;
                } else {
                    SERVER_STATUS_TEXT = Component.translatable("imp.text.relayServer.error", status).withStyle(ChatFormatting.RED);
                    error = true;
                }
            } catch (Exception ex) {
                SERVER_STATUS_TEXT = Component.translatable("imp.text.relayServer.error", ex.getMessage()).withStyle(ChatFormatting.RED);
                error = true;
            }
        }

        @NotNull
        private Pair<Long, JsonObject> getResponse(String url) {
            long st = System.currentTimeMillis();
            JsonObject jo = null;
            try {
                jo = OEUtils.readJson(new URL(url), JsonObject.class);
            } catch (IOException ignored) {
            }
           /* if (jo == null) {
                try {
                    st = System.currentTimeMillis();
                    jo = OEURLUtil.getJson(new URL(url + "status"));
                } catch (IOException ignored) {
                }
            }*/
            return Pair.of(System.currentTimeMillis() - st, jo);
        }
    }

    private void setMusicSourceName(String name) {
        getScreen().insMusicSourceName(name);
    }

    private void setCreateName(String name) {
        getScreen().insCreateName(name);
    }

    private class UploadThread extends FlagThread {
        private final File file;

        private UploadThread(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            if (isStopped()) return;
            try {
                var ujo = uploadToFile(Files.readAllBytes(file.toPath()));
                if (isStopped()) return;
                if (ujo == null) {
                    UPLOAD_ERROR_TEXT = Component.translatable("imp.text.fileUpload.error", "json is null");
                    return;
                }

                if (isStopped()) return;
                if (!ujo.has("url")) {
                    String error = "";
                    if (ujo.has("Error"))
                        error = ujo.get("Error").getAsString();
                    String msg = "";
                    if (ujo.has("Message"))
                        msg = ujo.get("Message").getAsString();

                    if (isStopped()) return;
                    UPLOAD_ERROR_TEXT = Component.translatable("imp.text.fileUpload.failure", error, msg);
                    return;
                }

                var url = ujo.get("url").getAsString();
                if (url == null || url.isEmpty()) {
                    UPLOAD_ERROR_TEXT = Component.translatable("imp.text.fileUpload.noURL");
                } else {
                    UPLOAD_ERROR_TEXT = null;
                    final byte[] img = getMusicImage(file);
                    IIMPSmartRender.mc.submit(() -> {
                        setMusicSourceName(url);
                        setCreateName(file.getName());
                        if (img != null) getScreen().musicFileImage = img;
                        insMonitor(getParentType());
                    });
                }
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                UPLOAD_ERROR_TEXT = Component.translatable("imp.text.fileUpload.error", e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    private JsonObject uploadToFile(byte[] data) throws IOException, InterruptedException {
        if (uploadUrl == null) return null;
        var url = uploadUrl + "music-upload";
        var client = HttpClient.newHttpClient();
        var req = HttpRequest.newBuilder(URI.create(url)).header("mc-uuid", IIMPSmartRender.mc.player.getGameProfile().getId().toString()).POST(HttpRequest.BodyPublishers.ofByteArray(data)).build();
        var res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(res.body(), JsonObject.class);
    }
    //{"Error":"There was a problem processing the server, please upload after a while","Message":""}

    private byte[] getMusicImage(File file) {
        try {
            var mp3 = new Mp3File(file);
            return mp3.getId3v2Tag().getAlbumImage();
        } catch (Exception ignored) {
        }
        return null;
    }
}
