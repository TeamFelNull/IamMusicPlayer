package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.PlayersFixedButtonsList;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.SmartRadioButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.otyacraftengine.client.gui.components.RadioButton;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CreatePlayListMMMonitor extends CreateBaseMMMonitor {
    private static final ResourceLocation CREATE_PLAYLIST_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/create_play_list.png");
    private static final Component PUBLIC_ST_TEXT = new TranslatableComponent("imp.text.publishingSettings");
    private static final Component PUBLIC_RDO_TEXT = new TranslatableComponent("imp.radioButton.public");
    private static final Component PRIVATE_RDO_TEXT = new TranslatableComponent("imp.radioButton.private");
    private static final Component READONLY_RDO_TEXT = new TranslatableComponent("imp.radioButton.readonly");
    private static final Component MEMBER_RDO_TEXT = new TranslatableComponent("imp.radioButton.member");
    private static final Component INITIAL_AUTHORITY_TEXT = new TranslatableComponent("imp.text.initialAuthority");
    private static final Component INVITE_TEXT = new TranslatableComponent("imp.text.invite");
    private static final Component INVITE_PLAYER_BY_MCID_OR_UUID = new TranslatableComponent("imp.text.invitePlayerByMCIDOrUUID");
    private final List<UUID> onlinePlayers = new ArrayList<>();
    private final List<UUID> invitePlayers = new ArrayList<>();
    private RadioButton publicRadio;
    private RadioButton privateRadio;
    private RadioButton initAuthReadOnlyRadio;
    private RadioButton initAuthMemberRadio;
    private EditBox invitePlayerByNameEditBox;
    private SmartButton addInvitePlayerButton;
    private PlayerUUIDLoadThread playerUUIDLoadThread;

    public CreatePlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void create(ImageInfo imageInfo, String name) {
        var pubType = getPublishingType();
        var intAuthType = getInitialAuthorityType();
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

        this.publicRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 5, getStartY() + 140, 20, 20, PUBLIC_RDO_TEXT, getPublishingType() == PublishingType.PUBLIC, true, () -> new RadioButton[]{this.publicRadio, this.privateRadio}, n -> setPublishingType(PublishingType.PUBLIC)));
        this.privateRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 95, getStartY() + 140, 20, 20, PRIVATE_RDO_TEXT, getPublishingType() == PublishingType.PRIVATE, true, () -> new RadioButton[]{this.publicRadio, this.privateRadio}, n -> setPublishingType(PublishingType.PRIVATE)));

        this.initAuthReadOnlyRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 189, getStartY() + 140, 20, 20, READONLY_RDO_TEXT, getInitialAuthorityType() == InitialAuthorityType.READ_ONLY, true, () -> new RadioButton[]{this.initAuthReadOnlyRadio, this.initAuthMemberRadio}, n -> setInitialAuthority(InitialAuthorityType.READ_ONLY)));
        this.initAuthMemberRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 279, getStartY() + 140, 20, 20, MEMBER_RDO_TEXT, getInitialAuthorityType() == InitialAuthorityType.MEMBER, true, () -> new RadioButton[]{this.initAuthReadOnlyRadio, this.initAuthMemberRadio}, n -> setInitialAuthority(InitialAuthorityType.MEMBER)));

        this.invitePlayerByNameEditBox = new EditBox(IIMPSmartRender.mc.font, getStartX() + 189, getStartY() + 112, 141, 12, new TranslatableComponent("imp.editBox.invitePlayerByName"));
        this.invitePlayerByNameEditBox.setMaxLength(300);
        this.invitePlayerByNameEditBox.setValue(getInvitePlayerName());
        this.invitePlayerByNameEditBox.setResponder(this::setInvitePlayerName);
        addRenderWidget(this.invitePlayerByNameEditBox);

        this.addInvitePlayerButton = addRenderWidget(new SmartButton(getStartX() + 333, getStartY() + 111, 33, 14, new TranslatableComponent("imp.button.addInvitePlayer"), n -> {
            startPlayerUUIDLoad(invitePlayerByNameEditBox.getValue());
        }));
        this.addInvitePlayerButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 106, 19, 11, 11);
        this.addInvitePlayerButton.setHideText(true);

        addRenderWidget(new PlayersFixedButtonsList(getStartX() + 189, getStartY() + 23, 87, 65, 5, new TranslatableComponent("imp.fixedList.onlinePlayers"), onlinePlayers, (fixedButtonsList, uuid, i, i1) -> addInvitePlayer(uuid)));
        addRenderWidget(new PlayersFixedButtonsList(getStartX() + 278, getStartY() + 23, 87, 65, 5, new TranslatableComponent("imp.fixedList.invitePlayers"), invitePlayers, (fixedButtonsList, uuid, i, i1) -> removeInvitePlayer(uuid)));

    }

    @Override
    public void depose() {
        super.depose();
        stopPlayerUUIDLoad();
    }

    @Override
    public boolean canCreate(MusicManagerBlockEntity blockEntity) {
        return super.canCreate(blockEntity) && getPublishingType(blockEntity) != null && getInitialAuthorityType(blockEntity) != null;
    }

    @Override
    public List<Component> getNotEntered(List<Component> names, MusicManagerBlockEntity blockEntity) {
        if (getPublishingType(blockEntity) == null)
            names.add(PUBLIC_ST_TEXT);
        if (getInitialAuthorityType(blockEntity) == null)
            names.add(INITIAL_AUTHORITY_TEXT);
        return super.getNotEntered(names, blockEntity);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(CREATE_PLAYLIST_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);


        drawSmartText(poseStack, PUBLIC_ST_TEXT, getStartX() + 5, getStartY() + 131);
        drawSmartText(poseStack, INITIAL_AUTHORITY_TEXT, getStartX() + 189, getStartY() + 131);

        drawSmartText(poseStack, INVITE_TEXT, getStartX() + 189, getStartY() + 13);

        drawSmartText(poseStack, INVITE_PLAYER_BY_MCID_OR_UUID, getStartX() + 189, getStartY() + 102);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtil.renderTextureSprite(CREATE_PLAYLIST_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 3, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);

        renderSmartTextSprite(poseStack, multiBufferSource, PUBLIC_ST_TEXT, 5, 131, OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);

        renderSmartRadioButton(poseStack, multiBufferSource, 5, 140, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, PUBLIC_RDO_TEXT, getPublishingType(blockEntity) == PublishingType.PUBLIC);
        renderSmartRadioButton(poseStack, multiBufferSource, 95, 140, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, PRIVATE_RDO_TEXT, getPublishingType(blockEntity) == PublishingType.PRIVATE);

        renderSmartTextSprite(poseStack, multiBufferSource, INITIAL_AUTHORITY_TEXT, 189, 131, OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);

        renderSmartRadioButton(poseStack, multiBufferSource, 189, 140, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, READONLY_RDO_TEXT, getInitialAuthorityType(blockEntity) == InitialAuthorityType.READ_ONLY);
        renderSmartRadioButton(poseStack, multiBufferSource, 279, 140, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, MEMBER_RDO_TEXT, getInitialAuthorityType(blockEntity) == InitialAuthorityType.MEMBER);
    }

    private void removeInvitePlayer(UUID uuid) {
        List<UUID> pls = new ArrayList<>(getInvitePlayers());
        pls.remove(uuid);
        setInvitePlayers(pls);
    }

    private void addInvitePlayer(UUID uuid) {
        if (IIMPSmartRender.mc.player.getGameProfile().equals(uuid)) return;
        List<UUID> pls = new ArrayList<>(getInvitePlayers());
        if (pls.contains(uuid)) return;
        pls.add(uuid);
        setInvitePlayers(pls);
    }

    public List<UUID> getInvitePlayers() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            return getInvitePlayers(musicManagerBlock);
        return Collections.emptyList();
    }

    public List<UUID> getInvitePlayers(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getMyInvitePlayers();
    }

    private void setInvitePlayers(List<UUID> players) {
        getScreen().insInvitePlayers(players);
    }

    public String getInvitePlayerName() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            return getInvitePlayerName(musicManagerBlock);
        return "";
    }

    public String getInvitePlayerName(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getMyInvitePlayerName();
    }

    private void setInvitePlayerName(String name) {
        getScreen().insInvitePlayerName(name);
    }

    public PublishingType getPublishingType() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            return getPublishingType(musicManagerBlock);
        return null;
    }

    public PublishingType getPublishingType(MusicManagerBlockEntity musicManagerBlockEntity) {
        return PublishingType.getTypeByName(musicManagerBlockEntity.getMyPublishing());
    }

    private void setPublishingType(PublishingType publishingType) {
        getScreen().insPublishing(publishingType == null ? "" : publishingType.name);
    }

    public InitialAuthorityType getInitialAuthorityType() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
            return getInitialAuthorityType(musicManagerBlock);
        return null;
    }

    public InitialAuthorityType getInitialAuthorityType(MusicManagerBlockEntity musicManagerBlockEntity) {
        return InitialAuthorityType.getTypeByName(musicManagerBlockEntity.getMyInitialAuthority());
    }

    private void setInitialAuthority(InitialAuthorityType initialAuthorityType) {
        getScreen().insInitialAuthority(initialAuthorityType == null ? "" : initialAuthorityType.name);
    }

    @Override
    public void tick() {
        super.tick();
        updateOnlinePlayers();
        updateInvitePlayers();
    }

    private static enum PublishingType {
        PUBLIC("public"), PRIVATE("private");
        private final String name;

        private PublishingType(String name) {
            this.name = name;
        }

        public static PublishingType getTypeByName(String type) {
            for (PublishingType value : values()) {
                if (value.name.equals(type))
                    return value;
            }
            return null;
        }
    }

    private static enum InitialAuthorityType {
        READ_ONLY("read_only"), MEMBER("member");
        private final String name;

        private InitialAuthorityType(String name) {
            this.name = name;
        }

        public static InitialAuthorityType getTypeByName(String type) {
            for (InitialAuthorityType value : values()) {
                if (value.name.equals(type))
                    return value;
            }
            return null;
        }
    }

    private void startPlayerUUIDLoad(String text) {
        stopPlayerUUIDLoad();
        playerUUIDLoadThread = new PlayerUUIDLoadThread(text);
        playerUUIDLoadThread.start();
    }

    private void stopPlayerUUIDLoad() {
        if (playerUUIDLoadThread != null) {
            playerUUIDLoadThread.interrupt();
            playerUUIDLoadThread = null;
        }
    }

    private class PlayerUUIDLoadThread extends Thread {
        private final String name;

        private PlayerUUIDLoadThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                addInvitePlayer(UUID.fromString(name));
                return;
            } catch (Exception ignored) {
            }


        }
    }

    private void updateOnlinePlayers() {
        onlinePlayers.clear();
        onlinePlayers.addAll(IIMPSmartRender.mc.player.connection.getOnlinePlayerIds());
        onlinePlayers.remove(IIMPSmartRender.mc.player.getGameProfile().getId());
        onlinePlayers.removeAll(getInvitePlayers());
    }

    private void updateInvitePlayers() {
        invitePlayers.clear();
        invitePlayers.addAll(getInvitePlayers());
    }
}
