package red.felnull.imp.handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import red.felnull.imp.music.MusicManager;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.imp.packet.PlayMusicCreateMessage;
import red.felnull.otyacraftengine.packet.IPacketMessageServerHandler;

import java.util.UUID;

public class PlayMusicCreateMessageHandler implements IPacketMessageServerHandler<PlayMusicCreateMessage> {
    @Override
    public boolean reversiveMessage(PlayMusicCreateMessage playMusicCreateMessage, ServerPlayer serverPlayer, ServerGamePacketListenerImpl serverGamePacketListener) {
        Music music = new Music(UUID.randomUUID(), playMusicCreateMessage.name, playMusicCreateMessage.source, playMusicCreateMessage.image, serverPlayer.getGameProfile().getId());
        MusicManager manager = MusicManager.getInstance();
        MusicPlayList playList = manager.getPlayList(playMusicCreateMessage.playListID);
        if (playList != null && playList.getPlayerList().contains(serverPlayer.getGameProfile().getId()) && playList.getAdministrator().getAuthority(serverPlayer.getGameProfile().getId()).canAdd()) {
            manager.addMusic(music);
            manager.addMusicToMusicPlayList(playMusicCreateMessage.playListID, music.getUUID());
        }
        return true;
    }
}
