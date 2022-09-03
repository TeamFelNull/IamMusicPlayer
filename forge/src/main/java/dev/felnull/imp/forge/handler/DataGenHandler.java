package dev.felnull.imp.forge.handler;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.data.IamMusicPlayerDataGen;
import dev.felnull.otyacraftengine.forge.data.CrossDataGeneratorAccesses;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IamMusicPlayer.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenHandler {
    @SubscribeEvent
    public static void onDataGen(GatherDataEvent event) {
        IamMusicPlayerDataGen.init(CrossDataGeneratorAccesses.create(event));
    }
}
