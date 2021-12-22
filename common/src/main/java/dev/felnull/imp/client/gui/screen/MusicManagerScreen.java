package dev.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.PowerButton;
import dev.felnull.imp.client.gui.screen.monitor.MusicManagerMonitor;
import dev.felnull.imp.inventory.MusicManagerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MusicManagerScreen extends IMPBaseContainerScreen<MusicManagerMenu> {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/music_manager_base.png");
    private final Map<MusicManagerBlockEntity.MonitorType, MusicManagerMonitor> monitors = new HashMap<>();
    protected MusicManagerMonitor monitor;

    public MusicManagerScreen(MusicManagerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 386;
        this.imageHeight = 227;
        this.bgTextureWidth = 512;
        this.bgTextureHeight = 512;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new PowerButton(this, leftPos + 368, topPos + 4, 12, 12, 386, 0, BG_TEXTURE, bgTextureWidth, bgTextureHeight));
        changeScreenMonitor(getBEMonitorType());
        insMonitor(getBEMonitorType());
    }

    public void insMonitor(MusicManagerBlockEntity.MonitorType type) {
        var tag = new CompoundTag();
        tag.putString("type", type.getName());
        instruction("set_monitor", 0, tag);
    }

    public void insAddPlayList(UUID playListId) {
        var tag = new CompoundTag();
        tag.putUUID("playlist", playListId);
        instruction("add_playlist", 0, tag);
    }

    private void changeScreenMonitor(MusicManagerBlockEntity.MonitorType type) {
        if (monitor != null) {
            monitor.renderables.forEach(n -> {
                if (n instanceof GuiEventListener guiEventListener)
                    removeWidget(guiEventListener);
            });
            monitor.depose();
        }

        if (!monitors.containsKey(type))
            monitors.put(type, MusicManagerMonitor.createdMusicMonitor(type, this));

        monitor = monitors.get(type);
        monitor.init(leftPos, topPos);
        monitor.renderables.forEach(n -> {
            if (n instanceof AbstractWidget widget)
                addRenderableWidget(widget);
        });
    }

    private MusicManagerBlockEntity.MonitorType getBEMonitorType() {
        if (getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return musicManagerBlockEntity.getMyMonitor(mc.player);
        return MusicManagerBlockEntity.MonitorType.OFF;
    }

    @Override
    protected ResourceLocation getBackGrandTexture() {
        return BG_TEXTURE;
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int i, int j) {
        this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {
        super.renderBg(poseStack, f, i, j);
        if (monitor != null)
            monitor.render(poseStack, f, i, j);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (monitor != null) {
            if (getBEMonitorType() != monitor.getType())
                changeScreenMonitor(getBEMonitorType());
            monitor.tick();
        }
    }
}
