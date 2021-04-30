package red.felnull.imp.client.music.loader;

import red.felnull.imp.client.music.player.IMusicPlayer;

public interface IMusicPlayerLoader {
    void init();

    IMusicPlayer createMusicPlayer(String identifier);
}
