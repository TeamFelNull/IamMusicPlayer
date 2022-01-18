package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.otyacraftengine.util.OEURLUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.net.URL;

public class UploadMusicMMMonitor extends MusicManagerMonitor {
    private static final String RELAY_SERVER_URL = "https://www.morimori0317.net/imp-relay-server";
    private static final Component BACK_TEXT = new TranslatableComponent("imp.button.back");
    private static final Component RELAY_SERVER_TEXT = new TranslatableComponent("imp.text.relayServer");
    private static final Component CONNECTING_CHECKING = new TranslatableComponent("imp.text.relayServer.connectingChecking");
    private Component RELAY_SERVER_URL_TEXT;
    private boolean connectingChecking;
    private Component SERVER_STATUS_TEXT;
    private ServerConnectingCheckThread connectingCheckThread;
    // private static final Component CONNECTING_CHECK_TEXT = new TranslatableComponent("imp.text.");

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
    }

    @Override
    public void depose() {
        super.depose();
        stopConnectingCheckThread();
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        float st = getStartX() + ((float) width - 270f) / 2f;
        drawSmartText(poseStack, RELAY_SERVER_TEXT, st, getStartY() + 13);
        drawSmartText(poseStack, RELAY_SERVER_URL_TEXT, st, getStartY() + 23, 0xFF008000);

        drawSmartFixedWidthText(poseStack, connectingChecking ? CONNECTING_CHECKING : SERVER_STATUS_TEXT, st, getStartY() + 33, 270);
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

    private class ServerConnectingCheckThread extends Thread {
        @Override
        public void run() {
            try {
                long st = System.currentTimeMillis();
                var res = OEURLUtil.getJson(new URL(getRelayServerURL() + "/status"));
                var time = res.getAsJsonObject("Time");
                long rt = time.get("ResponseSpeed").getAsLong();
                SERVER_STATUS_TEXT = new TranslatableComponent("imp.text.relayServer.response", (System.currentTimeMillis() - st), rt);
            } catch (Exception ex) {
                SERVER_STATUS_TEXT = new TranslatableComponent("imp.text.relayServer.error", ex.getMessage()).withStyle(ChatFormatting.RED);
                ex.printStackTrace();
            }

            connectingChecking = false;
        }
    }
}
