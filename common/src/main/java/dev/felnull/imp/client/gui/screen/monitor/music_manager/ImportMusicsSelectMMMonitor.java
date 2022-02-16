package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImportMusicsSelectMMMonitor extends ImportSelectBaseMMMonitor {
    public ImportMusicsSelectMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public @NotNull MusicManagerBlockEntity.MonitorType getImportYoutubeMonitor() {
        return MusicManagerBlockEntity.MonitorType.IMPORT_YOUTUBE_PLAY_LIST_MUSICS;
    }

    @Override
    protected @Nullable MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.ADD_MUSIC;
    }
}
