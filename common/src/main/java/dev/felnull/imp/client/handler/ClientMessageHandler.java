package dev.felnull.imp.client.handler;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.client.music.IMPMusicTrackerFactory;
import dev.felnull.imp.client.music.MusicRingerEngineConnector;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;
import net.minecraft.client.Minecraft;

import java.util.Collections;

public class ClientMessageHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void onMusicRingStateResponseMessage(IMPPackets.MusicRingStateMessage message, NetworkManager.PacketContext packetContext) {
        packetContext.queue(() -> {
            if (mc.getConnection() == null) return;
            switch (message.stateType()) {
                case PLAY -> MusicRingerEngineConnector.play(message.uuid(), message.elapsed());
                case STOP -> MusicRingerEngineConnector.stop(message.uuid());
                case UPDATE -> {
                    var ret = MusicRingerEngineConnector.update(message.uuid(), IMPMusicTrackerFactory.loadByTag(message.tracker()));
                    NetworkManager.sendToServer(IMPPackets.MUSIC_RING_UPDATE_RESULT, new IMPPackets.MusicRingUpdateResultMessage(message.uuid(), message.waitId(), ret).toFBB());
                }
            }
        });
    }

    public static void onMusicRingReadyResponseMessage(IMPPackets.MusicReadyMessage message, NetworkManager.PacketContext packetContext) {
        packetContext.queue(() -> {
            MusicRingerEngineConnector.load(message.uuid(), IMPMusicTrackerFactory.loadByTag(message.tracker()), message.source(), message.position(), (success, time, error, retry) -> {
                NetworkManager.sendToServer(IMPPackets.MUSIC_RING_READY_RESULT, new IMPPackets.MusicRingReadyResultMessage(message.waitId(), message.uuid(), success, retry, time).toFBB());
            });
        });
    }

    public static void onMusicSyncResponseMessage(IMPPackets.MusicSyncResponseMessage message, NetworkManager.PacketContext packetContext) {
        packetContext.queue(() -> {
            var msm = MusicSyncManager.getInstance();
            switch (message.syncType) {
                case UPDATE -> msm.reset();
                case PLAYLIST_MY_LIST -> {
                    msm.myPlayList = Collections.unmodifiableList(message.playLists);
                    int msmc = 0;
                    for (MusicPlayList playList : msm.myPlayList) {
                        msmc += playList.getMusicList().size();
                    }
                    msm.myPlayListInfo = new MusicSyncManager.PlayListInfo((int) msm.myPlayList.stream().map(n -> n.getAuthority().getOwner()).distinct().count(), msm.myPlayList.size(), msmc);
                }
                case PLAYLIST_CAN_JOIN -> {
                    msm.canJoinPlayList = Collections.unmodifiableList(message.playLists);
                    int msmc = 0;
                    for (MusicPlayList playList : msm.canJoinPlayList) {
                        msmc += playList.getMusicList().size();
                    }
                    msm.canJoinPlayListInfo = new MusicSyncManager.PlayListInfo((int) msm.canJoinPlayList.stream().map(n -> n.getAuthority().getOwner()).distinct().count(), msm.canJoinPlayList.size(), msmc);
                }
                case MUSIC_BY_PLAYLIST -> {
                    msm.musics.remove(message.syncId);
                    msm.musics.put(message.syncId, Collections.unmodifiableList(message.musics));
                }
            }
        });
    }
}
