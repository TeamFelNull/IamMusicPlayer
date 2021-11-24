package dev.felnull.imp.client;

import dev.felnull.imp.IMPConfig;
import dev.felnull.imp.client.handler.ClientHandler;
import dev.felnull.imp.client.handler.MusicHandler;
import dev.felnull.imp.client.handler.RenderHandler;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.music.loader.IMPMusicLoaders;
import dev.felnull.imp.client.music.subtitle.IMPMusicSubtitles;
import dev.felnull.imp.client.music.tracker.IMPMusicTrackers;
import dev.felnull.imp.client.renderer.blockentity.IMPBlockEntityRenderers;
import dev.felnull.imp.client.renderer.item.IMPItemRenderers;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.InteractionResult;

public class IamMusicPlayerClient {
    public static void init() {
        IMPMusicTrackers.init();
        IMPMusicLoaders.init();
        IMPMusicSubtitles.init();
        ClientHandler.init();
        RenderHandler.init();
        MusicHandler.init();
        IMPBlockEntityRenderers.init();
        IMPItemRenderers.init();

        AutoConfig.getConfigHolder(IMPConfig.class).registerSaveListener((manager, data) -> {
            MusicEngine.getInstance().reload();
            return InteractionResult.SUCCESS;
        });
    }
}
