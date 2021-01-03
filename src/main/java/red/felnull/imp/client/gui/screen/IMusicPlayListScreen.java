package red.felnull.imp.client.gui.screen;

import red.felnull.imp.music.resource.PlayList;
import red.felnull.imp.music.resource.PlayMusic;

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
