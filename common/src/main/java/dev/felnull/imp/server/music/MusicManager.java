package dev.felnull.imp.server.music;

import dev.felnull.imp.music.resource.AuthorityInfo;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.server.data.MusicSaveData;
import dev.felnull.otyacraftengine.server.data.WorldDataManager;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;
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

    public MusicPlayList getPlaylistByMusic(UUID musicId) {
        for (MusicPlayList value : getSaveData().getPlayLists().values()) {
            if (value.getMusicList().contains(musicId))
                return value;
        }
        return null;
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

    public void addPlayListToPlayer(UUID playListId, ServerPlayer player) {
        var uuid = player.getGameProfile().getId();
        var pl = getSaveData().getPlayLists().get(playListId);
        if (pl != null && pl.getAuthority().canJoin(uuid)) {
            pl.getAuthority().getRawAuthority().put(uuid, pl.getAuthority().getInitialAuthority());
            getSaveData().setDirty();
        }
    }

    public void editPlayList(UUID playListId, String name, ImageInfo image, List<UUID> invitePlayers, boolean publiced, boolean initMember, ServerPlayer player) {
        var pl = getSaveData().getPlayLists().get(playListId);
        if (pl == null) return;
        if (!pl.getAuthority().getAuthorityType(player.getGameProfile().getId()).canEdit()) return;
        var oldAuth = pl.getAuthority();
        Map<UUID, AuthorityInfo.AuthorityType> naus = new HashMap<>(oldAuth.getRawAuthority());
        List<UUID> rms = naus.entrySet().stream().filter(n -> n.getValue() == AuthorityInfo.AuthorityType.INVITATION).map(Map.Entry::getKey).toList();
        rms.forEach(naus::remove);
        invitePlayers.forEach(n -> naus.put(n, AuthorityInfo.AuthorityType.INVITATION));
        var auth = new AuthorityInfo(publiced, oldAuth.getOwner(), oldAuth.getOwnerName(), naus, initMember ? AuthorityInfo.AuthorityType.MEMBER : AuthorityInfo.AuthorityType.READ_ONLY);
        getSaveData().getPlayLists().put(playListId, new MusicPlayList(playListId, name, image, auth, pl.getMusicList(), pl.getCreateDate()));
        getSaveData().setDirty();
    }

    public static enum PlayListGetType {
        NO_BAN(n -> !n.isBan()),
        JOIN(n -> !n.isBan() && n.isMoreReadOnly()),
        NO_JOIN(n -> (!n.isBan() && !n.isMoreReadOnly()) || n.isInvitation());

        private final Function<AuthorityInfo.AuthorityType, Boolean> filter;

        private PlayListGetType(Function<AuthorityInfo.AuthorityType, Boolean> filter) {
            this.filter = filter;
        }

        private boolean check(AuthorityInfo.AuthorityType type) {
            return filter.apply(type);
        }
    }
}
