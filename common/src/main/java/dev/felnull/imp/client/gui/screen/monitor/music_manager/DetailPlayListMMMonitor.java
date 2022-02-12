package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.PlayersFixedButtonsList;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.components.SmartRadioButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.AuthorityInfo;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.gui.components.RadioButton;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class DetailPlayListMMMonitor extends PlayListBaseMMMonitor {
    private static final ResourceLocation DETAIL_PLAY_LIST_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/monitor/detail_play_list.png");
    private static final Component MEMBER_TEXT = new TranslatableComponent("imp.text.member");
    private static final Component EDIT_TEXT = new TranslatableComponent("imp.button.edit");
    private static final Component DELETE_TEXT = new TranslatableComponent("imp.button.delete").withStyle(ChatFormatting.DARK_RED);
    private SmartButton editButton;
    private SmartButton deleteButton;
    private RadioButton publishingRadio;
    private RadioButton initAuthRadio;

    public DetailPlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
        locked();
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);

        this.editButton = this.addRenderWidget(new SmartButton(getStartX() + width - 95 - 87, getStartY() + 180, 87, 15, EDIT_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.EDIT_PLAY_LIST)));
        this.editButton.visible = canEdit();

        this.deleteButton = this.addRenderWidget(new SmartButton(getStartX() + width - 5 - 87, getStartY() + 180, 87, 15, DELETE_TEXT, n -> {

        }));
        this.deleteButton.visible = canDelete();

        this.publishingRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 5, getStartY() + 140, 20, 20, getPublishingText(), true, true, () -> new RadioButton[]{}, n -> {
        }));
        this.publishingRadio.active = false;

        this.initAuthRadio = this.addRenderWidget(new SmartRadioButton(getStartX() + 189, getStartY() + 140, 20, 20, getInitAuthText(), true, true, () -> new RadioButton[]{}, n -> {
        }));
        this.initAuthRadio.active = false;

        addRenderWidget(new PlayersFixedButtonsList(getStartX() + 189, getStartY() + 23, 175, 101, 5, new TranslatableComponent("imp.fixedList.memberPlayers"), new ArrayList<>(), (fixedButtonsList, uuid, i, i1) -> {
        }));
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        OERenderUtil.drawTexture(DETAIL_PLAY_LIST_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        OERenderUtil.renderTextureSprite(DETAIL_PLAY_LIST_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);

        renderSmartRadioButtonSprite(poseStack, multiBufferSource, 5, 140, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, getPublishingText(blockEntity), true);
        renderSmartRadioButtonSprite(poseStack, multiBufferSource, 189, 140, OERenderUtil.MIN_BREADTH * 4, 20, 20, i, j, onPxW, onPxH, monitorHeight, getInitAuthText(blockEntity), true);

    }

    @Override
    public void tick() {
        super.tick();
        this.editButton.visible = canEdit();
        this.deleteButton.visible = canDelete();

        this.publishingRadio.setMessage(getPublishingText());
        this.initAuthRadio.setMessage(getInitAuthText());
    }

    @Override
    protected Component getPlayerListName() {
        return MEMBER_TEXT;
    }

    @Override
    public void done(ImageInfo imageInfo, String name) {

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
        return TextComponent.EMPTY;
    }

    @NotNull
    private Component getInitAuthText() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getInitAuthText(musicManagerBlockEntity);
        return TextComponent.EMPTY;
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
        return TextComponent.EMPTY;
    }

    @NotNull
    private Component getPublishingText() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getPublishingText(musicManagerBlockEntity);
        return TextComponent.EMPTY;
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
        return musicManagerBlockEntity.getMySelectedPlayList();
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
