package red.felnull.imp.client.music.factory;

import red.felnull.imp.client.music.player.IMusicPlayer;

public interface IMusicPlayerFactory {
    void init();

    IMusicPlayer createMusicPlayer(String identifier);
}
