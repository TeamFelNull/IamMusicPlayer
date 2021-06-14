package red.felnull.imp.handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import red.felnull.imp.data.resource.AdministratorInformation;
import red.felnull.imp.music.MusicManager;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.imp.music.resource.MusicPlayListDetailed;
import red.felnull.imp.packet.PlayListCreateMessage;
import red.felnull.otyacraftengine.packet.IPacketMessageServerHandler;

import java.util.*;

public class PlayListCreateMessageHandler implements IPacketMessageServerHandler<PlayListCreateMessage> {
    @Override
    public boolean reversiveMessage(PlayListCreateMessage playListCreateMessage, ServerPlayer serverPlayer, ServerGamePacketListenerImpl serverGamePacketListener) {
        Map<UUID, AdministratorInformation.AuthorityType> adminData = new HashMap<>();
        playListCreateMessage.adminData.forEach((n, m) -> {
            if (m == AdministratorInformation.AuthorityType.BAN || m == AdministratorInformation.AuthorityType.ADMINISTRATOR) {
                adminData.put(n, m);
            }
        });
        adminData.put(serverPlayer.getGameProfile().getId(), AdministratorInformation.AuthorityType.OWNER);
        AdministratorInformation administratorInformation = new AdministratorInformation(adminData);
        List<UUID> players = new ArrayList<>();
        players.add(serverPlayer.getGameProfile().getId());
        MusicPlayList playList = new MusicPlayList(UUID.randomUUID(), playListCreateMessage.name, new MusicPlayListDetailed(playListCreateMessage.publiced), playListCreateMessage.image, administratorInformation, new ArrayList<>(), players);
        MusicManager.getInstance().addPlayList(playList);
        return true;
    }
}
