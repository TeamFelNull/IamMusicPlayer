package red.felnull.imp.client.music.loader;

import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.subtitle.IMusicSubtitle;
import red.felnull.imp.music.resource.MusicLocation;

public interface IMusicLoader {
    void init();

    IMusicPlayer createMusicPlayer(MusicLocation location);

    default IMusicSubtitle createMusicSubtitle(MusicLocation location) {
        return null;
    }

}
