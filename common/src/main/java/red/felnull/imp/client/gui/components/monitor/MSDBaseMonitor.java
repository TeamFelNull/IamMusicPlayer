package red.felnull.imp.client.gui.components.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.IMPFonts;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class MSDBaseMonitor extends Monitor<MusicSharingDeviceScreen> {
    private static final Minecraft mc = Minecraft.getInstance();
    public static final ResourceLocation MSD_BACKGROUND = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device_screen/background.png");
    public static final ResourceLocation MSD_WIDGETS = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device_screen/widgets.png");
    private MusicSharingDeviceBlockEntity.Screen msdScreen;
    protected boolean renderBackGround = true;
    protected boolean renderHeader = true;
    protected Button closeButton;
    protected Button backButton;
    protected boolean enableCloseButton = true;
    protected boolean enableBackButton = true;


    public MSDBaseMonitor(Component component, MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(component, parentScreen, x, y, width, height);
        this.msdScreen = msdScreen;
    }

    @Override
    public void init() {
        super.init();

        this.closeButton = this.addRenderableWidget(new ImageButton(x + width - 15, y + 1, 14, 10, 0, 0, 10, MSD_WIDGETS, 256, 256, n -> getParentScreen().insMonitorScreenNoHistory(MusicSharingDeviceBlockEntity.Screen.PLAYLIST)));

        this.backButton = this.addRenderableWidget(new ImageButton(x + width - 29, y + 1, 14, 10, 14, 0, 10, MSD_WIDGETS, 256, 256, n -> {
            if (getLastScreen() != null) {
                MusicSharingDeviceBlockEntity.Screen last = getLastScreen();
                getParentScreen().screenHistory.remove(getParentScreen().screenHistory.size() - 1);
                getParentScreen().insMonitorScreenNoHistory(last);
            }
        }));

    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        if (renderBackGround) {
            IKSGRenderUtil.drawBindTextuer(MSD_BACKGROUND, poseStack, x, y, 0, 0, width, height, width, height);
        }

        if (renderHeader) {
            fillXGrayLine(poseStack, x + 1, y + 11, 197);
            fillLightGray(poseStack, x + 1, y + 1, 197, 10);
        }

        super.render(poseStack, i, j, f);
    }

    public void drawPrettyCenteredString(PoseStack poseStack, Component component, int i, int j, int k) {
        mc.font.draw(poseStack, component.copy().withStyle(IMPFonts.FLOPDE_SIGN_FONT), (float) (i - getFont().width(component) / 2), j, k);
    }

    public void drawPrettyString(PoseStack poseStack, Component component, int i, int j, int k) {
        mc.font.draw(poseStack, component.copy().withStyle(IMPFonts.FLOPDE_SIGN_FONT), i, j, k);
    }

    @Override
    public void tick() {
        super.tick();
        this.closeButton.visible = this.closeButton.active = renderHeader && enableCloseButton;
        this.backButton.visible = this.backButton.active = renderHeader && enableBackButton && getLastScreen() != null && getLastScreen() != MusicSharingDeviceBlockEntity.Screen.PLAYLIST;
    }

    private MusicSharingDeviceBlockEntity.Screen getLastScreen() {
        if (!getParentScreen().screenHistory.isEmpty()) {
            return getParentScreen().screenHistory.get(getParentScreen().screenHistory.size() - 1);
        }
        return null;
    }

    public MusicSharingDeviceBlockEntity.Screen getMSDScreen() {
        return msdScreen;
    }

    public void insMonitorScreen(MusicSharingDeviceBlockEntity.Screen screen) {
        getParentScreen().insMonitorScreen(screen);
    }

}
