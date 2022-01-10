package dev.felnull.imp.client;

import dev.felnull.imp.IMPConfig;
import dev.felnull.imp.client.gui.screen.IMPScreenFactorys;
import dev.felnull.imp.client.gui.screen.monitor.MusicManagerMonitor;
import dev.felnull.imp.client.handler.ClientHandler;
import dev.felnull.imp.client.handler.MusicHandler;
import dev.felnull.imp.client.handler.RenderHandler;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.music.loader.IMPMusicLoaders;
import dev.felnull.imp.client.music.loadertypes.IMPMusicLoaderTypes;
import dev.felnull.imp.client.music.subtitle.IMPMusicSubtitles;
import dev.felnull.imp.client.music.tracker.IMPMusicTrackers;
import dev.felnull.imp.client.renderer.blockentity.IMPBlockEntityRenderers;
import dev.felnull.imp.client.renderer.item.IMPItemRenderers;
import dev.felnull.imp.networking.IMPPackets;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.InteractionResult;

public class IamMusicPlayerClient {
    public static void init() {
        IMPPackets.clientInit();
        IMPMusicTrackers.init();
        IMPMusicLoaderTypes.init();
        IMPMusicLoaders.init();
        IMPMusicSubtitles.init();
        ClientHandler.init();
        RenderHandler.init();
        MusicHandler.init();
        IMPModels.init();
        IMPBlockEntityRenderers.init();
        IMPItemRenderers.init();
        IMPScreenFactorys.init();
        MusicManagerMonitor.firstInit();

        AutoConfig.getConfigHolder(IMPConfig.class).registerSaveListener((manager, data) -> {
            MusicEngine.getInstance().reload();
            return InteractionResult.SUCCESS;
        });
    }
}
