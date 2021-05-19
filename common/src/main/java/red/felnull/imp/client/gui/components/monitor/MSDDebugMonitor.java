package red.felnull.imp.client.gui.components.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextComponent;
import red.felnull.imp.client.gui.components.TestSelectionList;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;

public class MSDDebugMonitor extends MSDBaseMonitor {
    private TestSelectionList testSelectionList;

    public MSDDebugMonitor(MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TextComponent("Debug"), parentScreen, x, y, width, height);
    }

    @Override
    public void init() {
        super.init();
        this.testSelectionList = addWidget(new TestSelectionList(getMinecraft(), x + 20, y + 20, 40, 80, 18));

    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        this.testSelectionList.render(poseStack, i, j, f);
    }
}
