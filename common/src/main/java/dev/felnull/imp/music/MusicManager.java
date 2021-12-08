package dev.felnull.imp.music;

import dev.felnull.imp.data.MusicSaveData;
import dev.felnull.imp.music.resource.AuthorityInfo;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.data.WorldDataManager;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class MusicManager {
    private static final MusicManager INSTANCE = new MusicManager();

    public static MusicManager getInstance() {
        return INSTANCE;
    }

    public MusicSaveData getSaveData() {
        return WorldDataManager.getInstance().getSaveData(MusicSaveData.class);
    }

    public void addMusic(Music music) {
        getSaveData().getMusics().put(music.getUuid(), music);
        getSaveData().setDirty();
    }

    public void addPlayList(MusicPlayList playList) {
        getSaveData().getPlayLists().put(playList.getUuid(), playList);
        getSaveData().setDirty();
    }

    public void removeMusic(UUID musicID) {
        getSaveData().getMusics().remove(musicID);
        getSaveData().getPlayLists().values().stream().map(MusicPlayList::getMusicList).forEach(n -> n.remove(musicID));
        getSaveData().setDirty();
    }

    public void removePlayList(UUID playlistID) {
        getSaveData().getPlayLists().remove(playlistID);
        getSaveData().setDirty();
    }

    public void addMusicToPlayList(UUID playlistId, Music music) {
        if (getSaveData().getPlayLists().containsKey(playlistId)) {
            addMusic(music);
            getSaveData().getPlayLists().get(playlistId).getMusicList().add(music.getUuid());
            getSaveData().setDirty();
        }
    }

    public List<MusicPlayList> getPlayerPlayLists(ServerPlayer player, PlayListGetType getType) {
        var uuid = player.getGameProfile().getId();
        List<MusicPlayList> playLists = new ArrayList<>();
        getSaveData().getPlayLists().forEach((n, m) -> {
            if (getType.check(m.getAuthority().getAuthorityType(uuid)))
                playLists.add(m);
        });
        return Collections.unmodifiableList(playLists);
    }

    public static enum PlayListGetType {
        NO_BAN(n -> !n.isBan()),
        JOIN(n -> !n.isBan() && n.isMoreReadOnly()),
        NO_JOIN(n -> !n.isBan() && !n.isMoreReadOnly());

        private final Function<AuthorityInfo.AuthorityType, Boolean> filter;

        private PlayListGetType(Function<AuthorityInfo.AuthorityType, Boolean> filter) {
            this.filter = filter;
        }

        private boolean check(AuthorityInfo.AuthorityType type) {
            return filter.apply(type);
        }
    }
}
