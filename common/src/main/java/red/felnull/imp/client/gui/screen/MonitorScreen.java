package red.felnull.imp.client.gui.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import red.felnull.otyacraftengine.client.gui.screen.IkisugiContainerScreen;

public abstract class MonitorScreen extends Screen {
    private final IkisugiContainerScreen<?> screen;

    protected MonitorScreen(Component component, IkisugiContainerScreen<?> screen) {
        super(component);
        this.screen = screen;
    }

    public void enabled() {
        resize(screen.getMinecraft(), screen.width, screen.height);
    }

    public void disable() {

    }
}
