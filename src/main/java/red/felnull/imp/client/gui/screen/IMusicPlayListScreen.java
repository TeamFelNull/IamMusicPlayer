package red.felnull.imp.client.gui.screen;

import red.felnull.imp.musicplayer.PlayList;
import red.felnull.imp.musicplayer.PlayMusic;

import java.util.List;

public interface IMusicPlayListScreen {
    void updatePlayList();

    void updatePlayMusic();

    default void updateAll() {
        updatePlayList();
        updatePlayMusic();
    }

    PlayList getCurrentSelectedPlayList();

    void setCurrentSelectedPlayList(PlayList list);

    List<PlayList> getJonedAllPlayLists();

    List<PlayMusic> getCurrentPLPlayMusic();
}
