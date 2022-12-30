package dev.felnull.imp.fabric.client;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.IamMusicPlayerClient;
import dev.felnull.specialmodelloader.api.event.SpecialModelLoaderEvents;
import net.fabricmc.api.ClientModInitializer;

public class IamMusicPlayerClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        IamMusicPlayerClient.init();
        SpecialModelLoaderEvents.LOAD_SCOPE.register(loc -> IamMusicPlayer.MODID.equals(loc.getNamespace()));
    }
}
