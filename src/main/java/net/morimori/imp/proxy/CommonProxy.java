package net.morimori.imp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.morimori.imp.handler.ServerHandler;
import net.morimori.imp.packet.PacketHandler;

public class CommonProxy {

	public void preInit() {
		PacketHandler.init();
			MinecraftForge.EVENT_BUS.register(ServerHandler.class);
//		MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerLoggedInEvent.class,
//				new ServerHandler()::onPlayerLogIn);
//		MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerLoggedOutEvent.class,
//				new ServerHandler()::onPlayerLogOut);
//		MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, FMLServerStartingEvent.class,
//				new ServerHandler()::onServerStart);
//		MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, WorldEvent.Save.class,
//				new ServerHandler()::onWorldSave);
	}

	public void init() {

	}

	public void posInit() {

	}
	public Minecraft getMinecraft() {
		return null;
	}
}
