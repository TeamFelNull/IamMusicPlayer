package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.networking.BlockEntityExistence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditPlayListMMMonitor extends SavedPlayListBaseMMMonitor {
    public EditPlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
    }

    @Override
    public void done(ImageInfo imageInfo, String name) {
        var pubType = getPublishingType();
        var initAuthType = getInitialAuthorityType();
        var invitePlayers = getInvitePlayers();
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            NetworkManager.sendToServer(IMPPackets.MUSIC_PLAYLIST_EDIT, new IMPPackets.MusicPlayListMessage(musicManagerBlock.getMySelectedPlayList(), name, imageInfo, pubType == PublishingType.PUBLIC, initAuthType == InitialAuthorityType.MEMBER, invitePlayers, BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity())).toFBB());
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

}
