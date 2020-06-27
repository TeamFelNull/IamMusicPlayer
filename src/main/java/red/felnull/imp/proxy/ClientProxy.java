package red.felnull.imp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import red.felnull.imp.client.handler.ClientHandler;
import red.felnull.imp.client.handler.RenderHandler;
import red.felnull.imp.client.renderer.tileentity.IMPTileEntityRenderers;
import red.felnull.imp.client.screen.RegisterScrennContainerFactorys;
import red.felnull.imp.file.Options;
import red.felnull.imp.file.PlayList;
import red.felnull.imp.sound.ClientSoundPlayer;
import red.felnull.imp.sound.SoundThread;


public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
        MinecraftForge.EVENT_BUS.register(RenderHandler.class);
        MinecraftForge.EVENT_BUS.register(ClientHandler.class);
        PlayList.createClientPlayList();
    }

    public static void clientInit() {
		IMPTileEntityRenderers.registerTileEntityRenderer();
		RegisterScrennContainerFactorys.registerFactories();
        ClientSoundPlayer.INSTANS = new ClientSoundPlayer();

    }

    @Override
    public void init() {
        super.init();
        Options.loadOption();
    }

    @Override
    public void posInit() {
        super.posInit();

		SoundThread st = new SoundThread();
		st.start();

    }

    public Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }
}
