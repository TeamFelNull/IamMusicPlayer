package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.ImageInfo;
import org.jetbrains.annotations.Nullable;

public class EditMusicMMMonitor extends ImageNameBaseMMMonitor {
    public EditMusicMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

    }

    @Override
    public void done(ImageInfo imageInfo, String name) {
        System.out.println("test");
    }

    @Override
    protected DoneType getDoneType() {
        return DoneType.SAVE;
    }

    @Override
    protected @Nullable MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.PLAY_LIST;
    }
}
