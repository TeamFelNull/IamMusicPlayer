package net.morimori.imp.client.screen;

import net.minecraft.client.gui.ScreenManager;
import net.morimori.imp.container.IMPContainerTypes;

public class RegisterScrennContainerFactorys {
	public static void registerFactories() {
		ScreenManager.registerFactory(IMPContainerTypes.SOUNDFILE_UPLOADER, SoundFileUploaderScreen::new);
		ScreenManager.registerFactory(IMPContainerTypes.CASSETTE_DECK, CassetteDeckScreen::new);
	}
}
