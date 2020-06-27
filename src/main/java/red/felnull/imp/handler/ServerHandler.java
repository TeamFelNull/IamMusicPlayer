package red.felnull.imp.handler;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import red.felnull.imp.command.IMPCommands;
import red.felnull.imp.file.DwonloadMusic;
import red.felnull.imp.file.PlayList;
import red.felnull.imp.file.ServerFileSender;
import red.felnull.imp.file.ServerSoundFileReceiver;
import red.felnull.imp.sound.WorldSoundKey;
import red.felnull.imp.util.FileHelper;
import red.felnull.imp.util.FileLoader;
import red.felnull.imp.util.PlayerHelper;

public class ServerHandler {
    @SubscribeEvent
    public static void onPlayerLogIn(PlayerLoggedInEvent e) {
        FileLoader.createFolder(FileHelper.getWorldPlayerPlayListDataPath(e.getPlayer()));
    }

    @SubscribeEvent
    public static void onPlayerLogOut(PlayerLoggedOutEvent e) {
        ServerFileSender.stopSend(PlayerHelper.getUUID(e.getPlayer()));
        ServerSoundFileReceiver.receiveStop(PlayerHelper.getUUID(e.getPlayer()));
    }

    @SubscribeEvent
    public static void onServerStart(FMLServerStartingEvent e) {
        FileLoader.createFolder(FileHelper.getWorldEveryonePlayListDataPath(e.getServer()));
        FileLoader.createFolder(FileHelper.getWorldPictuerPath(e.getServer()));
        PlayList.checkWorldPlayLists(e.getServer(), false);
        DwonloadMusic.dwonloadSoundFromWorldPlayLists(e.getServer());
        IMPCommands.registerCommand(e.getCommandDispatcher());
    }

    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Save e) {
        PlayList.checkWorldPlayLists(e.getWorld().getWorld().getServer(), true);
    }

    @SubscribeEvent
    public static void onWorldSave(TickEvent.ServerTickEvent e) {

        Set<WorldSoundKey> deletes = new HashSet<WorldSoundKey>();

        for (Entry<WorldSoundKey, byte[]> mb : ServerSoundStreamMessageHandler.dwonloadbuf.entrySet()) {

            if (ServerSoundStreamMessageHandler.lasttimes.containsKey(mb.getKey())) {
                long keka = System.currentTimeMillis() - ServerSoundStreamMessageHandler.lasttimes.get(mb.getKey());
                if (keka >= 1000 * 60 * 3) {
                    deletes.add(mb.getKey());
                }
            }
        }

        for (WorldSoundKey delete : deletes) {
            ServerSoundStreamMessageHandler.dwonloadbuf.remove(delete);
            ServerSoundStreamMessageHandler.lasttimes.remove(delete);
        }

    }
}
