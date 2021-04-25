package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.screen.msdscreen.MSDOffScreen;
import red.felnull.imp.client.gui.screen.msdscreen.MSDPlayListScreen;
import red.felnull.imp.inventory.MusicSharingDeviceMenu;

import java.util.HashMap;
import java.util.Map;

public class MusicSharingDeviceScreen extends IMPEquipmentBaseScreen<MusicSharingDeviceMenu> {
    private static final ResourceLocation MSD_LOCATION = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device.png");
    private static final Map<MusicSharingDeviceBlockEntity.Screen, MonitorScreen> SCREENS = new HashMap<>();
    private MusicSharingDeviceBlockEntity.Screen lastScreen;

    public MusicSharingDeviceScreen(MusicSharingDeviceMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 215;
        this.imageHeight = 242;
        this.inventoryLabelY = this.imageHeight - 94;
        addScreens();
    }

    @Override
    protected void init() {
        super.init();
        insFristOpen();
        this.lastScreen = getCurrentMonitorScreen();
        getCurrentScreen().enabled();
    }

    protected void addScreens() {
        addScreen(MusicSharingDeviceBlockEntity.Screen.OFF, new MSDOffScreen(this));
        addScreen(MusicSharingDeviceBlockEntity.Screen.PLAYLIST, new MSDPlayListScreen(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (lastScreen != getCurrentMonitorScreen()) {
            SCREENS.get(lastScreen).disable();
            this.lastScreen = getCurrentMonitorScreen();
            getCurrentScreen().enabled();
        }
    }

    @Override
    protected ResourceLocation getBackGrandTextuer() {
        return MSD_LOCATION;
    }

    protected void addScreen(MusicSharingDeviceBlockEntity.Screen msdscreen, MonitorScreen screen) {
        SCREENS.put(msdscreen, screen);
    }

    protected MusicSharingDeviceBlockEntity.Screen getCurrentMonitorScreen() {
        return getMSDEntity().getCurrentScreen(null);
    }

    protected MonitorScreen getCurrentScreen() {
        return SCREENS.get(getCurrentMonitorScreen());
    }

    protected MusicSharingDeviceBlockEntity getMSDEntity() {
        return (MusicSharingDeviceBlockEntity) getBlockEntity();
    }

    public void insMonitorScreen(MusicSharingDeviceBlockEntity.Screen screen) {
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
        if (getCurrentScreen().isActive()) {
            getCurrentScreen().render(poseStack, i, j, f);
        }
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
