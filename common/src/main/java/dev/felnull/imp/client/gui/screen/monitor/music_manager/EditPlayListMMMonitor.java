package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.networking.existence.BlockEntityExistence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class EditPlayListMMMonitor extends SavedPlayListBaseMMMonitor {
    public EditPlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public boolean done(ImageInfo imageInfo, String name) {
        var pubType = getPublishingType();
        var initAuthType = getInitialAuthorityType();
        var invitePlayers = getInvitePlayers();
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            NetworkManager.sendToServer(IMPPackets.MUSIC_PLAYLIST_EDIT, new IMPPackets.MusicPlayListMessage(musicManagerBlock.getSelectedPlayList(mc.player), name, imageInfo, pubType == PublishingType.PUBLIC, initAuthType == InitialAuthorityType.MEMBER, invitePlayers, BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity()), new ArrayList<>()).toFBB());
        return true;
    }

    @Override
    protected Collection<UUID> excludeInvitePlayers(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pl = getSelectedMusicPlayList(musicManagerBlockEntity);
        if (pl != null)
            return pl.getAuthority().getPlayersAuthority().keySet();
        return null;
    }

    @Override
    protected MusicManagerBlockEntity.@NotNull MonitorType getDoneBackMonitor() {
        return MusicManagerBlockEntity.MonitorType.DETAIL_PLAY_LIST;
    }

    @Override
    protected @Nullable DoneType getDoneType() {
        return DoneType.SAVE;
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.DETAIL_PLAY_LIST;
    }

    protected MusicPlayList getSelectedMusicPlayList() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getSelectedMusicPlayList(musicManagerBlockEntity);
        return null;
    }

    protected MusicPlayList getSelectedMusicPlayList(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pls = getSyncManager().getMyPlayList();
        if (pls == null)
            return null;
        return pls.stream().filter(n -> n.getUuid().equals(getSelectedPlayList(musicManagerBlockEntity))).findFirst().orElse(null);
    }

    protected UUID getSelectedPlayList(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getSelectedPlayList(mc.player);
    }

}
