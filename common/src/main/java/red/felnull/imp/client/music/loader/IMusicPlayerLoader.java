package red.felnull.imp.client.music.loader;

import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.music.resource.MusicLocation;

public interface IMusicPlayerLoader {
    void init();

    IMusicPlayer createMusicPlayer(MusicLocation location);
}
