package red.felnull.imp.client;

import net.fabricmc.api.ClientModInitializer;

public class IamMusicPlayerClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        IamMusicPlayerClient.clientInit();
    }
}
