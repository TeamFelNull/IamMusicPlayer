package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.MemberPlayersFixedListWidget;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.SmartRadioButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.AuthorityInfo;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import dev.felnull.otyacraftengine.networking.existence.BlockEntityExistence;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class DetailPlayListMMMonitor extends PlayListBaseMMMonitor {
    private static final ResourceLocation DETAIL_PLAY_LIST_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/detail_play_list.png");
    private static final Component MEMBER_TEXT = Component.translatable("imp.text.member");
    private static final Component EDIT_TEXT = Component.translatable("imp.button.edit");
    private static final Component AUTHORITY_TEXT = Component.translatable("imp.button.authority");
    private static final Component DELETE_TEXT = Component.translatable("imp.button.delete").withStyle(ChatFormatting.DARK_RED);
    private static final Component EXIT_TEXT = Component.translatable("imp.button.exit").withStyle(ChatFormatting.RED);
    private final List<UUID> memberPlayers = new ArrayList<>();
    private SmartButton editButton;
    private SmartButton deleteButton;
    private SmartButton exitButton;
    private SmartRadioButton publishingRadio;
    private SmartRadioButton initAuthRadio;
    private String cashName;
    private MemberPlayersFixedListWidget memberPlayersFixedButtonsList;

    public DetailPlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
        locked();
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

        this.editButton = this.addRenderWidget(new SmartButton(getStartX() + width - 95 - 87, getStartY() + 180, 87, 15, EDIT_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.EDIT_PLAY_LIST)));
        this.editButton.visible = canEdit();

        this.deleteButton = this.addRenderWidget(new SmartButton(getStartX() + width - 5 - 87, getStartY() + 180, 87, 15, DELETE_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.DELETE_PLAY_LIST)));
        this.deleteButton.visible = canDelete();

        this.exitButton = this.addRenderWidget(new SmartButton(getStartX() + width - 5 - 87, getStartY() + 180, 87, 15, EXIT_TEXT, n -> {
            if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
                NetworkManager.sendToServer(IMPPackets.MUSIC_OR_PLAYLIST_DELETE, new IMPPackets.MusicOrPlayListDeleteMessage(getSelectedPlayList(musicManagerBlock), UUID.randomUUID(), BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity()), false).toFBB());
        }));
        this.exitButton.visible = !canDelete();

        this.publishingRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 5, getStartY() + 140, getPublishingText(), null, ImmutableSet::of));
        this.publishingRadio.setSelected(true);
        this.publishingRadio.active = false;

        this.initAuthRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 189, getStartY() + 140, getInitAuthText(), null, ImmutableSet::of));
        this.initAuthRadio.setSelected(true);
        this.initAuthRadio.active = false;

        this.memberPlayersFixedButtonsList = addRenderWidget(new MemberPlayersFixedListWidget(getStartX() + 189, getStartY() + 23, 175, 100, Component.translatable("imp.fixedList.memberPlayers"), 5, memberPlayers, (widget, item) -> {
        }, this.memberPlayersFixedButtonsList, this::getSelectedMusicPlayList));

        addRenderWidget(new SmartButton(getStartX() + width - 95 - 87 * 2 - 3, getStartY() + 180, 87, 15, AUTHORITY_TEXT, n -> {
            insMonitor(MusicManagerBlockEntity.MonitorType.AUTHORITY);
        }));

        this.cashName = getName();
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtils.drawTexture(DETAIL_PLAY_LIST_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtils.renderTextureSprite(DETAIL_PLAY_LIST_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtils.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);

        renderSmartRadioButtonSprite(poseStack, multiBufferSource, 5, 140, OERenderUtils.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, getPublishingText(blockEntity), true);
        renderSmartRadioButtonSprite(poseStack, multiBufferSource, 189, 140, OERenderUtils.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, getInitAuthText(blockEntity), true);
        var pl = getSelectedMusicPlayList(blockEntity);
        List<UUID> pls = null;
        if (pl != null) {
            pls = new ArrayList<>(pl.getAuthority().getPlayersAuthority().keySet());
            pls.sort(Comparator.comparingInt(o -> pl.getAuthority().getAuthorityType((UUID) o).getLevel()).reversed());
        }

        renderFixedListSprite(poseStack, multiBufferSource, 189, 23, OERenderUtils.MIN_BREADTH * 3, 175, 100, i, j, onPxW, onPxH, monitorHeight, pls, 5, (poseStack1, multiBufferSource1, x, y, z, w, h, i1, j1, entry) -> {
            renderSmartButtonBoxSprite(poseStack1, multiBufferSource1, x, y, z + OERenderUtils.MIN_BREADTH, w, h, i1, j1, onPxW, onPxH, monitorHeight, false);
            renderPlayerFaceSprite(poseStack1, multiBufferSource1, entry, x + 1, y + 1, z + OERenderUtils.MIN_BREADTH * 3, h - 2, i1, j1, onPxW, onPxH, monitorHeight);
            renderSmartTextSprite(poseStack1, multiBufferSource, Component.literal(OEClientUtils.getWidthOmitText(OEClientUtils.getPlayerNameByUUID(entry).orElseGet(entry::toString), w - (h + 7), "...")), x + h + 3f, y + 3f, z + OERenderUtils.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);
            if (pl != null)
                renderSmartTextSprite(poseStack1, multiBufferSource, pl.getAuthority().getAuthorityType(entry).getText(), x + h + 3f, y + 11f, z + OERenderUtils.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);
        });

        if (canEdit(blockEntity))
            renderSmartButtonSprite(poseStack, multiBufferSource, width - 95 - 87, 180, OERenderUtils.MIN_BREADTH * 2, 87, 15, i, j, onPxW, onPxH, monitorHeight, EDIT_TEXT, true);

        if (canDelete(blockEntity))
            renderSmartButtonSprite(poseStack, multiBufferSource, width - 5 - 87, 180, OERenderUtils.MIN_BREADTH * 2, 87, 15, i, j, onPxW, onPxH, monitorHeight, DELETE_TEXT, true);
        else
            renderSmartButtonSprite(poseStack, multiBufferSource, width - 5 - 87, 180, OERenderUtils.MIN_BREADTH * 2, 87, 15, i, j, onPxW, onPxH, monitorHeight, EXIT_TEXT, true);

        renderSmartButtonSprite(poseStack, multiBufferSource, width - 95 - 87 * 2 - 3, 180, OERenderUtils.MIN_BREADTH * 2, 87, 15, i, j, onPxW, onPxH, monitorHeight, AUTHORITY_TEXT, true);
    }

    @Override
    public void tick() {
        super.tick();
        this.editButton.visible = canEdit();
        this.deleteButton.visible = canDelete();
        this.exitButton.visible = !canDelete();

        this.publishingRadio.setMessage(getPublishingText());
        this.initAuthRadio.setMessage(getInitAuthText());

        memberPlayers.clear();
        var pl = getSelectedMusicPlayList();
        if (pl != null) {
            memberPlayers.addAll(pl.getAuthority().getPlayersAuthority().keySet());
            memberPlayers.sort(Comparator.comparingInt(o -> pl.getAuthority().getAuthorityType((UUID) o).getLevel()).reversed());
        }

        if (!this.cashName.equals(getName())) {
            this.cashName = getName();
            this.nameEditBox.setValue(this.cashName);
        }
    }

    @Override
    protected Component getPlayerListName() {
        return MEMBER_TEXT;
    }

    @Override
    public boolean done(ImageInfo imageInfo, String name) {
        return true;
    }

    @Override
    protected DoneType getDoneType() {
        return null;
    }

    @NotNull
    private Component getInitAuthText(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pl = getSelectedMusicPlayList(musicManagerBlockEntity);
        if (pl != null) {
            if (pl.getAuthority().getInitialAuthority() == AuthorityInfo.AuthorityType.READ_ONLY)
                return READONLY_RDO_TEXT;
            else if (pl.getAuthority().getInitialAuthority() == AuthorityInfo.AuthorityType.MEMBER)
                return MEMBER_RDO_TEXT;
        }
        return Component.literal("");
    }

    @NotNull
    private Component getInitAuthText() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getInitAuthText(musicManagerBlockEntity);
        return Component.literal("");
    }

    @NotNull
    private Component getPublishingText(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pl = getSelectedMusicPlayList(musicManagerBlockEntity);
        if (pl != null) {
            if (pl.getAuthority().isPublic())
                return PUBLIC_RDO_TEXT;
            else
                return PRIVATE_RDO_TEXT;
        }
        return Component.literal("");
    }

    @NotNull
    private Component getPublishingText() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getPublishingText(musicManagerBlockEntity);
        return Component.literal("");
    }

    private boolean canEdit(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pl = getSelectedMusicPlayList(musicManagerBlockEntity);
        if (pl != null)
            return pl.getAuthority().getAuthorityType(mc.player.getGameProfile().getId()).canEdit();
        return false;
    }

    private boolean canDelete(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pl = getSelectedMusicPlayList(musicManagerBlockEntity);
        if (pl != null)
            return pl.getAuthority().getAuthorityType(mc.player.getGameProfile().getId()).canDelete();
        return false;
    }

    private boolean canEdit() {
        var pl = getSelectedMusicPlayList();
        if (pl != null)
            return pl.getAuthority().getAuthorityType(mc.player.getGameProfile().getId()).canEdit();
        return false;
    }

    private boolean canDelete() {
        var pl = getSelectedMusicPlayList();
        if (pl != null)
            return pl.getAuthority().getAuthorityType(mc.player.getGameProfile().getId()).canDelete();
        return false;
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.PLAY_LIST;
    }

    @Override
    protected @NotNull ImageInfo getImage() {
        var pl = getSelectedMusicPlayList();
        if (pl != null)
            return pl.getImage();
        return ImageInfo.EMPTY;
    }

    @Override
    protected @NotNull ImageInfo getImage(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pl = getSelectedMusicPlayList(musicManagerBlockEntity);
        if (pl != null)
            return pl.getImage();
        return ImageInfo.EMPTY;
    }

    @Override
    protected @NotNull String getName() {
        var pl = getSelectedMusicPlayList();
        if (pl != null)
            return pl.getName();
        return "";
    }

    @Override
    protected @NotNull String getName(@NotNull MusicManagerBlockEntity musicManagerBlockEntity) {
        var pl = getSelectedMusicPlayList(musicManagerBlockEntity);
        if (pl != null)
            return pl.getName();
        return "";
    }

    protected UUID getSelectedPlayList(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getSelectedPlayList(mc.player);
    }

    protected MusicPlayList getSelectedMusicPlayList(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pls = getSyncManager().getMyPlayList();
        if (pls == null)
            return null;
        return getSyncManager().getMyPlayList().stream().filter(n -> n.getUuid().equals(getSelectedPlayList(musicManagerBlockEntity))).findFirst().orElse(null);
    }

    protected MusicPlayList getSelectedMusicPlayList() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getSelectedMusicPlayList(musicManagerBlockEntity);
        return null;
    }

    protected UUID getSelectedPlayList() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getSelectedPlayList(musicManagerBlockEntity);
        return null;
    }
}
