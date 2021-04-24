package red.felnull.imp.client.gui.screen.msdscreen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.client.gui.screen.MonitorScreen;
import red.felnull.otyacraftengine.client.gui.screen.IkisugiContainerScreen;

public class MSDOffScreen extends MonitorScreen {

    public MSDOffScreen(IkisugiContainerScreen<?> screen) {
        super(new TranslatableComponent("imp.msdscreen.off.title"), screen);
    }

    @Override
    protected void init() {

    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {

    }
}
