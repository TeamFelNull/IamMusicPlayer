package dev.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.gui.components.BoomboxButton;
import dev.felnull.imp.client.gui.screen.monitor.boombox.BoomboxMonitor;
import dev.felnull.imp.inventory.BoomboxMenu;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.imp.util.IMPItemUtil;
import dev.felnull.otyacraftengine.client.gui.screen.OEItemBEContainerBaseScreen;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoomboxScreen extends OEItemBEContainerBaseScreen<BoomboxMenu> {
    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/boombox_base.png");
    public static final ResourceLocation EMPTY_CASSETTE_TAPE_SLOT = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/slot/cassette_tape_slot.png");
    public static final ResourceLocation EMPTY_ANTENNA_SLOT = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/slot/antenna_slot.png");
    private final Map<BoomboxBlockEntity.MonitorType, BoomboxMonitor> monitors = new HashMap<>();
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
        this.addRenderableWidget(new BoomboxButton(leftPos + 5, topPos + 17, BoomboxBlockEntity.ButtonType.POWER, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.POWER);
        }, this::getButtons, this::isPower));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19, topPos + 17, BoomboxBlockEntity.ButtonType.RADIO, n -> {
            if (!getAntenna().isEmpty() && IMPItemUtil.isAntenna(getAntenna())) {
                insPressButton(BoomboxBlockEntity.ButtonType.RADIO);
            } else {
                lastNoAntenna = System.currentTimeMillis();
            }
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 2, topPos + 17, BoomboxBlockEntity.ButtonType.START, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.START);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 3, topPos + 17, BoomboxBlockEntity.ButtonType.PAUSE, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.PAUSE);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 4, topPos + 17, BoomboxBlockEntity.ButtonType.STOP, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.STOP);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 5, topPos + 17, BoomboxBlockEntity.ButtonType.LOOP, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.LOOP);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 6 + 14, topPos + 17, BoomboxBlockEntity.ButtonType.VOL_DOWN, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.VOL_DOWN);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 7 + 14, topPos + 17, BoomboxBlockEntity.ButtonType.VOL_UP, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.VOL_UP);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 8 + 14, topPos + 17, BoomboxBlockEntity.ButtonType.VOL_MUTE, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.VOL_MUTE);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 9 + 14, topPos + 17, BoomboxBlockEntity.ButtonType.VOL_MAX, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.VOL_MAX);
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
            OERenderUtil.drawTexture(EMPTY_CASSETTE_TAPE_SLOT, poseStack, leftPos + 183, topPos + 98, 0, 0, 16, 16, 16, 16);
        if (getAntenna().isEmpty())
            OERenderUtil.drawTexture(EMPTY_ANTENNA_SLOT, poseStack, leftPos + 183, topPos + 124, 0, 0, 16, 16, 16, 16);

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

    public boolean isMute() {
        if (isBlock()) {
            if (getBlockEntity() instanceof BoomboxBlockEntity boomboxBlockEntity)
                return boomboxBlockEntity.isMute();
        }
        return false;
    }

    public boolean isPlaying() {
        if (isBlock()) {
            if (getBlockEntity() instanceof BoomboxBlockEntity boomboxBlockEntity)
                return boomboxBlockEntity.isPlaying();
        }
        return false;
    }

    public int getVolume() {
        if (isBlock()) {
            if (getBlockEntity() instanceof BoomboxBlockEntity boomboxBlockEntity)
                return boomboxBlockEntity.getVolume();
        }
        return 0;
    }

    public long getMusicPosition() {
        if (isBlock()) {
            if (getBlockEntity() instanceof BoomboxBlockEntity boomboxBlockEntity)
                return boomboxBlockEntity.getMusicPosition();
        }
        return 0;
    }

    private boolean isPower() {
        if (isBlock()) {
            if (getBlockEntity() instanceof BoomboxBlockEntity boomboxBlockEntity)
                return boomboxBlockEntity.isPower();
        } else {
            return BoomboxItem.isPowerOn(getItem());
        }
        return false;
    }

    public boolean isLoop() {
        if (isBlock()) {
            if (getBlockEntity() instanceof BoomboxBlockEntity boomboxBlockEntity)
                return boomboxBlockEntity.isLoop();
        }
        return false;
    }

    public boolean isMusicLoading() {
        if (isBlock()) {
            if (getBlockEntity() instanceof BoomboxBlockEntity boomboxBlockEntity)
                return boomboxBlockEntity.isLoadingMusic();
        }
        return false;
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
        instruction("set_volume", 0, tag);
    }

    public void insPositionAndRestart(long position) {
        var tag = new CompoundTag();
        tag.putLong("position", position);
        instruction("restat_and_set_position", 0, tag);
    }


    public void insLoop(boolean loop) {
        var tag = new CompoundTag();
        tag.putBoolean("loop", loop);
        instruction("set_loop", 0, tag);
    }

    public void insPause() {
        instruction("set_pause", 0, new CompoundTag());
    }

    public void insPlaying(boolean playing) {
        var tag = new CompoundTag();
        tag.putBoolean("playing", playing);
        instruction("set_playing", 0, tag);
    }

    @Override
    protected ResourceLocation getBackGrandTexture() {
        return BG_TEXTURE;
    }

    private BoomboxBlockEntity.Buttons getButtons() {
        if (isBlock()) {
            if (getBlockEntity() instanceof BoomboxBlockEntity boomboxBlockEntity)
                return boomboxBlockEntity.getButtons();
        }
        return BoomboxItem.getButtons(getItem());
    }

    private void insPressButton(BoomboxBlockEntity.ButtonType type) {
        var tag = new CompoundTag();
        tag.putString("Type", type.getName());
        instruction("buttons_press", 0, tag);
    }

    private void changeScreenMonitor(BoomboxBlockEntity.MonitorType type) {
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

    private BoomboxBlockEntity.MonitorType getRawMonitorType() {
        if (getBlockEntity() instanceof BoomboxBlockEntity boomboxBlockEntity)
            return boomboxBlockEntity.getMonitorType();
        return BoomboxBlockEntity.MonitorType.OFF;
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
