package red.felnull.imp.client.gui.screen.monitor;

import net.minecraft.network.chat.Component;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.data.resource.ImageInfo;

import java.util.UUID;

public abstract class DetailsEditBaseMonitor extends ImageNameMonitor {
    public DetailsEditBaseMonitor(Component component, MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(component, msdScreen, parentScreen, x, y, width, height);
        this.canChangeImage = canChangeable();
    }

    @Override
    public void init() {
        super.init();
        if (!isMaintain())
            return;
        this.imageInfo = getDefaultImage();
        this.nameTextBox.setValue(getDefaultName());
    }

    @Override
    public void tick() {
        super.tick();
        if (!isMaintain()) {
            insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.PLAYLIST);
            return;
        }


    }

    abstract protected UUID getCurrentDetailsObjectUUID();

    abstract protected boolean isMaintain();

    abstract protected ImageInfo getDefaultImage();

    abstract protected String getDefaultName();

    abstract protected boolean canChangeable();
}
