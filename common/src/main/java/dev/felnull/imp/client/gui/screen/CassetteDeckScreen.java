package dev.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.CassetteDeckBlockEntity;
import dev.felnull.imp.client.gui.components.PowerButton;
import dev.felnull.imp.client.gui.screen.monitor.cassette_deck.CassetteDeckMonitor;
import dev.felnull.imp.inventory.CassetteDeckMenu;
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
            OERenderUtil.drawTexture(EMPTY_CASSETTE_TAPE_SLOT, poseStack, leftPos + 183, topPos + 99, 0, 0, 16, 16, 16, 16);

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

    private void changeScreenMonitor(CassetteDeckBlockEntity.MonitorType type) {
        if (monitor != null) {
            monitor.renderables.forEach(n -> {
                if (n instanceof GuiEventListener guiEventListener)
                    removeWidget(guiEventListener);
            });
            monitor.depose();
        }

        if (!monitors.containsKey(type))
            monitors.put(type, CassetteDeckMonitor.createdCassetteDeckMonitor(type, this));

        monitor = monitors.get(type);
        monitor.init(leftPos, topPos);
        monitor.renderables.forEach(n -> {
            if (n instanceof AbstractWidget widget)
                addRenderableWidget(widget);
        });
    }

    public void insMonitor(CassetteDeckBlockEntity.MonitorType monitorType) {
        var tag = new CompoundTag();
        tag.putString("name", monitorType.getName());
        instruction("monitor", 0, tag);
    }

    public CassetteDeckBlockEntity.MonitorType getRawMonitorType() {
        if (getBlockEntity() instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity)
            return cassetteDeckBlockEntity.getMonitor();
        return CassetteDeckBlockEntity.MonitorType.OFF;
    }

    public ItemStack getCassetteTape() {
        return getMenu().getItems().get(0);
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
