package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.BoomboxData;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.client.gui.screen.monitor.Monitor;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class BoomboxMonitor extends Monitor<BoomboxBlockEntity> {
    private static final Map<BoomboxData.MonitorType, MonitorFactory> monitorFactory = new HashMap<>();
    protected static final ResourceLocation BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/background.png");
    private final BoomboxData.MonitorType monitorType;
    private final BoomboxScreen screen;

    public BoomboxMonitor(BoomboxData.MonitorType monitorType, BoomboxScreen screen) {
        super(Component.translatable("imp.monitor.boombox." + monitorType.getName()), 7, 36, 200, 37);
        this.monitorType = monitorType;
        this.screen = screen;
    }

    public static void firstInit() {
        registerMonitors(BoomboxData.MonitorType.OFF, OffBMonitor::new);
        registerMonitors(BoomboxData.MonitorType.PLAYBACK, PlaybackBMonitor::new);
        registerMonitors(BoomboxData.MonitorType.RADIO, RadioBMonitor::new);
        registerMonitors(BoomboxData.MonitorType.REMOTE_PLAYBACK, RemotePlayBackBMonitor::new);
        registerMonitors(BoomboxData.MonitorType.RADIO_SELECT, RadioSelectBMonitor::new);
        registerMonitors(BoomboxData.MonitorType.REMOTE_PLAYBACK_SELECT, RemotePlayBackSelectBMonitor::new);
    }

    protected ImageInfo getRadioImage() {
        return getScreen().getBoomBoxData().getRadioImage();
    }

    public static BoomboxMonitor createdBoomBoxMonitor(BoomboxData.MonitorType type, BoomboxScreen screen) {
        return monitorFactory.get(type).create(type, screen);
    }

    private static void registerMonitors(BoomboxData.MonitorType type, MonitorFactory factory) {
        monitorFactory.put(type, factory);
    }

    public void setMonitor(BoomboxData.MonitorType monitorType) {
        getScreen().insMonitor(monitorType);
    }

    public String getRadioUrl() {
        return getScreen().getBoomBoxData().getRadioUrl();
    }

    public String getRadioUrl(BoomboxData data) {
        return data.getRadioUrl();
    }

    public BoomboxScreen getScreen() {
        return screen;
    }

    public BoomboxData.MonitorType getType() {
        return monitorType;
    }

    @Override
    public void render(GuiGraphics guiGraphics, float f, int mouseX, int mouseY) {
        super.render(guiGraphics, f, mouseX, mouseY);
        OERenderUtils.drawTexture(BG_TEXTURE, guiGraphics.pose(), getStartX(), getStartY(), 0f, 0f, width, height, width, height);
    }

    public void renderAppearance(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight, BoomboxData data) {
        OERenderUtils.renderTextureSprite(BG_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtils.MIN_BREADTH, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
    }

    @Override
    public float getDefaultRenderTextScale() {
        return 2.5f;
    }

    public ItemStack getCassetteTape() {
        return getScreen().getCassetteTape();
    }

    public MusicSource getRadioSource(BoomboxData data) {
        return data.getRadioSource();
    }

    public MusicSource getRadioSource() {
        return getScreen().getBoomBoxData().getRadioSource();
    }

    private static interface MonitorFactory {
        BoomboxMonitor create(BoomboxData.MonitorType type, BoomboxScreen screen);
    }
}
