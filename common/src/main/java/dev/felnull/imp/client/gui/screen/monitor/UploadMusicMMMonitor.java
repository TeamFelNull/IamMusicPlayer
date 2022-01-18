package dev.felnull.imp.client.gui.screen.monitor;

import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;

public class UploadMusicMMMonitor extends MusicManagerMonitor {
    private static final String RELAY_SERVER_URL = "https://www.morimori0317.net/imp-relay-server";

    public UploadMusicMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.ADD_MUSIC;
    }

    private String getRelayServerURL() {
        return RELAY_SERVER_URL;
    }
}
