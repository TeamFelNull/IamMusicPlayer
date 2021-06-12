package red.felnull.imp.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.InteractionResult;
import red.felnull.imp.IMPConfig;
import red.felnull.imp.api.client.IMPClientRegistry;
import red.felnull.imp.client.music.MusicEngine;
import red.felnull.imp.client.renderer.blockentity.IMPBERenderers;
import red.felnull.imp.client.renderer.blockentity.gui.IMPScreenFactoryRegister;
import red.felnull.imp.client.renderer.item.IMPItemRenderers;

import java.util.Properties;

public class IamMusicPlayerClient {

    public static void clientInit() {

        IMPClientRegistry.init();
        IMPBERenderers.init();
        IMPItemRenderers.init();
        IMPScreenFactoryRegister.init();

        AutoConfig.getConfigHolder(IMPConfig.class).registerSaveListener((manager, data) -> {
            MusicEngine.getInstance().reload();
            return InteractionResult.SUCCESS;
        });
    }
}
