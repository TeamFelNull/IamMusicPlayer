package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class MusicManagerMonitor extends Monitor<MusicManagerBlockEntity> {
    private static final Map<MusicManagerBlockEntity.MonitorType, MonitorFactory> monitorFactory = new HashMap<>();
    protected static final ResourceLocation BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/background.png");
    protected static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/widgets.png");
    protected static final ResourceLocation BASE_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/base.png");
    protected boolean header = true;
    private final MusicManagerBlockEntity.MonitorType type;
    private final MusicManagerScreen screen;

    public MusicManagerMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen, int leftPos, int topPos) {
        super(new TranslatableComponent("monitor.music_manager." + type.getName() + ".title"), 8, 20, 370, 199, leftPos, topPos);
        this.type = type;
        this.screen = screen;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(BG_TEXTURE, poseStack, leftPos + x, topPos + y, 0f, 0f, width, height, width, height);
        if (header)
            OERenderUtil.drawTexture(BASE_TEXTURE, poseStack, leftPos + x, topPos + y, 0, 0, width, 10, width, height);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        float onPxH = monitorHeight / (float) height;
        OERenderUtil.renderTextureSprite(BG_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        if (header)
            OERenderUtil.renderTextureSprite(BASE_TEXTURE, poseStack, multiBufferSource, 0, monitorHeight - onPxH * 10, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, onPxH * 10, 0, 0, width, 10, width, height, i, j);
    }

    public static void firstInit() {
        registerMonitors(MusicManagerBlockEntity.MonitorType.OFF, OffMMMonitor::new);
        registerMonitors(MusicManagerBlockEntity.MonitorType.TEST, TestMMMonitor::new);
        registerMonitors(MusicManagerBlockEntity.MonitorType.FIRST, FirstMMMonitor::new);
        registerMonitors(MusicManagerBlockEntity.MonitorType.PLAY_LIST, PlayListMMMonitor::new);
    }

    public MusicManagerScreen getScreen() {
        return screen;
    }

    public static MusicManagerMonitor createdMusicMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen, int leftPos, int topPos) {
        return monitorFactory.get(type).create(type, screen, leftPos, topPos);
    }

    private static void registerMonitors(MusicManagerBlockEntity.MonitorType type, MonitorFactory factory) {
        monitorFactory.put(type, factory);
    }

    private static interface MonitorFactory {
        MusicManagerMonitor create(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen, int leftPos, int topPos);
    }

    public MusicSyncManager getSyncManager() {
        return MusicSyncManager.getInstance();
    }

    public MusicManagerBlockEntity.MonitorType getType() {
        return type;
    }
}
