package net.morimori.imp.client.handler;

import java.io.File;
import java.nio.file.Paths;
import java.util.function.Supplier;

import net.minecraftforge.fml.network.NetworkEvent;
import net.morimori.imp.file.PlayList;
import net.morimori.imp.packet.WorldPlaylistMessage;

public class WorldPlaylistMessageHandler {

    public static void reversiveMessage(WorldPlaylistMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        PlayList.worldPlayList = new File[message.playlist.length];
        for (int c = 0; c < message.playlist.length; c++) {
            PlayList.worldPlayList[c] = Paths.get(message.playlist[c]).toFile();
            PlayList.worldPlayListUUIDs.put(Paths.get(message.playlist[c]).toFile(), message.PlayerUUIDs[c]);
            PlayList.worldPlayListNamesMap.put(Paths.get(message.playlist[c]).toFile(), message.PlayerNames[c]);
        }
        PlayList.worldPlayListSize = message.playlistsize;

        PlayList.worldPlayListNames = message.PlayerNames;

    }
}
