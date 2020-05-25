package net.morimori.imp.handler;

import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.morimori.imp.file.DwonloadMusic;
import net.morimori.imp.file.PlayList;
import net.morimori.imp.file.ServerFileReceiver;
import net.morimori.imp.file.ServerFileSender;
import net.morimori.imp.util.FileHelper;
import net.morimori.imp.util.FileLoader;
import net.morimori.imp.util.PlayerHelper;

public class ServerHandler {
	@SubscribeEvent
	public static void onPlayerLogIn(PlayerLoggedInEvent e) {
		FileLoader.createFolder(FileHelper.getWorldPlayerPlayListDataPath(e.getPlayer()));
	}

	@SubscribeEvent
	public static void onPlayerLogOut(PlayerLoggedOutEvent e) {
		ServerFileSender.stopSend(PlayerHelper.getUUID(e.getPlayer()));
		ServerFileReceiver.stopReceiver(PlayerHelper.getUUID(e.getPlayer()));
	}

	@SubscribeEvent
	public static void onServerStart(FMLServerStartingEvent e) {
		FileLoader.createFolder(FileHelper.getWorldEveryonePlayListDataPath(e.getServer()));
		PlayList.checkWorldPlayLists(e.getServer(), false);
		DwonloadMusic.dwonloadSoundFromWorldPlayLists(e.getServer());
	}

	@SubscribeEvent
	public static void onWorldSave(WorldEvent.Save e) {
		PlayList.checkWorldPlayLists(e.getWorld().getWorld().getServer(), true);
	}
}
