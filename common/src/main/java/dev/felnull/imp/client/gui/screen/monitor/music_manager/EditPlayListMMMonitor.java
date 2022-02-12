package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.ImageInfo;
import org.jetbrains.annotations.Nullable;

public class EditPlayListMMMonitor extends SavedPlayListBaseMMMonitor {
    public EditPlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void done(ImageInfo imageInfo, String name) {

    }

    @Override
    protected @Nullable DoneType getDoneType() {
        return null;
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.DETAIL_PLAY_LIST;
    }

}
