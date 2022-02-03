package dev.felnull.imp.fabric.client;

import dev.felnull.imp.client.IamMusicPlayerClient;
import net.fabricmc.api.ClientModInitializer;

public class IamMusicPlayerClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        IamMusicPlayerClient.init();
    }
}
