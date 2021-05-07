package red.felnull.imp.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.sounds.SoundSource;
import red.felnull.imp.api.client.IMPClientRegistry;
import red.felnull.imp.client.config.IMPClientConfig;
import red.felnull.imp.client.renderer.blockentity.IMPBERenderers;
import red.felnull.imp.client.renderer.blockentity.gui.IMPScreenFactoryRegister;
import red.felnull.imp.client.renderer.item.IMPItemRenderers;
import red.felnull.otyacraftengine.config.OEConfig;

public class IamMusicPlayerClient {
    public static final IMPClientConfig CLIENT_CONFIG = AutoConfig.register(IMPClientConfig.class, Toml4jConfigSerializer::new).getConfig();

    public static void clientInit() {
        IMPClientRegistry.init();
        IMPBERenderers.init();
        IMPItemRenderers.init();
        IMPScreenFactoryRegister.init();
    }
}
