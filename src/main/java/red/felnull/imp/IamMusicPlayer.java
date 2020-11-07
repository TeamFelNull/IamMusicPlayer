package red.felnull.imp;

import com.github.kiulian.downloader.YoutubeDownloader;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.proxy.ClientProxy;
import red.felnull.imp.proxy.CommonProxy;

@Mod(IamMusicPlayer.MODID)
public class IamMusicPlayer {
    public static final String MODID = "iammusicplayer";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public IamMusicPlayer() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }

    private void setup(final FMLCommonSetupEvent event) {
        proxy.preInit();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientProxy.clientInit();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        proxy.init();
    }

    private void processIMC(final InterModProcessEvent event) {
        proxy.posInit();
    }
}
