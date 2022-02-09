package dev.felnull.imp.client.handler;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.api.event.ClientMusicEvent;
import dev.felnull.imp.client.gui.components.MusicSubtitleOverlay;
import dev.felnull.imp.client.music.subtitle.SubtitleEntry;
import dev.felnull.imp.client.music.subtitle.SubtitleType;
import dev.felnull.imp.client.music.tracker.IMPMusicTrackers;
import dev.felnull.otyacraftengine.client.gui.subtitle.ICustomTimeSubtitle;
import dev.felnull.otyacraftengine.client.gui.subtitle.IDynamicSubtitle;
import dev.felnull.otyacraftengine.client.util.OEClientUtil;
import net.minecraft.client.gui.components.SubtitleOverlay;

public class MusicHandler {
    public static void init() {
        ClientMusicEvent.ADD_SUBTITLE.register(MusicHandler::addSubtitle);
    }

    public static void addSubtitle(SubtitleEntry entry) {
        if (IamMusicPlayer.CONFIG.subtitleType == SubtitleType.OVERLAY) {
            MusicSubtitleOverlay.SUBTITLE_OVERLAY.addSubtitle(entry);
        } else if (IamMusicPlayer.CONFIG.subtitleType == SubtitleType.VANILLA) {
            var sub = new SubtitleOverlay.Subtitle(entry.component(), entry.musicPlayer().getCoordinatePosition());
            ((IDynamicSubtitle) sub).setDynamicLocation(IMPMusicTrackers.createTracker(entry.playbackInfo()).getPosition());
            ((ICustomTimeSubtitle) sub).setCustomTime(entry.duration());
            OEClientUtil.addSubtitle(sub, false);
        }
    }
}
