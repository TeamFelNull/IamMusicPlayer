package red.felnull.imp.client.gui.components.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class MSDBaseMonitor extends Monitor<MusicSharingDeviceScreen> {
    private static final ResourceLocation MSD_BACKGROUND = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device_screen/background.png");
    protected boolean renderBackGround = true;

    public MSDBaseMonitor(Component component, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(component, parentScreen, x, y, width, height);
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        if (renderBackGround) {
            IKSGRenderUtil.drawBindTextuer(MSD_BACKGROUND, poseStack, x, y, 0, 0, width, height, width, height);
        }
    }
}
