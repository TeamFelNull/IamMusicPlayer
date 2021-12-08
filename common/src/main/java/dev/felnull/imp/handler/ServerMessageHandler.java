package dev.felnull.imp.handler;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.GameInstance;
import dev.felnull.imp.music.MusicManager;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerMessageHandler {
    public static void onMusicSyncRequestMessage(IMPPackets.MusicSyncRequestMessage message, NetworkManager.PacketContext packetContext) {
        packetContext.queue(() -> {
            var mm = MusicManager.getInstance();
            var pl = (ServerPlayer) packetContext.getPlayer();
            switch (message.syncType) {
                case PLAYLIST_MY_LIST -> sendMusicSyncData(pl, message.syncType, message.syncId, mm.getPlayerPlayLists(pl, MusicManager.PlayListGetType.JOIN), new ArrayList<>());
                case PLAYLIST_CAN_JOIN -> sendMusicSyncData(pl, message.syncType, message.syncId, mm.getPlayerPlayLists(pl, MusicManager.PlayListGetType.NO_JOIN), new ArrayList<>());
                case MUSIC_BY_PLAYLIST -> {
                    var mpl = mm.getSaveData().getPlayLists().get(message.syncId);
                    if (mpl != null && mpl.getAuthority().getAuthorityType(pl.getGameProfile().getId()).isMoreReadOnly()) {
                        List<Music> musics = new ArrayList<>();
                        mpl.getMusicList().forEach(n -> {
                            var music = mm.getSaveData().getMusics().get(n);
                            if (music != null)
                                musics.add(music);
                        });
                        sendMusicSyncData(pl, message.syncType, message.syncId, new ArrayList<>(), musics);
                    }
                }
            }
        });
    }

    public static void onMusicDataUpdate() {
        if (GameInstance.getServer() != null) {
            GameInstance.getServer().getPlayerList().getPlayers().forEach(n -> sendMusicSyncData(n, IMPPackets.MusicSyncType.UPDATE, n.getGameProfile().getId(), new ArrayList<>(), new ArrayList<>()));
        }
    }

    private static void sendMusicSyncData(ServerPlayer player, IMPPackets.MusicSyncType syncType, UUID uuid, List<MusicPlayList> playLists, List<Music> musics) {
        NetworkManager.sendToPlayer(player, IMPPackets.MUSIC_SYNC, new IMPPackets.MusicSyncResponseMessage(syncType, uuid, playLists, musics).toFBB());
    }
}
