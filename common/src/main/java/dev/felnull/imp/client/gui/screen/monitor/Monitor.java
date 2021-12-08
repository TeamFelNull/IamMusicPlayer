package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class Monitor<T extends BlockEntity> extends AbstractContainerEventHandler {
    protected final List<GuiEventListener> children = new ArrayList<>();
    public final List<Widget> renderables = new ArrayList<>();
    protected Component title;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int leftPos;
    protected int topPos;

    public Monitor(Component title, int x, int y, int width, int height, int leftPos, int topPos) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.leftPos = leftPos;
        this.topPos = topPos;
    }

    public void init() {

    }

    public void depose() {

    }

    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {

    }

    public void renderAppearance(T blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {

    }

    protected <W extends Widget> W addRenderWidget(W widget) {
        renderables.add(widget);
        return widget;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return children;
    }

    public void tick() {

    }
}
