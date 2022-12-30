package dev.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.gui.components.PowerButton;
import dev.felnull.imp.client.gui.screen.monitor.cassette_deck.CassetteDeckMonitor;
import dev.felnull.imp.inventory.CassetteDeckMenu;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CassetteDeckScreen extends IMPBaseContainerScreen<CassetteDeckMenu> {
    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/cassette_deck/cassette_deck_base.png");
    public static final ResourceLocation EMPTY_CASSETTE_TAPE_SLOT = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/slot/cassette_tape_slot.png");
    private final Map<CassetteDeckBlockEntity.MonitorType, CassetteDeckMonitor> monitors = new HashMap<>();
    protected CassetteDeckMonitor monitor;

    public CassetteDeckScreen(CassetteDeckMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 214;
        this.imageHeight = 176;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new PowerButton(this, leftPos + 185, topPos + 79, 12, 12, 214, 0, BG_TEXTURE, bgTextureWidth, bgTextureHeight));


        changeScreenMonitor(getRawMonitorType());
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {
        super.renderBg(poseStack, f, i, j);
        if (getCassetteTape().isEmpty())
            OERenderUtils.drawTexture(EMPTY_CASSETTE_TAPE_SLOT, poseStack, leftPos + 183, topPos + 99, 0, 0, 16, 16, 16, 16);

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

    public long getPosition() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return getPosition(cassetteDeckBlockEntity);
        return 0;
    }

    public long getPosition(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        return cassetteDeckBlockEntity.getPosition();
    }

    public boolean isPlaying() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return isPlaying(cassetteDeckBlockEntity);
        return false;
    }

    public boolean isPlaying(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        return cassetteDeckBlockEntity.isPlaying();
    }

    public void insPositionAndRestart(long position) {
        var tag = new CompoundTag();
        tag.putLong("position", position);
        instruction("restat_and_set_position", tag);
    }


    public void insLoop(boolean loop) {
        var tag = new CompoundTag();
        tag.putBoolean("loop", loop);
        instruction("set_loop", tag);
    }

    public boolean isLoop() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return isLoop(cassetteDeckBlockEntity);
        return false;
    }

    public boolean isLoop(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        return cassetteDeckBlockEntity.isLoop();
    }

    private void changeScreenMonitor(CassetteDeckBlockEntity.MonitorType type) {
        if (monitor != null) {
            monitor.renderables.forEach(this::removeWidget);
            monitor.depose();
        }

        if (!monitors.containsKey(type))
            monitors.put(type, CassetteDeckMonitor.createdCassetteDeckMonitor(type, this));

        monitor = monitors.get(type);
        monitor.init(leftPos, topPos);
        monitor.renderables.forEach(this::addRenderableWidget);
    }

    public void insPause() {
        instruction("set_pause", new CompoundTag());
    }

    public void insPlaying(boolean playing) {
        var tag = new CompoundTag();
        tag.putBoolean("playing", playing);
        instruction("set_playing", tag);
    }

    public void insVolume(int volume) {
        var tag = new CompoundTag();
        tag.putInt("volume", volume);
        instruction("set_volume", tag);
    }

    public void insMute(boolean mute) {
        var tag = new CompoundTag();
        tag.putBoolean("mute", mute);
        instruction("set_mute", tag);
    }

    public void insMonitor(CassetteDeckBlockEntity.MonitorType monitorType) {
        var tag = new CompoundTag();
        tag.putString("name", monitorType.getName());
        instruction("monitor", tag);
    }

    public void insSelectPlaylist(UUID uuid) {
        var tag = new CompoundTag();
        tag.putUUID("uuid", uuid);
        instruction("select_playlist", tag);
    }

    public void insMusic(UUID uuid) {
        var tag = new CompoundTag();
        tag.putUUID("music", uuid);
        instruction("set_music", tag);
    }

    public CassetteDeckBlockEntity.MonitorType getRawMonitorType() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return cassetteDeckBlockEntity.getMonitor();
        return CassetteDeckBlockEntity.MonitorType.OFF;
    }

    public ItemStack getCassetteTape() {
        return getMenu().getItems().get(0);
    }

    public boolean isLoading() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return isLoading(cassetteDeckBlockEntity);
        return false;
    }

    public boolean isLoading(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        return cassetteDeckBlockEntity.isLoadingMusic();
    }

    public int getVolume() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return getVolume(cassetteDeckBlockEntity);
        return 0;
    }

    public int getVolume(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        return cassetteDeckBlockEntity.getVolume();
    }

    public boolean isMute() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return isMute(cassetteDeckBlockEntity);
        return false;
    }

    public boolean isMute(CassetteDeckBlockEntity cassetteDeckBlockEntity) {
        return cassetteDeckBlockEntity.isMute();
    }

    @Override
    protected ResourceLocation getBackGrandTexture() {
        return BG_TEXTURE;
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
