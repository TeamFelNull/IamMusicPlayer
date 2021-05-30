package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.components.monitor.*;
import red.felnull.imp.inventory.MusicSharingDeviceMenu;
import red.felnull.otyacraftengine.api.OtyacraftEngineAPI;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MusicSharingDeviceScreen extends IMPEquipmentBaseScreen<MusicSharingDeviceMenu> {
    private static final ResourceLocation MSD_LOCATION = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device.png");
    private static final Map<MusicSharingDeviceBlockEntity.Screen, Supplier<MSDBaseMonitor>> SCREEN_CRATER = new HashMap<>();
    public List<MusicSharingDeviceBlockEntity.Screen> screenHistory = new ArrayList();
    private MSDBaseMonitor currentScreens;

    public MusicSharingDeviceScreen(MusicSharingDeviceMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 215;
        this.imageHeight = 242;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        insFristOpen();
        this.currentScreens = null;
        SCREEN_CRATER.clear();
        addScreens();

        if (OtyacraftEngineAPI.getInstance().isDebugMode())
            this.addRenderableWidget(new Button(leftPos + imageWidth - 34, topPos + imageHeight - 40 + 20, 20, 20, new TextComponent("Debug"), n -> insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.DEBUG)));


    }

    protected void addScreens() {
        addScreen(MusicSharingDeviceBlockEntity.Screen.OFF, () -> new MSDOffMonitor(MusicSharingDeviceBlockEntity.Screen.OFF, this, getMonitorLeftPos(), getMonitorTopPos(), getMonitorWidth(), getMonitorHeight()));
        addScreen(MusicSharingDeviceBlockEntity.Screen.DEBUG, () -> new MSDDebugMonitor(MusicSharingDeviceBlockEntity.Screen.DEBUG, this, getMonitorLeftPos(), getMonitorTopPos(), getMonitorWidth(), getMonitorHeight()));
        addScreen(MusicSharingDeviceBlockEntity.Screen.PLAYLIST, () -> new MSDPlayListMonitor(MusicSharingDeviceBlockEntity.Screen.PLAYLIST, this, getMonitorLeftPos(), getMonitorTopPos(), getMonitorWidth(), getMonitorHeight()));
        addScreen(MusicSharingDeviceBlockEntity.Screen.NO_ANTENNA, () -> new NoAntennaMonitor(MusicSharingDeviceBlockEntity.Screen.NO_ANTENNA, this, getMonitorLeftPos(), getMonitorTopPos(), getMonitorWidth(), getMonitorHeight()));
    }

    @Override
    public void tick() {
        super.tick();

        if (getCurrentScreen() == null || getCurrentScreen().getMSDScreen() != getCurrentMonitorScreen()) {
            if (getCurrentScreen() != null) {
                getCurrentScreen().disable();
                removeWidget(getCurrentScreen());
            }

            currentScreens = SCREEN_CRATER.get(getCurrentMonitorScreen()).get();
            getCurrentScreen().init();
            addWidget(getCurrentScreen());
        }

        if (getCurrentScreen().isActive()) {
            getCurrentScreen().tick();
        }

        if (getCurrentMonitorScreen() == MusicSharingDeviceBlockEntity.Screen.OFF || getCurrentMonitorScreen() == MusicSharingDeviceBlockEntity.Screen.PLAYLIST) {
            screenHistory.clear();
        }
    }

    @Override
    protected ResourceLocation getBackGrandTextuer() {
        return MSD_LOCATION;
    }

    protected void addScreen(MusicSharingDeviceBlockEntity.Screen msdscreen, Supplier<MSDBaseMonitor> screen) {
        SCREEN_CRATER.put(msdscreen, screen);
    }

    protected MusicSharingDeviceBlockEntity.Screen getCurrentMonitorScreen() {
        return getMSDEntity().getCurrentScreen(null);
    }

    protected MSDBaseMonitor getCurrentScreen() {
        return currentScreens;
    }


    protected MusicSharingDeviceBlockEntity getMSDEntity() {
        return (MusicSharingDeviceBlockEntity) getBlockEntity();
    }


    public void insMonitorScreen(MusicSharingDeviceBlockEntity.Screen screen) {
        if (screen == MusicSharingDeviceBlockEntity.Screen.PLAYLIST) {
            screenHistory.clear();
        } else {
            if (screen != getCurrentMonitorScreen() && (screenHistory.isEmpty() || screenHistory.get(screenHistory.size() - 1) != getCurrentMonitorScreen())) {
                screenHistory.add(getCurrentMonitorScreen());
            }
        }
        insMonitorScreenNoHistory(screen);
    }

    public void insMonitorScreenNoHistory(MusicSharingDeviceBlockEntity.Screen screen) {
        CompoundTag tag = new CompoundTag();
        tag.putString("Name", screen.getSerializedName());
        this.instruction("Screen", tag);
    }

    public void insFristOpen() {
        this.instruction("Open", new CompoundTag());
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {
        super.renderBg(poseStack, f, i, j);
        if (getCurrentScreen() != null && getCurrentScreen().isActive()) {
            getCurrentScreen().render(poseStack, i, j, f);
        } else {
            if (isPowerOn())
                IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_BACKGROUND, poseStack, getMonitorLeftPos(), getMonitorTopPos(), 0, 0, getMonitorWidth(), getMonitorHeight(), getMonitorWidth(), getMonitorHeight());
        }
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        boolean f1 = super.mouseScrolled(d, e, f);
        boolean f2 = getCurrentScreen() == null || !getCurrentScreen().isActive() || getCurrentScreen().mouseScrolled(d, e, f);
        return f1 && f2;
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        boolean f1 = super.mouseClicked(d, e, i);
        boolean f2 = getCurrentScreen() == null || !getCurrentScreen().isActive() || getCurrentScreen().mouseClicked(d, e, i);
        return f1 && f2;
    }

    public int getMonitorLeftPos() {
        return leftPos + 8;
    }

    public int getMonitorTopPos() {
        return topPos + 20;
    }

    public int getMonitorWidth() {
        return 199;
    }

    public int getMonitorHeight() {
        return 122;
    }

}
