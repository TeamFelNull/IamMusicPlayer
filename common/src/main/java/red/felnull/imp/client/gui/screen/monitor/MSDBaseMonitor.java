package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.IMPFonts;
import red.felnull.imp.client.gui.components.IMSDSmartRender;
import red.felnull.imp.client.gui.components.MSDSmartButton;
import red.felnull.imp.client.gui.components.MSDSmartEditBox;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.function.Consumer;

public class MSDBaseMonitor extends Monitor<MusicSharingDeviceScreen> implements IMSDSmartRender {
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
            drawPrettyString(poseStack, getTitle().copy(), x + 2, y + 2, 0);
        }

        super.render(poseStack, i, j, f);
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

    public MSDSmartButton addCreateSmartButton(MutableComponent component, int x, int y, Button.OnPress onPress) {
        return addCreateSmartButton(component, x, y, 48, 15, onPress);
    }

    public MSDSmartButton addCreateSmartButton(MutableComponent component, int x, int y, int w, int h, Button.OnPress onPress) {
        component.withStyle(IMPFonts.FLOPDE_SIGN_FONT);
        return addRenderableWidget(new MSDSmartButton(x, y, w, h, component, onPress));
    }

    public MSDSmartEditBox addCreateSmartTextEditBox(MutableComponent component, int x, int y, int w) {
        return addCreateSmartTextEditBox(component, x, y, w, n -> {
        });
    }

    public MSDSmartEditBox addCreateSmartTextEditBox(MutableComponent component, int x, int y, int w, Consumer<String> changeNamed) {
        component.withStyle(IMPFonts.FLOPDE_SIGN_FONT);
        MSDSmartEditBox msdSmartEditBox = this.addWidget(new MSDSmartEditBox(x, y, w, component));
        msdSmartEditBox.setTextColor(-1);
        msdSmartEditBox.setTextColorUneditable(-1);
        msdSmartEditBox.setMaxLength(100);
        msdSmartEditBox.setResponder(changeNamed);
        msdSmartEditBox.setValue("");
        return msdSmartEditBox;
    }

}
