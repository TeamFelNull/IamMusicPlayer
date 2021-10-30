package dev.felnull.imp.client.music.subtitle;

import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.music.MusicPlaybackInfo;

import java.util.List;

public interface IMusicSubtitle {
    boolean isExist();

    void load() throws Exception;

    List<SubtitleEntry> getSubtitle(IMusicPlayer player, MusicPlaybackInfo playbackInfo, long last, long current);
}
