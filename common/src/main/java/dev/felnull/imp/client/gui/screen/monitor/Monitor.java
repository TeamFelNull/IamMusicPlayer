package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class Monitor<T extends BlockEntity> extends AbstractContainerEventHandler implements IIMPSmartRender {
    protected final List<GuiEventListener> children = new ArrayList<>();
    public final List<AbstractWidget> renderables = new ArrayList<>();
    protected Component title;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int leftPos;
    protected int topPos;

    public Monitor(Component title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected int getStartX() {
        return leftPos + x;
    }

    protected int getStartY() {
        return topPos + y;
    }

    public void init(int leftPos, int topPos) {
        this.leftPos = leftPos;
        this.topPos = topPos;
    }

    public void depose() {
        renderables.clear();
        this.leftPos = 0;
        this.topPos = 0;
    }

    public void render(GuiGraphics guiGraphics, float f, int mouseX, int mouseY) {

    }

    public void renderAppearance(T blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {

    }

    protected <W extends AbstractWidget> W addRenderWidget(W widget) {
        renderables.add(widget);
        return widget;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return children;
    }

    public void tick() {

    }

    public void onFilesDrop(List<Path> list) {

    }

    public PlayImageRenderer getPlayImageRenderer() {
        return PlayImageRenderer.getInstance();
    }

    public MusicSyncManager getSyncManager() {
        return MusicSyncManager.getInstance();
    }
}
