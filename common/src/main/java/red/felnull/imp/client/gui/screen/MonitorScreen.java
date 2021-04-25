package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import red.felnull.otyacraftengine.client.gui.screen.IkisugiContainerScreen;

public abstract class MonitorScreen extends Screen {
    protected final IkisugiContainerScreen<?> screen;
    private boolean active;

    protected MonitorScreen(Component component, IkisugiContainerScreen<?> screen) {
        super(component);
        this.screen = screen;
    }

    public void enabled() {
        resize(screen.getMinecraft(), screen.width, screen.height);
        active = true;
        buttons.forEach(n -> {
            n.visible = true;
            n.active = true;
        });
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

}
