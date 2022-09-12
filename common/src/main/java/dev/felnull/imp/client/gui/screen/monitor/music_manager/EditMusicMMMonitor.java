package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.networking.existence.BlockEntityExistence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EditMusicMMMonitor extends SavedMusicBaseMMMonitor {
    public EditMusicMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

    }

    @Override
    public boolean done(ImageInfo imageInfo, String name) {
        var mid = getSelectedMusicRaw();
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock && mid != null && musicManagerBlock.getSelectedPlayList(mc.player) != null)
            NetworkManager.sendToServer(IMPPackets.MUSIC_EDIT, new IMPPackets.MusicMessage(mid, musicManagerBlock.getSelectedPlayList(mc.player), name, "", imageInfo, MusicSource.EMPTY, BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity())).toFBB());
        return true;
    }

    @Override
    protected MusicManagerBlockEntity.@NotNull MonitorType getDoneBackMonitor() {
        return MusicManagerBlockEntity.MonitorType.DETAIL_MUSIC;
    }

    @Override
    protected DoneType getDoneType() {
        return DoneType.SAVE;
    }

    @Override
    protected @Nullable MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.DETAIL_MUSIC;
    }

    @Nullable
    private UUID getSelectedMusicRaw() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getSelectedMusicRaw(musicManagerBlockEntity);
        return null;
    }

    @Nullable
    private UUID getSelectedMusicRaw(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getSelectedMusic(mc.player);
    }
}
