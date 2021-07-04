package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import red.felnull.otyacraftengine.client.gui.components.IIkisugibleWidget;
import red.felnull.otyacraftengine.client.gui.screen.IkisugiContainerScreen;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Monitor<PS extends IkisugiContainerScreen<?>> extends AbstractContainerEventHandler implements NarratableEntry, IIkisugibleWidget {
    protected final List<GuiEventListener> children = new ArrayList<>();
    protected final List<AbstractWidget> buttons = new ArrayList<>();
    protected final PS parentScreen;
    protected final Component title;
    public int x;
    public int y;
    public int width;
    public int height;
    private boolean active;
    private Monitor<?> beforemonitor;

    public Monitor(Component component, PS parentScreen, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parentScreen = parentScreen;
        this.title = component;
    }

    public void init() {
        active = true;
    }

    public void render(PoseStack poseStack, int mousX, int mousY, float parTick) {
        renderBg(poseStack, mousX, mousY, parTick);
        buttons.forEach(n -> n.render(poseStack, mousX, mousY, parTick));

        children.stream().filter(n -> n instanceof EditBox).filter(n -> ((EditBox) n).visible).forEach(n -> ((EditBox) n).render(poseStack, mousX, mousY, parTick));
    }

    public void renderBg(PoseStack poseStack, int mousX, int mousY, float parTick) {

    }

    public Component getTitle() {
        return this.title;
    }

    public String getNarrationMessage() {
        return this.getTitle().getString();
    }

    protected <T extends GuiEventListener> T addWidget(T guiEventListener) {
        this.children.add(guiEventListener);
        return guiEventListener;
    }

    protected <T extends AbstractWidget> T addRenderableWidget(T abstractWidget) {
        this.buttons.add(abstractWidget);
        return this.addWidget(abstractWidget);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return this.children;
    }

    public PS getParentScreen() {
        return parentScreen;
    }

    public void disable() {
        active = false;
        buttons.forEach(n -> {
            n.visible = false;
            n.active = false;
        });
    }

    public boolean isActive() {
        return active;
    }

    public void tick() {
        children.stream().filter(n -> n instanceof EditBox).filter(n -> ((EditBox) n).active).forEach(n -> ((EditBox) n).tick());
    }

    @Override
    public boolean charTyped(char c, int i) {
        return super.charTyped(c, i);
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean keyPressed(int i, int j, int k) {

        boolean all = children.stream().filter(n -> n instanceof EditBox).anyMatch(n -> ((EditBox) n).canConsumeInput() && ((EditBox) n).active);

        return super.keyPressed(i, j, k) || all;
    }

    public void onFilesDrop(List<Path> list) {

    }

    public Monitor<?> getBeforeMonitor() {
        return beforemonitor;
    }

    public void setBeforeMonitor(Monitor<?> beforemonitor) {
        this.beforemonitor = beforemonitor;
    }
}
