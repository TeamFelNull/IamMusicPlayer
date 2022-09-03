package dev.felnull.imp.server.music;

import dev.felnull.imp.advancements.IMPCriteriaTriggers;
import dev.felnull.imp.music.resource.AuthorityInfo;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.server.saveddata.MusicSaveData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;
import java.util.function.Function;

public class MusicManager {
    private static final MusicManager INSTANCE = new MusicManager();

    public static MusicManager getInstance() {
        return INSTANCE;
    }

    public MusicSaveData getSaveData(MinecraftServer server) {
        return MusicSaveData.get(server);
    }

    public void addMusic(MinecraftServer server, Music music) {
        getSaveData(server).getMusics().put(music.getUuid(), music);
        getSaveData(server).setDirty();
    }

    public MusicPlayList getPlaylistByMusic(MinecraftServer server, UUID musicId) {
        for (MusicPlayList value : getSaveData(server).getPlayLists().values()) {
            if (value.getMusicList().contains(musicId))
                return value;
        }
        return null;
    }

    public void addPlayList(MinecraftServer server, MusicPlayList playList) {
        getSaveData(server).getPlayLists().put(playList.getUuid(), playList);
        getSaveData(server).setDirty();
    }

    public void removeMusic(MinecraftServer server, UUID musicID) {
        getSaveData(server).getMusics().remove(musicID);
        getSaveData(server).getPlayLists().values().stream().map(MusicPlayList::getMusicList).forEach(n -> n.remove(musicID));
        getSaveData(server).setDirty();
    }

    public void removePlayList(MinecraftServer server, UUID playlistID) {
        getSaveData(server).getPlayLists().remove(playlistID);
        getSaveData(server).setDirty();
    }

    public void addMusicToPlayList(ServerPlayer player, UUID playlistId, Music music) {
        addMusicToPlayList(player.server, playlistId, music);
        IMPCriteriaTriggers.ADD_MUSIC.trigger(player);
    }

    public void addMusicToPlayList(MinecraftServer server, UUID playlistId, Music music) {
        if (getSaveData(server).getPlayLists().containsKey(playlistId)) {
            addMusic(server, music);
            getSaveData(server).getPlayLists().get(playlistId).getMusicList().add(music.getUuid());
            getSaveData(server).setDirty();
        }
    }

    public List<MusicPlayList> getPlayerPlayLists(ServerPlayer player, PlayListGetType getType) {
        var uuid = player.getGameProfile().getId();
        List<MusicPlayList> playLists = new ArrayList<>();
        getSaveData(player.server).getPlayLists().forEach((n, m) -> {
            if (getType.check(m.getAuthority().getAuthorityType(uuid)))
                playLists.add(m);
        });
        return Collections.unmodifiableList(playLists);
    }

    public void addPlayListToPlayer(MinecraftServer server, UUID playListId, ServerPlayer player) {
        var uuid = player.getGameProfile().getId();
        var pl = getSaveData(server).getPlayLists().get(playListId);
        if (pl != null && pl.getAuthority().canJoin(uuid)) {
            pl.getAuthority().getRawAuthority().put(uuid, pl.getAuthority().getInitialAuthority());
            getSaveData(server).setDirty();
        }
    }

    public void deletePlayList(MinecraftServer server, UUID playListId, ServerPlayer player) {
        var pl = getSaveData(server).getPlayLists().get(playListId);
        if (pl == null) return;
        if (!pl.getAuthority().getAuthorityType(player.getGameProfile().getId()).canDelete()) {
            exitPlayList(server, playListId, player);
            return;
        }
        for (UUID uuid : pl.getMusicList()) {
            getSaveData(server).getMusics().remove(uuid);
        }
        getSaveData(server).getPlayLists().remove(playListId);
        getSaveData(server).setDirty();
    }

    public void exitPlayList(MinecraftServer server, UUID playListId, ServerPlayer player) {
        var pl = getSaveData(server).getPlayLists().get(playListId);
        if (pl == null) return;
        getSaveData(server).getPlayLists().get(playListId).getAuthority().getRawAuthority().remove(player.getGameProfile().getId());
        getSaveData(server).setDirty();
    }

    public void deleteMusic(MinecraftServer server, UUID playListId, UUID musicId, ServerPlayer player) {
        var pl = getSaveData(server).getPlayLists().get(playListId);
        if (pl == null || !pl.getMusicList().contains(musicId)) return;
        var pid = player.getGameProfile().getId();
        var m = getSaveData(server).getMusics().get(musicId);
        if (m == null) return;
        boolean flg1 = pl.getAuthority().getAuthorityType(pid).canMusicDelete();
        boolean flg2 = m.getOwner().equals(pid);
        if (!flg1 && !flg2) return;
        getSaveData(server).getPlayLists().get(playListId).getMusicList().remove(musicId);
        getSaveData(server).getMusics().remove(musicId);
        getSaveData(server).setDirty();
    }

    public void changeAuthority(MinecraftServer server, UUID playListId, UUID targetPlayerId, AuthorityInfo.AuthorityType authorityType, ServerPlayer player) {
        if (authorityType == AuthorityInfo.AuthorityType.INVITATION) return;
        var pl = getSaveData(server).getPlayLists().get(playListId);
        if (pl == null) return;
        if (!pl.getAuthority().getPlayersAuthority().containsKey(targetPlayerId)) return;
        var ma = pl.getAuthority().getAuthorityType(player.getGameProfile().getId());
        var ta = pl.getAuthority().getAuthorityType(targetPlayerId);
        if (!ma.canChangeAuth(ta)) return;
        if (authorityType.isMoreAdmin() && !ma.isMoreOwner()) return;
        if (authorityType == AuthorityInfo.AuthorityType.NONE) {
            getSaveData(server).getPlayLists().get(playListId).getAuthority().getRawAuthority().remove(targetPlayerId);
        } else {
            getSaveData(server).getPlayLists().get(playListId).getAuthority().getRawAuthority().put(targetPlayerId, authorityType);
        }
        getSaveData(server).setDirty();
    }

    public void addMultipleMusic(MinecraftServer server, UUID playListId, List<Music> musics, ServerPlayer player) {
        var pl = getSaveData(server).getPlayLists().get(playListId);
        if (pl == null) return;
        if (!pl.getAuthority().getAuthorityType(player.getGameProfile().getId()).canAddMusic()) return;
        for (Music music : musics) {
            var am = new Music(UUID.randomUUID(), music.getName(), music.getAuthor(), music.getSource(), music.getImage(), player.getGameProfile().getId(), System.currentTimeMillis());
            addMusicToPlayList(player, pl.getUuid(), am);
        }
    }


    public void editMusic(MinecraftServer server, UUID musicId, UUID playListId, String name, ImageInfo image, ServerPlayer player) {
        var pl = getSaveData(server).getPlayLists().get(playListId);
        if (pl == null || !pl.getMusicList().contains(musicId)) return;
        var pid = player.getGameProfile().getId();
        if (pl.getAuthority().getAuthorityType(pid).isBan()) return;
        var m = getSaveData(server).getMusics().get(musicId);
        if (m == null || !m.getOwner().equals(pid)) return;
        var nm = new Music(musicId, name, m.getAuthor(), m.getSource(), image, m.getOwner(), m.getCreateDate());
        getSaveData(server).getMusics().put(musicId, nm);
        getSaveData(server).setDirty();
    }

    public void editPlayList(MinecraftServer server, UUID playListId, String name, ImageInfo image, List<UUID> invitePlayers, boolean publiced, boolean initMember, ServerPlayer player) {
        var pl = getSaveData(server).getPlayLists().get(playListId);
        if (pl == null) return;
        if (!pl.getAuthority().getAuthorityType(player.getGameProfile().getId()).canEdit()) return;
        var oldAuth = pl.getAuthority();
        Map<UUID, AuthorityInfo.AuthorityType> naus = new HashMap<>(oldAuth.getRawAuthority());
        List<UUID> rms = naus.entrySet().stream().filter(n -> n.getValue() == AuthorityInfo.AuthorityType.INVITATION).map(Map.Entry::getKey).toList();
        rms.forEach(naus::remove);
        invitePlayers.forEach(n -> {
            if (!naus.containsKey(n))
                naus.put(n, AuthorityInfo.AuthorityType.INVITATION);
        });
        var auth = new AuthorityInfo(publiced, oldAuth.getOwner(), oldAuth.getOwnerName(), naus, initMember ? AuthorityInfo.AuthorityType.MEMBER : AuthorityInfo.AuthorityType.READ_ONLY);
        getSaveData(server).getPlayLists().put(playListId, new MusicPlayList(playListId, name, image, auth, pl.getMusicList(), pl.getCreateDate()));
        getSaveData(server).setDirty();
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
