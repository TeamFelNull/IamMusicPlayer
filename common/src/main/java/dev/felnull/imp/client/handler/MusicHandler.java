package dev.felnull.imp.client.handler;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.api.event.client.ClientMusicEvent;
import dev.felnull.imp.client.gui.components.MusicSubtitleOverlay;
import dev.felnull.imp.client.music.subtitle.SubtitleEntry;
import dev.felnull.imp.client.music.subtitle.SubtitleType;

public class MusicHandler {
    public static void init() {
        ClientMusicEvent.ADD_SUBTITLE.register(MusicHandler::addSubtitle);
    }

    public static void addSubtitle(SubtitleEntry entry) {
        if (IamMusicPlayer.CONFIG.subtitleType == SubtitleType.OVERLAY) {
            MusicSubtitleOverlay.SUBTITLE_OVERLAY.addSubtitle(entry);
        }
    }
}
