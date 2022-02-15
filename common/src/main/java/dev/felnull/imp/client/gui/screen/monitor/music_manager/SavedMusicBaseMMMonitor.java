package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.MusicSource;

public abstract class SavedMusicBaseMMMonitor extends MusicBaseMMMonitor {

    public SavedMusicBaseMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    protected void setMusicSource(MusicSource source, String author) {
        getScreen().insMusicSource(source);
        setMusicAuthor(author);
    }
}
