package net.morimori.imp.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.handler.ServerHandler;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.registries.IMPRegistries;

public class CommonProxy {

	public void preInit() {
		PacketHandler.init();
		MinecraftForge.EVENT_BUS.register(ServerHandler.class);
		IMPRegistries.registerDwonloadMusic(new ResourceLocation(IkisugiMusicPlayer.MODID, "test"),
				"https://www.dropbox.com/s/1ygziobmycqzjl2/%E9%87%8E%E7%8D%A3%E3%82%A4%E3%83%B3%E3%82%BF%E3%83%93%E3%83%A5%E3%83%BC%E3%80%81%E6%9C%AC%E7%B7%A8.mp3?dl=1");
	}

	public void init() {

	}

	public void posInit() {

	}

	public Minecraft getMinecraft() {
		return null;
	}
}
