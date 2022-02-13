package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.components.PlayersFixedButtonsList;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.SmartRadioButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.otyacraftengine.client.gui.components.RadioButton;
import dev.felnull.otyacraftengine.client.util.OEClientUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import dev.felnull.otyacraftengine.util.OEPlayerUtil;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class SavedPlayListBaseMMMonitor extends PlayListBaseMMMonitor {
    private static final ResourceLocation CREATE_PLAYLIST_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/create_play_list.png");
    private static final Component INVITE_TEXT = new TranslatableComponent("imp.text.invite");
    private static final Component INVITE_PLAYER_BY_MCID_OR_UUID_TEXT = new TranslatableComponent("imp.text.invitePlayerByMCIDOrUUID");
    private static final Component UNINVITED_TEXT = new TranslatableComponent("imp.text.uninvited");
    private static final Component INVITED_TEXT = new TranslatableComponent("imp.text.invited");
    private final List<UUID> onlinePlayers = new ArrayList<>();
    private final List<UUID> invitePlayers = new ArrayList<>();
    private EditBox invitePlayerByNameEditBox;
    private SmartButton addInvitePlayerButton;
    private RadioButton publicRadio;
    private RadioButton privateRadio;
    private RadioButton initAuthReadOnlyRadio;
    private RadioButton initAuthMemberRadio;
    private PlayerUUIDLoadThread playerUUIDLoadThread;

    public SavedPlayListBaseMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.publicRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 5, getStartY() + 140, 20, 20, PUBLIC_RDO_TEXT, getPublishingType() == CreatePlayListMMMonitor.PublishingType.PUBLIC, true, () -> new RadioButton[]{this.publicRadio, this.privateRadio}, n -> setPublishingType(CreatePlayListMMMonitor.PublishingType.PUBLIC)));
        this.privateRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 95, getStartY() + 140, 20, 20, PRIVATE_RDO_TEXT, getPublishingType() == CreatePlayListMMMonitor.PublishingType.PRIVATE, true, () -> new RadioButton[]{this.publicRadio, this.privateRadio}, n -> setPublishingType(CreatePlayListMMMonitor.PublishingType.PRIVATE)));

        this.initAuthReadOnlyRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 189, getStartY() + 140, 20, 20, READONLY_RDO_TEXT, getInitialAuthorityType() == CreatePlayListMMMonitor.InitialAuthorityType.READ_ONLY, true, () -> new RadioButton[]{this.initAuthReadOnlyRadio, this.initAuthMemberRadio}, n -> setInitialAuthority(CreatePlayListMMMonitor.InitialAuthorityType.READ_ONLY)));
        this.initAuthMemberRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 279, getStartY() + 140, 20, 20, MEMBER_RDO_TEXT, getInitialAuthorityType() == CreatePlayListMMMonitor.InitialAuthorityType.MEMBER, true, () -> new RadioButton[]{this.initAuthReadOnlyRadio, this.initAuthMemberRadio}, n -> setInitialAuthority(CreatePlayListMMMonitor.InitialAuthorityType.MEMBER)));

        this.invitePlayerByNameEditBox = new EditBox(IIMPSmartRender.mc.font, getStartX() + 189, getStartY() + 112, 141, 12, new TranslatableComponent("imp.editBox.invitePlayerByName"));
        this.invitePlayerByNameEditBox.setMaxLength(300);
        this.invitePlayerByNameEditBox.setValue(getInvitePlayerName());
        this.invitePlayerByNameEditBox.setResponder(this::setInvitePlayerName);
        addRenderWidget(this.invitePlayerByNameEditBox);

        this.addInvitePlayerButton = addRenderWidget(new SmartButton(getStartX() + 333, getStartY() + 111, 33, 14, new TranslatableComponent("imp.button.addInvitePlayer"), n -> {
            startPlayerUUIDLoad(invitePlayerByNameEditBox.getValue());
            invitePlayerByNameEditBox.setValue("");
        }));
        this.addInvitePlayerButton.setIcon(MusicManagerMonitor.WIDGETS_TEXTURE, 106, 19, 11, 11);
        this.addInvitePlayerButton.setHideText(true);

        addRenderWidget(new PlayersFixedButtonsList(getStartX() + 189, getStartY() + 23, 87, 65, 5, new TranslatableComponent("imp.fixedList.onlinePlayers"), onlinePlayers, (fixedButtonsList, uuid, i, i1) -> addInvitePlayer(uuid)));
        addRenderWidget(new PlayersFixedButtonsList(getStartX() + 278, getStartY() + 23, 87, 65, 5, new TranslatableComponent("imp.fixedList.invitePlayers"), invitePlayers, (fixedButtonsList, uuid, i, i1) -> removeInvitePlayer(uuid)));
    }

    @Override
    public boolean canDone(MusicManagerBlockEntity blockEntity) {
        return super.canDone(blockEntity) && getPublishingType(blockEntity) != null && getInitialAuthorityType(blockEntity) != null;
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


        drawSmartText(poseStack, INVITE_PLAYER_BY_MCID_OR_UUID_TEXT, getStartX() + 189, getStartY() + 102);

        drawSmartCenterText(poseStack, UNINVITED_TEXT, getStartX() + 230, getStartY() + 90);
        drawSmartCenterText(poseStack, INVITED_TEXT, getStartX() + 312, getStartY() + 90);
    }

    @Override
    protected Component getPlayerListName() {
        return INVITE_TEXT;
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        renderSmartRadioButtonSprite(poseStack, multiBufferSource, 5, 140, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, PUBLIC_RDO_TEXT, getPublishingType(blockEntity) == PublishingType.PUBLIC);
        renderSmartRadioButtonSprite(poseStack, multiBufferSource, 95, 140, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, PRIVATE_RDO_TEXT, getPublishingType(blockEntity) == PublishingType.PRIVATE);

        renderSmartRadioButtonSprite(poseStack, multiBufferSource, 189, 140, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, READONLY_RDO_TEXT, getInitialAuthorityType(blockEntity) == CreatePlayListMMMonitor.InitialAuthorityType.READ_ONLY);
        renderSmartRadioButtonSprite(poseStack, multiBufferSource, 279, 140, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, MEMBER_RDO_TEXT, getInitialAuthorityType(blockEntity) == CreatePlayListMMMonitor.InitialAuthorityType.MEMBER);

        OERenderUtil.renderTextureSprite(CREATE_PLAYLIST_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 3, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);

        renderSmartCenterTextSprite(poseStack, multiBufferSource, UNINVITED_TEXT, 230, 91, OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);
        renderSmartCenterTextSprite(poseStack, multiBufferSource, INVITED_TEXT, 312, 91, OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);

        renderSmartTextSprite(poseStack, multiBufferSource, INVITE_PLAYER_BY_MCID_OR_UUID_TEXT, 189, 102, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);

        renderSmartEditBoxSprite(poseStack, multiBufferSource, 189, 112, OERenderUtil.MIN_BREADTH * 4, 141, 12, i, j, onPxW, onPxH, monitorHeight, getInvitePlayerName(blockEntity));

        renderSmartButtonSprite(poseStack, multiBufferSource, 333, 111, OERenderUtil.MIN_BREADTH * 4, 33, 14, i, j, onPxW, onPxH, monitorHeight, MusicManagerMonitor.WIDGETS_TEXTURE, 106, 19, 11, 11, 256, 256);

        var opl = new ArrayList<>(IIMPSmartRender.mc.player.connection.getOnlinePlayerIds());
        opl.remove(IIMPSmartRender.mc.player.getGameProfile().getId());
        opl.removeAll(getInvitePlayers(blockEntity));

        for (int k = 0; k < Math.min(5, opl.size()); k++) {
            renderSmartButtonBoxSprite(poseStack, multiBufferSource, 189, 23 + (k * 13), OERenderUtil.MIN_BREADTH * 4, 87 - 10, 13, i, j, onPxW, onPxH, monitorHeight);
            OERenderUtil.renderPlayerFaceSprite(poseStack, multiBufferSource, opl.get(k), (189 + 1) * onPxW, monitorHeight - (23 + (k * 13) + 1 + 11) * onPxH, OERenderUtil.MIN_BREADTH * 6, 0, 0, 0, onPxH * 11, i, j);
            String name = OEClientUtil.getPlayerNameByUUID(opl.get(k)).orElse(opl.get(k).toString());
            var tx = new TextComponent(name);
            float sc = Math.min(1, (87f - 10f - 11f - 4f - 3f) / (float) IIMPSmartRender.mc.font.width(tx));
            renderSmartTextSprite(poseStack, multiBufferSource, tx, 189 + 11 + 4, 23 + (k * 13) + (13f - 6f) / 2f, OERenderUtil.MIN_BREADTH * 6, onPxW, onPxH, monitorHeight, sc, i);
        }

        renderScrollbarSprite(poseStack, multiBufferSource, 267, 23, OERenderUtil.MIN_BREADTH * 4, 65, i, j, onPxW, onPxH, monitorHeight, opl.size(), 5);

        var ipl = getInvitePlayers(blockEntity);

        for (int k = 0; k < Math.min(5, ipl.size()); k++) {
            renderSmartButtonBoxSprite(poseStack, multiBufferSource, 278, 23 + (k * 13), OERenderUtil.MIN_BREADTH * 4, 87 - 10, 13, i, j, onPxW, onPxH, monitorHeight);
            OERenderUtil.renderPlayerFaceSprite(poseStack, multiBufferSource, ipl.get(k), (278 + 1) * onPxW, monitorHeight - (23 + (k * 13) + 1 + 11) * onPxH, OERenderUtil.MIN_BREADTH * 6, 0, 0, 0, onPxH * 11, i, j);
            String name = OEClientUtil.getPlayerNameByUUID(ipl.get(k)).orElse(ipl.get(k).toString());
            var tx = new TextComponent(name);
            float sc = Math.min(1, (87f - 10f - 11f - 4f - 3f) / (float) IIMPSmartRender.mc.font.width(tx));
            renderSmartTextSprite(poseStack, multiBufferSource, tx, 278 + 11 + 4, 23 + (k * 13) + (13f - 6f) / 2f, OERenderUtil.MIN_BREADTH * 6, onPxW, onPxH, monitorHeight, sc, i);
        }

        renderScrollbarSprite(poseStack, multiBufferSource, 355, 23, OERenderUtil.MIN_BREADTH * 4, 65, i, j, onPxW, onPxH, monitorHeight, ipl.size(), 5);
    }

    @Override
    public void depose() {
        super.depose();
        stopPlayerUUIDLoad();
    }

    @Override
    public void tick() {
        super.tick();
        updateOnlinePlayers();
        updateInvitePlayers();
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

    protected void setPublishingType(@Nullable PublishingType publishingType) {
        getScreen().insPublishing(publishingType == null ? "" : publishingType.getName());
    }

    protected void setInvitePlayerName(@NotNull String name) {
        getScreen().insInvitePlayerName(name);
    }

    protected void setInitialAuthority(@Nullable InitialAuthorityType initialAuthorityType) {
        getScreen().insInitialAuthority(initialAuthorityType == null ? "" : initialAuthorityType.getName());
    }

    protected void removeInvitePlayer(@NotNull UUID uuid) {
        List<UUID> pls = new ArrayList<>(getInvitePlayers());
        pls.remove(uuid);
        setInvitePlayers(pls);
    }

    protected void addInvitePlayer(@NotNull UUID uuid) {
        if (IIMPSmartRender.mc.player.getGameProfile().equals(uuid)) return;
        List<UUID> pls = new ArrayList<>(getInvitePlayers());
        if (pls.contains(uuid)) return;
        pls.add(uuid);
        setInvitePlayers(pls);
    }

    protected void setInvitePlayers(List<UUID> players) {
        getScreen().insInvitePlayers(players);
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
            OEPlayerUtil.getUUIDByName(name).ifPresent(SavedPlayListBaseMMMonitor.this::addInvitePlayer);
        }
    }
}
