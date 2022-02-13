package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import dev.felnull.otyacraftengine.networking.BlockEntityExistence;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class CreatePlayListMMMonitor extends SavedPlayListBaseMMMonitor {
    protected static final Component IMPORT_TEXT = new TranslatableComponent("imp.button.import");

    public CreatePlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        addRenderWidget(new SmartButton(getStartX() + width - 95 - 87, getStartY() + 180, 87, 15, IMPORT_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.IMPORT_PLAY_LIST_SELECT)));
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        renderSmartButtonSprite(poseStack, multiBufferSource, width - 95 - 87, 180, OERenderUtil.MIN_BREADTH * 4, 87, 15, i, j, onPxW, onPxH, monitorHeight, IMPORT_TEXT, true);
    }

    @Override
    public void done(ImageInfo imageInfo, String name) {
        var pubType = getPublishingType();
        var initAuthType = getInitialAuthorityType();
        var invitePlayers = getInvitePlayers();
        NetworkManager.sendToServer(IMPPackets.MUSIC_PLAYLIST_ADD, new IMPPackets.MusicPlayListMessage(name, imageInfo, pubType == PublishingType.PUBLIC, initAuthType == InitialAuthorityType.MEMBER, invitePlayers, BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity())).toFBB());
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
