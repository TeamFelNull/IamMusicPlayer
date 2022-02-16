package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.networking.BlockEntityExistence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DeleteMusicMMMonitor extends DeleteBaseMMMonitor {
    public DeleteMusicMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void onDelete() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            NetworkManager.sendToServer(IMPPackets.MUSIC_OR_PLAYLIST_DELETE, new IMPPackets.MusicOrPlayListDeleteMessage(getSelectedPlayList(musicManagerBlock), getSelectedMusicRaw(musicManagerBlock), BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity()), true).toFBB());
    }

    @Override
    public @NotNull String getWaringName(MusicManagerBlockEntity musicManagerBlockEntity) {
        var sm = getSelectedMusic(musicManagerBlockEntity);
        if (sm != null)
            return sm.getName();
        return "";
    }

    @Override
    protected @Nullable MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.DETAIL_MUSIC;
    }

    @Nullable
    private Music getSelectedMusic(MusicManagerBlockEntity musicManagerBlockEntity) {
        var id = getSelectedMusicRaw(musicManagerBlockEntity);
        var pl = getSelectedPlayList(musicManagerBlockEntity);
        if (id != null && pl != null) {
            var sms = getSyncManager().getMusics(pl);
            if (sms != null)
                return sms.stream().filter(n -> id.equals(n.getUuid())).findFirst().orElse(null);
        }
        return null;
    }

    @Nullable
    private UUID getSelectedPlayList(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getMySelectedPlayList();
    }

    @Nullable
    private UUID getSelectedMusicRaw(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getMySelectedMusic();
    }
}
