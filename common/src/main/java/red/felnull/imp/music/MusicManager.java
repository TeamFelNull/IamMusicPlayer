package red.felnull.imp.music;

import red.felnull.imp.data.MusicSaveData;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.otyacraftengine.data.WorldDataManager;

import java.util.List;
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

    public List<MusicPlayList> getPlayList() {
        return getSaveData().getMusicPlaylists().values().stream().toList();
    }

    public List<MusicPlayList> getMyPlayList(UUID playerID) {
        return getPlayList().stream().filter(n -> n.getPlayerList().contains(playerID) && n.getAdministrator().getAuthority(playerID).canRead()).toList();
    }

    public List<MusicPlayList> getPublicPlayList() {
        return getPlayList().stream().filter(n -> n.getDetailed().isPubliced()).toList();
    }

    public List<MusicPlayList> getFilterPublicPlayList(UUID playerId) {
        //.filter(n -> !n.getPlayerList().contains(playerId))
        return getPlayList().stream().filter(n -> n.getDetailed().isPubliced()).filter(n -> n.getAdministrator().getAuthority(playerId).canRead()).toList();
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
