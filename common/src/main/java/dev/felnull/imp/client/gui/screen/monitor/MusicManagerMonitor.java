package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class MusicManagerMonitor extends Monitor<MusicManagerBlockEntity> {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final Map<MusicManagerBlockEntity.MonitorType, MonitorFactory> monitorFactory = new HashMap<>();
    protected static final ResourceLocation BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/background.png");
    public static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/widgets.png");
    public static final ResourceLocation BASE_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/base.png");
    protected boolean header = true;
    private final MusicManagerBlockEntity.MonitorType type;
    private final MusicManagerScreen screen;

    public MusicManagerMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(new TranslatableComponent("imp.monitor.music_manager." + type.getName()), 8, 20, 370, 199);
        this.type = type;
        this.screen = screen;
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        if (header && getParentType() != null) {
            addRenderWidget(new ImageButton(getStartX() + 356, getStartY(), 14, 10, 0, 0, 10, WIDGETS_TEXTURE, 256, 256, n -> {
                insMonitor(MusicManagerBlockEntity.MonitorType.PLAY_LIST);
            }, new TranslatableComponent("imp.button.close")));
            addRenderWidget(new ImageButton(getStartX() + 342, getStartY(), 14, 10, 14, 0, 10, WIDGETS_TEXTURE, 256, 256, n -> {
                insMonitor(getParentType());
            }, new TranslatableComponent("imp.button.backScreen")));
        }
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(BG_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
        if (header) {
            OERenderUtil.drawTexture(BASE_TEXTURE, poseStack, getStartX(), getStartY(), 0, 0, width, 10, width, height);
            drawSmartText(poseStack, title, getStartX() + 1, getStartY() + 1);
        }
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;

        OERenderUtil.renderTextureSprite(BG_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
        if (header) {
            OERenderUtil.renderTextureSprite(BASE_TEXTURE, poseStack, multiBufferSource, 0, monitorHeight - onPxH * 10, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, onPxH * 10, 0, 0, width, 10, width, height, i, j);
            if (getParentType() != null) {
                OERenderUtil.renderTextureSprite(WIDGETS_TEXTURE, poseStack, multiBufferSource, onPxW * 356, monitorHeight - onPxH * 10, OERenderUtil.MIN_BREADTH * 3, 0, 0, 0, onPxW * 14, onPxH * 10, 0, 0, 14, 10, 256, 256, i, j);
                OERenderUtil.renderTextureSprite(WIDGETS_TEXTURE, poseStack, multiBufferSource, onPxW * 342, monitorHeight - onPxH * 10, OERenderUtil.MIN_BREADTH * 3, 0, 0, 0, onPxW * 14, onPxH * 10, 14, 0, 14, 10, 256, 256, i, j);
            }
            renderSmartTextSprite(poseStack, multiBufferSource, title, 1, 2, OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);
        }
    }

    public static void firstInit() {
        registerMonitors(MusicManagerBlockEntity.MonitorType.OFF, OffMMMonitor::new);
        registerMonitors(MusicManagerBlockEntity.MonitorType.TEST, TestMMMonitor::new);
        registerMonitors(MusicManagerBlockEntity.MonitorType.PLAY_LIST, PlayListMMMonitor::new);
        registerMonitors(MusicManagerBlockEntity.MonitorType.ADD_PLAY_LIST, AddPlayListMMMonitor::new);
        registerMonitors(MusicManagerBlockEntity.MonitorType.CREATE_PLAY_LIST, CreatePlayListMMMonitor::new);
        registerMonitors(MusicManagerBlockEntity.MonitorType.ADD_MUSIC, AddMusicMMMonitor::new);
        registerMonitors(MusicManagerBlockEntity.MonitorType.SEARCH_MUSIC, SearchMusicMMMonitor::new);
    }

    public MusicManagerScreen getScreen() {
        return screen;
    }

    public static MusicManagerMonitor createdMusicMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        return monitorFactory.get(type).create(type, screen);
    }

    private static void registerMonitors(MusicManagerBlockEntity.MonitorType type, MonitorFactory factory) {
        monitorFactory.put(type, factory);
    }

    private static interface MonitorFactory {
        MusicManagerMonitor create(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen);
    }

    public MusicSyncManager getSyncManager() {
        return MusicSyncManager.getInstance();
    }

    public MusicEngine getMusicEngine() {
        return MusicEngine.getInstance();
    }

    public MusicManagerBlockEntity.MonitorType getType() {
        return type;
    }

    protected void insMonitor(MusicManagerBlockEntity.MonitorType type) {
        screen.insMonitor(type);
    }

    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return null;
    }

}
