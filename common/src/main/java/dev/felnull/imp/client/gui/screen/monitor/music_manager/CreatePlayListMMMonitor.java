package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.networking.BlockEntityExistence;

public class CreatePlayListMMMonitor extends SavedPlayListBaseMMMonitor {

    public CreatePlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void done(ImageInfo imageInfo, String name) {
        var pubType = getPublishingType();
        var initAuthType = getInitialAuthorityType();
        var invitePlayers = getInvitePlayers();
        NetworkManager.sendToServer(IMPPackets.MUSIC_PLAYLIST_ADD, new IMPPackets.MusicPlayListAddMessage(name, imageInfo, pubType == PublishingType.PUBLIC, initAuthType == InitialAuthorityType.MEMBER, invitePlayers, BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity())).toFBB());
    }

    @Override
    protected DoneType getDoneType() {
        return DoneType.CREATE;
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.ADD_PLAY_LIST;
    }
}
