package red.felnull.imp.data;

import net.minecraft.nbt.CompoundTag;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.imp.util.NbtUtils;
import red.felnull.otyacraftengine.data.IkisugiSaveData;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MusicSaveData extends IkisugiSaveData {
    private final Map<UUID, Music> MUSICS = new HashMap<>();
    private final Map<UUID, MusicPlayList> MUSIC_PLAYLISTS = new HashMap<>();

    @Override
    public void load(CompoundTag tag) {
        List<Music> musics = new ArrayList<>();
        NbtUtils.readMusics(tag, "Music", musics);
        musics.forEach(n -> MUSICS.put(n.getUUID(), n));

        List<MusicPlayList> playLists = new ArrayList<>();
        NbtUtils.readMusicPlayLists(tag, "PlayList", playLists);
        playLists.forEach(n -> MUSIC_PLAYLISTS.put(n.getUUID(), n));
    }

    @Override
    public Path getSavePath() {
        return Paths.get("musicdata.dat");
    }

    @Override
    public void clear() {
        MUSICS.clear();
        MUSIC_PLAYLISTS.clear();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        List<Music> musics = new ArrayList<>(MUSICS.values());
        NbtUtils.writeMusics(tag, "Music", musics);

        List<MusicPlayList> playLists = new ArrayList<>(MUSIC_PLAYLISTS.values());
        NbtUtils.writeMusicPlayLists(tag, "PlayList", playLists);
        return tag;
    }

    private Map<UUID, Music> getMusics() {
        return MUSICS;
    }

    private Map<UUID, MusicPlayList> getMusicPlaylists() {
        return MUSIC_PLAYLISTS;
    }

    public void addMusic(Music music) {
        setDirty();
        getMusics().put(music.getUUID(), music);
    }

    public void addPlayList(MusicPlayList playList) {
        setDirty();
        getMusicPlaylists().put(playList.getUUID(), playList);
    }
}
