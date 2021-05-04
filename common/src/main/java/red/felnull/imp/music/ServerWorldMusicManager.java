package red.felnull.imp.music;

import me.shedaniel.architectury.utils.GameInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.AABB;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.info.tracker.EntityMusicTracker;
import red.felnull.imp.packet.MusicClientInstructionMessage;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServerWorldMusicManager {
    private static final ServerWorldMusicManager INSTANCE = new ServerWorldMusicManager();
    private Map<UUID, MusicRinger> ringers = new HashMap<>();

    public static ServerWorldMusicManager getInstance() {
        return INSTANCE;
    }


    public void readyComplete(UUID playerId, UUID musicId, long eqtime) {
        ServerPlayer player = GameInstance.getServer().getPlayerList().getPlayer(playerId);
        List<Creeper> entityList = player.level.getEntitiesOfClass(Creeper.class, new AABB(player.position().x - 10, player.position().y - 10, player.position().z - 10, player.position().x + 10, player.position().y + 10, player.position().z + 10F));

        entityList.stream().findAny().ifPresent(n -> {
            System.out.println(n.getName().getString());
            IKSGPacketUtil.sendToClientPacket(player, new MusicClientInstructionMessage(MusicClientInstructionMessage.Type.PLAY, musicId, 0, new MusicPlayInfo(new EntityMusicTracker(player.position(), n.getId()), 1, 32)));
        });


        System.out.println(eqtime);
    }

    public void readyFailure(UUID playerId, UUID musicId) {

    }


}
