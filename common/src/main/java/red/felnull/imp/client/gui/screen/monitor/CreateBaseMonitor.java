package red.felnull.imp.client.gui.screen.monitor;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.components.MSDSmartButton;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.data.resource.ImageInfo;

public abstract class CreateBaseMonitor extends ImageNameMonitor {
    private MSDSmartButton createButton;

    public CreateBaseMonitor(Component component, MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(component, msdScreen, parentScreen, x, y, width, height);
    }

    @Override
    public void init() {
        super.init();
        this.createButton = addCreateSmartButton(new TranslatableComponent("imp.msdButton.create"), x + 148, y + 104, n -> created());
    }

    @Override
    public void tick() {
        super.tick();
        createButton.active = canCreate();
    }

    protected boolean canCreate() {
        return imageInfo != ImageInfo.EMPTY && !nameTextBox.getValue().isEmpty();
    }

    abstract protected void created();
}
