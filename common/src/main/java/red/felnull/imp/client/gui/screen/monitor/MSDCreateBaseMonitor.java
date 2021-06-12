package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.components.MSDSmartEditBox;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.renderer.PlayImageRenderer;
import red.felnull.imp.data.resource.ImageInfo;

public abstract class MSDCreateBaseMonitor extends MSDBaseMonitor {
    private MSDSmartEditBox imageURL;
    private ImageInfo imageInfo;

    public MSDCreateBaseMonitor(Component component, MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(component, msdScreen, parentScreen, x, y, width, height);
        this.imageInfo = ImageInfo.EMPTY;
    }


    @Override
    public void init() {
        super.init();
        this.imageURL = addCreateSmartTextEditBox(new TextComponent("TEST"), x + 45, y + 23, 53);
        addCreateSmartTextEditBox(new TextComponent("Name"), x + 101, y + 23, 95, n -> {
            imageInfo = new ImageInfo(ImageInfo.ImageType.STRING, n);
        });
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        fillXDarkGrayLine(poseStack, x + 3, y + 23, 39);
        fillXDarkGrayLine(poseStack, x + 3, y + 61, 39);
        fillYDarkGrayLine(poseStack, x + 3, y + 24, 37);
        fillYDarkGrayLine(poseStack, x + 41, y + 24, 37);
        fillLightGray(poseStack, x + 4, y + 24, 37, 37);

        PlayImageRenderer.getInstance().render(imageInfo, poseStack, x + 4, y + 24, 37);
    }
}
