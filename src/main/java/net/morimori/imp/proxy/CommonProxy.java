package net.morimori.imp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.morimori.imp.handler.ServerHandler;
import net.morimori.imp.packet.PacketHandler;

public class CommonProxy {

	public void preInit() {
		PacketHandler.init();
		MinecraftForge.EVENT_BUS.register(ServerHandler.class);

	}

	public void init() {

	}

	public void posInit() {

	}

	public Minecraft getMinecraft() {
		return null;
	}
}
