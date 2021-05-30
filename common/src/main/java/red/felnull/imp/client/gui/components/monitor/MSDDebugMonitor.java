package red.felnull.imp.client.gui.components.monitor;

import net.minecraft.network.chat.TextComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.otyacraftengine.client.gui.components.TestFixedButtonsList;

import java.util.ArrayList;
import java.util.List;

public class MSDDebugMonitor extends MSDBaseMonitor {
    public final List<String> goToScreen = new ArrayList<>();

    public MSDDebugMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TextComponent("Debug"), msdScreen, parentScreen, x, y, width, height);
        for (MusicSharingDeviceBlockEntity.Screen value : MusicSharingDeviceBlockEntity.Screen.values()) {
            goToScreen.add(value.getSerializedName());
        }
    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(new TestFixedButtonsList(x + 1, y + 10, 29, 100, 5, new TextComponent("Go To Screen"), this.goToScreen, TextComponent::new, (n) -> {
            insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.getScreenByName(n.item()));
        }));
    }
}
