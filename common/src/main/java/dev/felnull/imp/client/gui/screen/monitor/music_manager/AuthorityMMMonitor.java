package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.AuthorityPlayersFixedButtonsList;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.SmartRadioButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.AuthorityInfo;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import dev.felnull.otyacraftengine.client.gui.components.RadioButton;
import dev.felnull.otyacraftengine.client.util.OEClientUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import dev.felnull.otyacraftengine.networking.BlockEntityExistence;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class AuthorityMMMonitor extends MusicManagerMonitor {
    private static final ResourceLocation AUTHORITY_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/authority.png");
    private static final Component BACK_TEXT = new TranslatableComponent("gui.back");
    private static final Component EXPULSION_BUTTON_TEXT = new TranslatableComponent("imp.button.expulsion").withStyle(ChatFormatting.DARK_RED);
    private static final Component CANT_CHANGE_AUTHORITY = new TranslatableComponent("imp.text.cantChangeAuthority");
    private static final Component READONLY_RDO_TEXT = new TranslatableComponent("imp.radioButton.readonly");
    private static final Component MEMBER_RDO_TEXT = new TranslatableComponent("imp.radioButton.member");
    private static final Component BAN_RDO_TEXT = new TranslatableComponent("imp.radioButton.ban");
    private static final Component ADMIN_RDO_TEXT = new TranslatableComponent("imp.radioButton.admin");
    private final List<UUID> members = new ArrayList<>();
    private List<MusicPlayList> cashPlayLists;
    private SmartRadioButton readOnlyRadio;
    private SmartRadioButton memberRadio;
    private SmartRadioButton banRadio;
    private SmartRadioButton adminRadio;
    private SmartButton expulsionButton;

    public AuthorityMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        addRenderWidget(new SmartButton(getStartX() + 5, getStartY() + 180, 87, 15, BACK_TEXT, n -> {
            if (getParentType() != null)
                insMonitor(getParentType());
        }));

        addRenderWidget(new AuthorityPlayersFixedButtonsList(getStartX() + 6, getStartY() + 23, 175, 135, 9, new TranslatableComponent("imp.fixedList.authorityPlayers"), members, new FixedButtonsList.PressEntry<UUID>() {
            @Override
            public void onPressEntry(FixedButtonsList<UUID> fixedButtonsList, UUID uuid, int i, int i1) {
                setSelectedPlayer(uuid);
            }
        }, n -> n.equals(getSelectedPlayer())));

        Supplier<RadioButton[]> rdos = () -> new RadioButton[]{this.readOnlyRadio, this.memberRadio, this.adminRadio, this.banRadio};
        this.readOnlyRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 188, getStartY() + 47, 20, 20, READONLY_RDO_TEXT, getAuthorityType(getSelectedPlayer()) == AuthorityInfo.AuthorityType.READ_ONLY, true, rdos, n -> sendChangeAuthorityPacket(getSelectedPlayer(), AuthorityInfo.AuthorityType.READ_ONLY)));
        this.memberRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 188, getStartY() + 72, 20, 20, MEMBER_RDO_TEXT, getAuthorityType(getSelectedPlayer()) == AuthorityInfo.AuthorityType.MEMBER, true, rdos, n -> sendChangeAuthorityPacket(getSelectedPlayer(), AuthorityInfo.AuthorityType.MEMBER)));
        this.banRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 188, getStartY() + 97, 20, 20, BAN_RDO_TEXT, getAuthorityType(getSelectedPlayer()) == AuthorityInfo.AuthorityType.BAN, true, rdos, n -> sendChangeAuthorityPacket(getSelectedPlayer(), AuthorityInfo.AuthorityType.BAN)));
        this.adminRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 188, getStartY() + 122, 20, 20, ADMIN_RDO_TEXT, getAuthorityType(getSelectedPlayer()) == AuthorityInfo.AuthorityType.READ_ONLY, true, rdos, n -> sendChangeAuthorityPacket(getSelectedPlayer(), AuthorityInfo.AuthorityType.ADMIN)));

        this.readOnlyRadio.visible = canEdit(getSelectedPlayer());
        this.memberRadio.visible = canEdit(getSelectedPlayer());
        this.banRadio.visible = canEdit(getSelectedPlayer());
        this.adminRadio.visible = canEdit(getSelectedPlayer()) && canSetAdmin();

        this.expulsionButton = addRenderWidget(new SmartButton(getStartX() + 188, getStartY() + 147, 87, 15, EXPULSION_BUTTON_TEXT, n -> {
            sendChangeAuthorityPacket(getSelectedPlayer(), AuthorityInfo.AuthorityType.NONE);
            setSelectedPlayer(null);
        }));
        this.expulsionButton.visible = canEdit(getSelectedPlayer());

        this.cashPlayLists = getSyncManager().getMyPlayList();
        updateMembers();
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(AUTHORITY_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width / (getSelectedPlayer() != null ? 1f : 2f), height, width, height);
        var sp = getSelectedPlayer();
        if (sp != null) {
            OERenderUtil.drawPlayerFace(poseStack, sp, getStartX() + 189, getStartY() + 23, 21);
            var str = OEClientUtil.getPlayerNameByUUID(sp).orElseGet(sp::toString);
            drawSmartFixedWidthText(poseStack, new TextComponent(str), getStartX() + 212, getStartY() + 25, 150);
            drawSmartFixedWidthText(poseStack, getAuthorityType(sp).getText(), getStartX() + 212, getStartY() + 35, 150);
            if (!canEdit(sp))
                drawSmartText(poseStack, CANT_CHANGE_AUTHORITY, getStartX() + 188, getStartY() + 47);
        }
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtil.renderTextureSprite(AUTHORITY_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth / (getSelectedPlayer(blockEntity) != null ? 1f : 2f), monitorHeight, 0, 0, width / (getSelectedPlayer(blockEntity) != null ? 1f : 2f), height, width, height, i, j);

        renderSmartButtonSprite(poseStack, multiBufferSource, 5, 180, OERenderUtil.MIN_BREADTH * 4, 87, 15, i, j, onPxW, onPxH, monitorHeight, BACK_TEXT, true);

        var sp = getSelectedPlayer(blockEntity);
        if (sp != null) {
            renderPlayerFaceSprite(poseStack, multiBufferSource, sp, 189, 23, OERenderUtil.MIN_BREADTH * 4, 21, i, j, onPxW, onPxH, monitorHeight);
            var str = OEClientUtil.getPlayerNameByUUID(sp).orElseGet(sp::toString);
            renderSmartTextSprite(poseStack, multiBufferSource, new TextComponent(OERenderUtil.getWidthString(str, 130, "...")), 215, 26, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
            renderSmartTextSprite(poseStack, multiBufferSource, getAuthorityType(blockEntity, sp).getText(), 215, 36, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
            if (canEdit(blockEntity, sp)) {
                renderSmartRadioButtonSprite(poseStack, multiBufferSource, 188, 47, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, READONLY_RDO_TEXT, getAuthorityType(blockEntity, getSelectedPlayer(blockEntity)) == AuthorityInfo.AuthorityType.READ_ONLY);
                renderSmartRadioButtonSprite(poseStack, multiBufferSource, 188, 72, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, MEMBER_RDO_TEXT, getAuthorityType(blockEntity, getSelectedPlayer(blockEntity)) == AuthorityInfo.AuthorityType.MEMBER);
                renderSmartRadioButtonSprite(poseStack, multiBufferSource, 188, 97, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, BAN_RDO_TEXT, getAuthorityType(blockEntity, getSelectedPlayer(blockEntity)) == AuthorityInfo.AuthorityType.BAN);
                renderSmartRadioButtonSprite(poseStack, multiBufferSource, 188, 122, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, ADMIN_RDO_TEXT, getAuthorityType(blockEntity, getSelectedPlayer(blockEntity)) == AuthorityInfo.AuthorityType.ADMIN);

                renderSmartButtonSprite(poseStack, multiBufferSource, 188, 147, OERenderUtil.MIN_BREADTH * 4, 87, 15, i, j, onPxW, onPxH, monitorHeight, EXPULSION_BUTTON_TEXT, true);
            } else {
                renderSmartTextSprite(poseStack, multiBufferSource, CANT_CHANGE_AUTHORITY, 188, 48, OERenderUtil.MIN_BREADTH * 4, onPxW, onPxH, monitorHeight, i);
            }
        }

        List<UUID> pls = new ArrayList<>();

        var pl = getSelectedMusicPlayList(blockEntity);
        if (pl != null)
            pls.addAll(pl.getAuthority().getPlayersAuthority().entrySet().stream().filter(n -> n.getValue() != AuthorityInfo.AuthorityType.NONE && n.getValue() != AuthorityInfo.AuthorityType.INVITATION).map(Map.Entry::getKey).toList());

        renderFixedListSprite(poseStack, multiBufferSource, 6, 23, OERenderUtil.MIN_BREADTH * 4, 175, 135, i, j, onPxW, onPxH, monitorHeight, pls, 9, (poseStack1, multiBufferSource1, x, y, z, w, h, i1, j1, entry) -> {
            renderSmartButtonBoxSprite(poseStack1, multiBufferSource1, x, y, z + OERenderUtil.MIN_BREADTH, w, h, i1, j1, onPxW, onPxH, monitorHeight, entry.equals(getSelectedPlayer(blockEntity)));
            renderPlayerFaceSprite(poseStack1, multiBufferSource1, entry, x + 1, y + 1, z + OERenderUtil.MIN_BREADTH * 3, h - 2, i1, j1, onPxW, onPxH, monitorHeight);
            renderSmartTextSprite(poseStack1, multiBufferSource1, new TextComponent(OERenderUtil.getWidthString(OEClientUtil.getPlayerNameByUUID(entry).orElseGet(entry::toString), w - (h + 7), "...")), x + h + 3f, y + (h - 6.5f) / 2f, z + OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i1);
            //   renderSmartTextSprite(poseStack1, multiBufferSource1, getAuthorityType(blockEntity, entry).getText(), x + h + 3f, y + 11f, z + OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i1);
        });
    }

    private void sendChangeAuthorityPacket(UUID playerID, AuthorityInfo.AuthorityType type) {
        if (playerID == null) return;
        var ps = getSelectedMusicPlayList();
        if (ps != null)
            NetworkManager.sendToServer(IMPPackets.MUSIC_PLAYLIST_CHANGE_AUTHORITY, new IMPPackets.MusicPlayListChangeAuthority(ps.getUuid(), playerID, type, BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity())).toFBB());
    }

    protected AuthorityInfo.AuthorityType getAuthorityType(UUID playerId) {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getAuthorityType(musicManagerBlockEntity, playerId);
        return AuthorityInfo.AuthorityType.NONE;
    }

    protected AuthorityInfo.AuthorityType getAuthorityType(MusicManagerBlockEntity musicManagerBlockEntity, UUID playerId) {
        var pl = getSelectedMusicPlayList(musicManagerBlockEntity);
        if (pl != null)
            return pl.getAuthority().getAuthorityType(playerId);
        return AuthorityInfo.AuthorityType.NONE;
    }

    private boolean canEdit(UUID playerID) {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return canEdit(musicManagerBlockEntity, playerID);
        return false;
    }

    private boolean canSetAdmin() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return canSetAdmin(musicManagerBlockEntity);
        return false;
    }

    private boolean canSetAdmin(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pl = getSelectedMusicPlayList(musicManagerBlockEntity);
        if (pl != null) {
            return pl.getAuthority().getAuthorityType(mc.player.getGameProfile().getId()).isMoreOwner();
        }
        return false;
    }

    private boolean canEdit(MusicManagerBlockEntity musicManagerBlockEntity, UUID playerID) {
        if (playerID == null) return false;
        var pl = getSelectedMusicPlayList(musicManagerBlockEntity);
        if (pl != null) {
            var myAuth = pl.getAuthority().getAuthorityType(mc.player.getGameProfile().getId());
            var tarAuth = pl.getAuthority().getAuthorityType(playerID);
            return myAuth.canChangeAuth(tarAuth);
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (cashPlayLists != getSyncManager().getMyPlayList()) {
            cashPlayLists = getSyncManager().getMyPlayList();
            updateMembers();
        }

        this.readOnlyRadio.visible = canEdit(getSelectedPlayer());
        this.memberRadio.visible = canEdit(getSelectedPlayer());
        this.banRadio.visible = canEdit(getSelectedPlayer());
        this.adminRadio.visible = canEdit(getSelectedPlayer()) && canSetAdmin();

        this.expulsionButton.visible = canEdit(getSelectedPlayer());

        this.readOnlyRadio.setSelected(getAuthorityType(getSelectedPlayer()) == AuthorityInfo.AuthorityType.READ_ONLY);
        this.memberRadio.setSelected(getAuthorityType(getSelectedPlayer()) == AuthorityInfo.AuthorityType.MEMBER);
        this.banRadio.setSelected(getAuthorityType(getSelectedPlayer()) == AuthorityInfo.AuthorityType.BAN);
        this.adminRadio.setSelected(getAuthorityType(getSelectedPlayer()) == AuthorityInfo.AuthorityType.ADMIN);
    }

    @Override
    protected @Nullable MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.DETAIL_PLAY_LIST;
    }

    private void updateMembers() {
        members.clear();
        var pl = getSelectedMusicPlayList();
        if (pl != null)
            members.addAll(pl.getAuthority().getPlayersAuthority().entrySet().stream().filter(n -> n.getValue() != AuthorityInfo.AuthorityType.NONE && n.getValue() != AuthorityInfo.AuthorityType.INVITATION).map(Map.Entry::getKey).toList());
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
        return musicManagerBlockEntity.getMySelectedPlayList();
    }

    protected UUID getSelectedPlayer() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getSelectedPlayer(musicManagerBlockEntity);
        return null;
    }

    protected UUID getSelectedPlayer(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getMySelectedPlayer();
    }

    protected void setSelectedPlayer(UUID playerId) {
        getScreen().insSelectedPlayer(playerId);
    }
}
