package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.networking.existence.BlockEntityExistence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DeletePlayListMMMonitor extends DeleteBaseMMMonitor {
    public DeletePlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void onDelete() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            NetworkManager.sendToServer(IMPPackets.MUSIC_OR_PLAYLIST_DELETE, new IMPPackets.MusicOrPlayListDeleteMessage(getSelectedPlayList(musicManagerBlock), UUID.randomUUID(), BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity()), false).toFBB());
    }

    @Override
    public @NotNull String getWaringName(MusicManagerBlockEntity musicManagerBlockEntity) {
        var mp = getSelectedMusicPlayList(musicManagerBlockEntity);
        if (mp != null) return mp.getName();
        return "";
    }

    @Override
    protected @Nullable MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.DETAIL_PLAY_LIST;
    }

    protected MusicPlayList getSelectedMusicPlayList(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pls = getSyncManager().getMyPlayList();
        if (pls == null) return null;
        return getSyncManager().getMyPlayList().stream().filter(n -> n.getUuid().equals(getSelectedPlayList(musicManagerBlockEntity))).findFirst().orElse(null);
    }

    protected UUID getSelectedPlayList(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getMySelectedPlayList();
    }
}
