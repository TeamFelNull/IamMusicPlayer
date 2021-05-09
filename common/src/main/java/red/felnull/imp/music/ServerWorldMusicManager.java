package red.felnull.imp.music;

import me.shedaniel.architectury.utils.GameInstance;
import net.minecraft.server.level.ServerPlayer;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.info.tracker.FixedMusicTracker;
import red.felnull.imp.packet.MusicClientInstructionMessage;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerWorldMusicManager {
    private static final ServerWorldMusicManager INSTANCE = new ServerWorldMusicManager();
    private final Map<UUID, MusicRinger> ringers = new HashMap<>();

    public static ServerWorldMusicManager getInstance() {
        return INSTANCE;
    }


    public void readyComplete(UUID playerId, UUID musicId, long eqtime) {
        ServerPlayer player = GameInstance.getServer().getPlayerList().getPlayer(playerId);

        IKSGPacketUtil.sendToClientPacket(player, new MusicClientInstructionMessage(MusicClientInstructionMessage.Type.PLAY, musicId, 0, new MusicPlayInfo(new FixedMusicTracker(player.position(), 1, 32))));

        System.out.println(eqtime);
    }

    public void readyFailure(UUID playerId, UUID musicId) {

    }


}
