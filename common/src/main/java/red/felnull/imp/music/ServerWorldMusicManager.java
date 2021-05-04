package red.felnull.imp.music;

import me.shedaniel.architectury.utils.GameInstance;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.info.tracker.EntityMusicTracker;
import red.felnull.imp.packet.MusicClientInstructionMessage;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerWorldMusicManager {
    private static final ServerWorldMusicManager INSTANCE = new ServerWorldMusicManager();
    private Map<UUID, MusicRinger> ringers = new HashMap<>();

    public static ServerWorldMusicManager getInstance() {
        return INSTANCE;
    }


    public void readyComplete(UUID playerId, UUID musicId, long time) {
        IKSGPacketUtil.sendToClientPacket(GameInstance.getServer().getPlayerList().getPlayer(playerId), new MusicClientInstructionMessage(MusicClientInstructionMessage.Type.PLAY, musicId, 0, new MusicPlayInfo(new EntityMusicTracker(GameInstance.getServer().getPlayerList().getPlayer(playerId).position(), GameInstance.getServer().getPlayerList().getPlayer(playerId).getId()), 1, 32)));
        System.out.println(time);
    }

    public void readyFailure(UUID playerId, UUID musicId) {

    }


}
