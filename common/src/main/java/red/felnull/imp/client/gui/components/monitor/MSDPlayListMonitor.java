package red.felnull.imp.client.gui.components.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.client.gui.components.PlayListSelectionList;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;

public class MSDPlayListMonitor extends MSDBaseMonitor {
    private PlayListSelectionList playListSelectionList;

    public MSDPlayListMonitor(MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdscreen.playlist.title"), parentScreen, x, y, width, height);
        this.renderHeader = false;
    }

    @Override
    public void init() {
        super.init();
        this.playListSelectionList = addWidget(new PlayListSelectionList(getMinecraft(), x + 1, y + 20, 28, 101, 18));
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        this.playListSelectionList.render(poseStack, i, j, f);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {

        return super.mouseClicked(d, e, i);
    }
}
