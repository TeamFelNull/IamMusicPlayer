package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.components.monitor.MSDOffMonitor;
import red.felnull.imp.client.gui.components.monitor.MSDPlayListMonitor;
import red.felnull.imp.client.gui.components.monitor.Monitor;
import red.felnull.imp.inventory.MusicSharingDeviceMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MusicSharingDeviceScreen extends IMPEquipmentBaseScreen<MusicSharingDeviceMenu> {
    private static final ResourceLocation MSD_LOCATION = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device.png");
    private static final Map<MusicSharingDeviceBlockEntity.Screen, Monitor<? extends MusicSharingDeviceScreen>> SCREENS = new HashMap<>();
    private MusicSharingDeviceBlockEntity.Screen lastScreen;

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
        this.lastScreen = null;
        SCREENS.clear();
        addScreens();
    }

    protected void addScreens() {
        addScreen(MusicSharingDeviceBlockEntity.Screen.OFF, new MSDOffMonitor(this, getMonitorLeftPos(), getMonitorTopPos(), getMonitorWidth(), getMonitorHeight()));
        addScreen(MusicSharingDeviceBlockEntity.Screen.PLAYLIST, new MSDPlayListMonitor(this, getMonitorLeftPos(), getMonitorTopPos(), getMonitorWidth(), getMonitorHeight()));
    }

    @Override
    public void tick() {
        super.tick();
        getCurrentScreen().ifPresent(n -> {
            if (lastScreen != getCurrentMonitorScreen()) {
                if (lastScreen != null)
                    SCREENS.get(lastScreen).disable();
                this.lastScreen = getCurrentMonitorScreen();
                n.enabled();
            }
        });
    }

    @Override
    protected ResourceLocation getBackGrandTextuer() {
        return MSD_LOCATION;
    }

    protected void addScreen(MusicSharingDeviceBlockEntity.Screen msdscreen, Monitor<? extends MusicSharingDeviceScreen> screen) {
        this.addWidget(screen);
        SCREENS.put(msdscreen, screen);
    }

    protected MusicSharingDeviceBlockEntity.Screen getCurrentMonitorScreen() {
        return getMSDEntity().getCurrentScreen(null);
    }

    protected Optional<Monitor<? extends MusicSharingDeviceScreen>> getCurrentScreen() {

        if (SCREENS.containsKey(getCurrentMonitorScreen()))
            return Optional.of(SCREENS.get(getCurrentMonitorScreen()));

        return Optional.empty();
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
        getCurrentScreen().filter(Monitor::isActive).ifPresent(n -> {
            n.render(poseStack, i, j, f);
        });
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        return super.mouseScrolled(d, e, f) && getCurrentScreen().filter(Monitor::isActive).map(n -> n.mouseScrolled(d, e, f)).orElse(true);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        boolean flag1 = super.mouseDragged(d, e, i, f, g);
        boolean flag2 = this.getFocused() != null && this.isDragging() && i == 0 && this.getFocused().mouseDragged(d, e, i, f, g);
/*
        if (getCurrentScreen().filter(Monitor::isActive).isPresent()) {
            boolean flag3 = getCurrentScreen().get().getFocused() != null && getCurrentScreen().get().isDragging() && i == 0 && getCurrentScreen().get().mouseDragged(d, e, i, f, g);
            return flag1 && flag2 && flag3;
        }*/
        return flag1 && flag2;
    }

/*
    @Override
    public boolean mouseClicked(double d, double e, int i) {
        return super.mouseClicked(d, e, i) && getCurrentScreen().filter(Monitor::isActive).map(n -> n.mouseClicked(d, e, i)).orElse(true);
    }*/

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
