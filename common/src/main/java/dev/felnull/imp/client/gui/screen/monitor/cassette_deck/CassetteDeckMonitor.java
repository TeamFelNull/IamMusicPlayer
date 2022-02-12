package dev.felnull.imp.client.gui.screen.monitor.cassette_deck;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.gui.screen.CassetteDeckScreen;
import dev.felnull.imp.client.gui.screen.monitor.Monitor;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class CassetteDeckMonitor extends Monitor<CassetteDeckBlockEntity> {
    protected static final ResourceLocation BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck/monitor/background.png");
    private static final Map<CassetteDeckBlockEntity.MonitorType, MonitorFactory> monitorFactory = new HashMap<>();
    private final CassetteDeckBlockEntity.MonitorType monitorType;
    private final CassetteDeckScreen screen;

    public CassetteDeckMonitor(CassetteDeckBlockEntity.MonitorType monitorType, CassetteDeckScreen screen) {
        super(new TranslatableComponent("imp.monitor.cassette_deck." + monitorType.getName()), 7, 18, 200, 56);
        this.monitorType = monitorType;
        this.screen = screen;
    }

    public CassetteDeckBlockEntity.MonitorType getType() {
        return monitorType;
    }

    public CassetteDeckScreen getScreen() {
        return screen;
    }

    public static CassetteDeckMonitor createdCassetteDeckMonitor(CassetteDeckBlockEntity.MonitorType type, CassetteDeckScreen screen) {
        return monitorFactory.get(type).create(type, screen);
    }

    public BlockEntity getBlockEntity() {
        return getScreen().getBlockEntity();
    }

    public static void firstInit() {
        registerMonitors(CassetteDeckBlockEntity.MonitorType.OFF, OffCDMonitor::new);
        registerMonitors(CassetteDeckBlockEntity.MonitorType.MENU, MenuCDMonitor::new);
        registerMonitors(CassetteDeckBlockEntity.MonitorType.WRITE, WriteCDMonitor::new);
        registerMonitors(CassetteDeckBlockEntity.MonitorType.PLAYBACK, PlaybackCDMonitor::new);
        registerMonitors(CassetteDeckBlockEntity.MonitorType.WRITE_EXECUTION, WriteExecutionCDMonitor::new);
    }

    private static void registerMonitors(CassetteDeckBlockEntity.MonitorType type, MonitorFactory factory) {
        monitorFactory.put(type, factory);
    }

    private static interface MonitorFactory {
        CassetteDeckMonitor create(CassetteDeckBlockEntity.MonitorType type, CassetteDeckScreen screen);
    }

    public void insMonitor(CassetteDeckBlockEntity.MonitorType monitorType) {
        getScreen().insMonitor(monitorType);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(BG_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
    }

    @Override
    public void renderAppearance(CassetteDeckBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        OERenderUtil.renderTextureSprite(BG_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
    }
}
