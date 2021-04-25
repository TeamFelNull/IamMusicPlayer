package red.felnull.imp.client.gui.screen.msdscreen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.gui.screen.MonitorScreen;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.otyacraftengine.client.gui.screen.IkisugiContainerScreen;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public abstract class MSDBaseScreen extends MonitorScreen {
    private static final ResourceLocation MSD_BACKGROUND = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device_screen/background.png");
    protected boolean renderBackGround = true;

    protected MSDBaseScreen(Component component, IkisugiContainerScreen<?> screen) {
        super(component, screen);
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        if (renderBackGround) {
            IKSGRenderUtil.drawBindTextuer(MSD_BACKGROUND, poseStack, getMSDScreen().getMonitorLeftPos(), getMSDScreen().getMonitorTopPos(), 0, 0, getMSDScreen().getMonitorWidth(), getMSDScreen().getMonitorHeight(), getMSDScreen().getMonitorWidth(), getMSDScreen().getMonitorHeight());
        }
    }

    public MusicSharingDeviceScreen getMSDScreen() {
        return (MusicSharingDeviceScreen) screen;
    }
}
