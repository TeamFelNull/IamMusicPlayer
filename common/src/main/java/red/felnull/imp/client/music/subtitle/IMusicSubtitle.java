package red.felnull.imp.client.music.subtitle;

import red.felnull.imp.client.music.player.IMusicPlayer;

import java.util.List;

public interface IMusicSubtitle {
    void init() throws Exception;

    boolean isLoaded();

    IMusicPlayer getMusicPlayer();

    List<SubtitleManager.MusicSubtitleEntry> getSubtitles();

}
