package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;

public class ImportYoutubePlayListMMMonitor extends MusicManagerMonitor {
    public ImportYoutubePlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.ADD_ONLINE_PLAY_LIST;
    }
}
