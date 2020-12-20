package red.felnull.imp.client.gui;

import net.minecraft.client.gui.ScreenManager;
import red.felnull.imp.client.gui.screen.CassetteDeckScreen;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.container.IMPContainerTypes;

public class IMPScrennContainerRegister {
    public static void registerFactories() {
        ScreenManager.registerFactory(IMPContainerTypes.MUSIC_SHARING_DEVICE, MusicSharingDeviceScreen::new);
        ScreenManager.registerFactory(IMPContainerTypes.CASSETTE_DECK, CassetteDeckScreen::new);
    }
}
