package net.morimori.imp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.morimori.imp.client.handler.ClientHandler;
import net.morimori.imp.client.handler.RenderHandler;
import net.morimori.imp.client.renderer.tileentity.IMPTileEntityRenderers;
import net.morimori.imp.client.screen.RegisterScrennContainerFactorys;
import net.morimori.imp.file.Options;
import net.morimori.imp.file.PlayList;
import net.morimori.imp.sound.SoundWaitThread;

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

	}

	@Override
	public void init() {
		super.init();
		Options.loadOption();
	}

	@Override
	public void posInit() {
		super.posInit();
		SoundWaitThread.startSoundWaiter();
	}

	public Minecraft getMinecraft() {
		return Minecraft.getInstance();
	}
}
