package dev.felnull.imp.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.IamMusicPlayerClient;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(IamMusicPlayer.MODID)
public class IamMusicPlayerForge {
    public IamMusicPlayerForge() {
        EventBuses.registerModEventBus(IamMusicPlayer.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        IamMusicPlayer.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        System.out.println(IamMusicPlayer.class.getResourceAsStream("/natives/win-x86-64/connector.dll"));
        System.out.println("test");
    }

    private void doClientStuff(FMLClientSetupEvent event) {
        IamMusicPlayerClient.init();
    }
}
