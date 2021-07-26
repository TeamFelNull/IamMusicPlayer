package red.felnull.imp.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.InteractionResult;
import red.felnull.imp.IMPConfig;
import red.felnull.imp.api.client.IMPClientRegistry;
import red.felnull.imp.client.gui.tooltip.IMPTooltips;
import red.felnull.imp.client.handler.RenderHandler;
import red.felnull.imp.client.music.MusicEngine;
import red.felnull.imp.client.renderer.blockentity.IMPBERenderers;
import red.felnull.imp.client.renderer.blockentity.gui.IMPScreenFactoryRegister;
import red.felnull.imp.client.renderer.item.IMPItemRenderers;

public class IamMusicPlayerClient {

    public static void clientInit() {
        IMPClientRegistry.init();
        IMPBERenderers.init();
        IMPItemRenderers.init();
        IMPScreenFactoryRegister.init();
        IMPTooltips.init();
        RenderHandler.init();

        AutoConfig.getConfigHolder(IMPConfig.class).registerSaveListener((manager, data) -> {
            MusicEngine.getInstance().reload();
            return InteractionResult.SUCCESS;
        });
    }
}
