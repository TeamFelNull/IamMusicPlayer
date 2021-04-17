package red.felnull.imp.music;

import red.felnull.imp.data.MusicSaveData;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.otyacraftengine.data.WorldDataManager;

import java.util.UUID;

public class MusicManager {
    private static final MusicManager INSTANCE = new MusicManager();

    public static MusicManager getInstance() {
        return INSTANCE;
    }

    public MusicSaveData getSaveData() {
        return WorldDataManager.getInstance().getSaveData(MusicSaveData.class);
    }

    public void addMusic(Music music) {
        getSaveData().getMusics().put(music.getUUID(), music);
        getSaveData().setDirty();
    }

    public void addPlayList(MusicPlayList playList) {
        getSaveData().getMusicPlaylists().put(playList.getUUID(), playList);
        getSaveData().setDirty();
    }

    public void addMusic(UUID playlistID, Music music) {
        addMusic(music);
        if (getSaveData().getMusicPlaylists().containsKey(playlistID)) {
            getSaveData().getMusicPlaylists().get(playlistID).getMusicList().add(music.getUUID());
            getSaveData().setDirty();
        }
    }

    public void removeMusic(UUID musicID) {
        getSaveData().getMusics().remove(musicID);
        getSaveData().getMusicPlaylists().values().forEach(n -> n.getMusicList().remove(musicID));
        getSaveData().setDirty();
    }

    public void removePlayList(UUID playlistID) {
        getSaveData().getMusicPlaylists().remove(playlistID);
        getSaveData().setDirty();
    }
}
