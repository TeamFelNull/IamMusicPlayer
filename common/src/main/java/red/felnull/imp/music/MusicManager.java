package red.felnull.imp.music;

import red.felnull.imp.data.MusicSaveData;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.otyacraftengine.data.WorldDataManager;

import java.util.ArrayList;
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

    public List<MusicPlayList> getPlayLists() {
        return getSaveData().getMusicPlaylists().values().stream().toList();
    }

    public List<MusicPlayList> getMyPlayLists(UUID playerID) {
        return getPlayLists().stream().filter(n -> n.getPlayerList().contains(playerID) && n.getAdministrator().getAuthority(playerID).canRead()).toList();
    }

    public List<MusicPlayList> getPublicPlayLists() {
        return getPlayLists().stream().filter(n -> n.getDetailed().isPubliced()).toList();
    }

    public List<MusicPlayList> getFilterPublicPlayLists(UUID playerId) {
        return getPlayLists().stream().filter(n -> n.getDetailed().isPubliced()).filter(n -> !n.getPlayerList().contains(playerId)).filter(n -> n.getAdministrator().getAuthority(playerId).canRead()).toList();
    }

    public List<Music> getPlayerAllMusics(UUID playerID) {
        List<Music> musics = new ArrayList<>();
        getMyPlayLists(playerID).forEach(n -> n.getMusicList().forEach(m -> musics.add(getMusic(m))));
        return musics;
    }

    public List<Music> getPlayListToMusics(UUID playlistID) {
        MusicPlayList playList = getPlayList(playlistID);
        if (playList != null)
            return playList.getMusicList().stream().map(this::getMusic).toList();

        return new ArrayList<>();
    }

    public Music getMusic(UUID musicID) {
        return getSaveData().getMusics().get(musicID);
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

    public MusicPlayList getPlayList(UUID playlistID) {
        return getSaveData().getMusicPlaylists().get(playlistID);
    }

    public void addMusicToMusicPlayList(UUID playlistID, UUID musicID) {
        MusicPlayList playList = getPlayList(playlistID);
        Music music = getMusic(musicID);
        if (playList != null && music != null && playList.getPlayerList().contains(music.getOwner()) && playList.getAdministrator().getAuthority(music.getOwner()).canAdd()) {
            playList.getMusicList().add(musicID);
            getSaveData().setDirty();
        }
    }

    public void addPlayerToMusicPlayList(UUID playlistID, UUID playerID) {
        MusicPlayList playList = getPlayList(playlistID);
        if (playList != null && playList.getAdministrator().getAuthority(playlistID).canRead()) {
            playList.getPlayerList().add(playerID);
            getSaveData().setDirty();
        }
    }

}
