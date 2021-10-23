package dev.felnull.imp.fabric;

import dev.felnull.imp.IamMusicPlayer;
import net.fabricmc.api.ModInitializer;

public class IamMusicPlayerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        IamMusicPlayer.init();
    }
}
