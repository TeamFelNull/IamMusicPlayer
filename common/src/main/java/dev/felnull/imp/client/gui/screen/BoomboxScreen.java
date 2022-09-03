package dev.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.BoomboxData;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.gui.components.BoomboxButton;
import dev.felnull.imp.client.gui.screen.monitor.boombox.BoomboxMonitor;
import dev.felnull.imp.inventory.BoomboxMenu;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.util.IMPItemUtil;
import dev.felnull.otyacraftengine.client.gui.screen.OEItemBEContainerBaseScreen;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BoomboxScreen extends OEItemBEContainerBaseScreen<BoomboxMenu> {
    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/boombox_base.png");
    public static final ResourceLocation EMPTY_CASSETTE_TAPE_SLOT = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/slot/cassette_tape_slot.png");
    public static final ResourceLocation EMPTY_ANTENNA_SLOT = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/slot/antenna_slot.png");
    private final Map<BoomboxData.MonitorType, BoomboxMonitor> monitors = new HashMap<>();
    protected BoomboxMonitor monitor;
    public long lastNoAntenna;

    public BoomboxScreen(BoomboxMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 214;
        this.imageHeight = 175;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new BoomboxButton(leftPos + 5, topPos + 17, BoomboxData.ButtonType.POWER, n -> {
            insPressButton(BoomboxData.ButtonType.POWER);
        }, this::getButtons, this::isPowered));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19, topPos + 17, BoomboxData.ButtonType.RADIO, n -> {
            if (!getAntenna().isEmpty() && IMPItemUtil.isAntenna(getAntenna())) {
                insPressButton(BoomboxData.ButtonType.RADIO);
            } else {
                lastNoAntenna = System.currentTimeMillis();
            }
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 2, topPos + 17, BoomboxData.ButtonType.START, n -> {
            insPressButton(BoomboxData.ButtonType.START);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 3, topPos + 17, BoomboxData.ButtonType.PAUSE, n -> {
            insPressButton(BoomboxData.ButtonType.PAUSE);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 4, topPos + 17, BoomboxData.ButtonType.STOP, n -> {
            insPressButton(BoomboxData.ButtonType.STOP);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 5, topPos + 17, BoomboxData.ButtonType.LOOP, n -> {
            insPressButton(BoomboxData.ButtonType.LOOP);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 6 + 14, topPos + 17, BoomboxData.ButtonType.VOL_DOWN, n -> {
            insPressButton(BoomboxData.ButtonType.VOL_DOWN);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 7 + 14, topPos + 17, BoomboxData.ButtonType.VOL_UP, n -> {
            insPressButton(BoomboxData.ButtonType.VOL_UP);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 8 + 14, topPos + 17, BoomboxData.ButtonType.VOL_MUTE, n -> {
            insPressButton(BoomboxData.ButtonType.VOL_MUTE);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 9 + 14, topPos + 17, BoomboxData.ButtonType.VOL_MAX, n -> {
            insPressButton(BoomboxData.ButtonType.VOL_MAX);
        }, this::getButtons));

        changeScreenMonitor(getRawMonitorType());
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {
        super.renderBg(poseStack, f, i, j);
        if (getCassetteTape().isEmpty())
            OERenderUtils.drawTexture(EMPTY_CASSETTE_TAPE_SLOT, poseStack, leftPos + 183, topPos + 98, 0, 0, 16, 16, 16, 16);
        if (getAntenna().isEmpty())
            OERenderUtils.drawTexture(EMPTY_ANTENNA_SLOT, poseStack, leftPos + 183, topPos + 124, 0, 0, 16, 16, 16, 16);

        if (monitor != null)
            monitor.render(poseStack, f, i, j);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (monitor != null) {
            if (getRawMonitorType() != monitor.getType())
                changeScreenMonitor(getRawMonitorType());
            monitor.tick();
        }
    }

    public void insSelectedPlayList(@Nullable UUID uuid) {
        var tag = new CompoundTag();
        if (uuid != null)
            tag.putUUID("pl", uuid);
        instruction("set_selected_play_list", tag);
    }

    public void insRadioUrl(String url) {
        var tag = new CompoundTag();
        tag.putString("url", url);
        instruction("set_radio_url", tag);
    }

    public void insSelectedMusic(@Nullable UUID musicId) {
        var tag = new CompoundTag();
        if (musicId != null)
            tag.putUUID("m", musicId);
        instruction("set_selected_music", tag);
    }

    public boolean isMute() {
        return getBoomBoxData().isMute();
    }

    public boolean isPlaying() {
        return getBoomBoxData().isPlaying();
    }

    public int getVolume() {
        return getBoomBoxData().getVolume();
    }

    public long getMusicPosition() {
        return getBoomBoxData().getMusicPosition();
    }

    private boolean isPowered() {
        return getBoomBoxData().isPowered();
    }

    public boolean isLoop() {
        return getBoomBoxData().isLoop();
    }

    public BoomboxData getBoomBoxData() {
        if (isBlock()) {
            if (getBlockEntity() instanceof BoomboxBlockEntity boomboxBlockEntity)
                return boomboxBlockEntity.getBoomboxData();
        }
        return BoomboxItem.getData(getItem());
    }

    public void insMonitor(BoomboxData.MonitorType monitorType) {
        var tag = new CompoundTag();
        tag.putString("name", monitorType.getName());
        instruction("set_monitor", tag);
    }

    public boolean isMusicLoading() {
        return getBoomBoxData().isLoadingMusic();
    }

    public ItemStack getCassetteTape() {
        return getMenu().getItems().get(0);
    }

    public ItemStack getAntenna() {
        return getMenu().getItems().get(1);
    }

    public void insVolume(int volume) {
        var tag = new CompoundTag();
        tag.putInt("volume", volume);
        instruction("set_volume", tag);
    }

    public void insPositionAndRestart(long position) {
        var tag = new CompoundTag();
        tag.putLong("position", position);
        instruction("restat_and_set_position", tag);
    }

    public void insContinuousType(@NotNull BoomboxData.ContinuousType continuousType) {
        var tag = new CompoundTag();
        tag.putString("type", continuousType.getName());
        instruction("set_continuous_type", tag);
    }

    public void insLoop(boolean loop) {
        var tag = new CompoundTag();
        tag.putBoolean("loop", loop);
        instruction("set_loop", tag);
    }

    public void insPause() {
        instruction("set_pause", new CompoundTag());
    }

    public void insPlaying(boolean playing) {
        var tag = new CompoundTag();
        tag.putBoolean("playing", playing);
        instruction("set_playing", tag);
    }

    public void insRadioSource(MusicSource source) {
        var tag = new CompoundTag();
        tag.put("source", source.createSavedTag());
        instruction("set_radio_source", tag);
    }

    public void insRadioImage(ImageInfo imageInfo) {
        var tag = new CompoundTag();
        tag.put("image", imageInfo.createSavedTag());
        instruction("set_radio_image", tag);
    }

    public void insRadioName(String name) {
        var tag = new CompoundTag();
        tag.putString("name", name);
        instruction("set_radio_name", tag);
    }

    public void insRadioAuthor(String author) {
        var tag = new CompoundTag();
        tag.putString("author", author);
        instruction("set_radio_author", tag);
    }

    @Override
    protected ResourceLocation getBackGrandTexture() {
        return BG_TEXTURE;
    }

    private BoomboxData.Buttons getButtons() {
        return getBoomBoxData().getButtons();
    }

    private void insPressButton(BoomboxData.ButtonType type) {
        var tag = new CompoundTag();
        tag.putString("Type", type.getName());
        instruction("buttons_press", tag);
    }

    private void changeScreenMonitor(BoomboxData.MonitorType type) {
        if (monitor != null) {
            monitor.renderables.forEach(n -> {
                if (n instanceof GuiEventListener guiEventListener)
                    removeWidget(guiEventListener);
            });
            monitor.depose();
        }

        if (!monitors.containsKey(type))
            monitors.put(type, BoomboxMonitor.createdBoomBoxMonitor(type, this));

        monitor = monitors.get(type);
        monitor.init(leftPos, topPos);
        monitor.renderables.forEach(n -> {
            if (n instanceof AbstractWidget widget)
                addRenderableWidget(widget);
        });
    }

    private BoomboxData.MonitorType getRawMonitorType() {
        return getBoomBoxData().getMonitorType();
    }

    @Override
    public void onClose() {
        super.onClose();
        if (monitor != null)
            monitor.depose();
    }

    @Override
    public void onFilesDrop(List<Path> list) {
        if (monitor != null)
            monitor.onFilesDrop(list);
    }

}
