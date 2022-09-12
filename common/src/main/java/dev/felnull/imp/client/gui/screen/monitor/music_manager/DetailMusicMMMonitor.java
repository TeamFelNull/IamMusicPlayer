package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DetailMusicMMMonitor extends MusicBaseMMMonitor {
    private static final Component EDIT_TEXT = Component.translatable("imp.button.edit");
    private static final Component DELETE_TEXT = Component.translatable("imp.button.delete").withStyle(ChatFormatting.DARK_RED);
    private SmartButton editButton;
    private SmartButton deleteButton;
    private String cashName;

    public DetailMusicMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
        locked();
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        this.editButton = this.addRenderWidget(new SmartButton(getStartX() + width - 95 - 87, getStartY() + 180, 87, 15, EDIT_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.EDIT_MUSIC)));
        this.editButton.visible = canEdit();

        this.deleteButton = this.addRenderWidget(new SmartButton(getStartX() + width - 5 - 87, getStartY() + 180, 87, 15, DELETE_TEXT, n -> insMonitor(MusicManagerBlockEntity.MonitorType.DELETE_MUSIC)));
        this.deleteButton.visible = canDelete();

        this.cashName = getName();
    }

    @Override
    public void renderAppearance(MusicManagerBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight) {
        super.renderAppearance(blockEntity, poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        if (canEdit(blockEntity))
            renderSmartButtonSprite(poseStack, multiBufferSource, width - 95 - 87, 180, OERenderUtils.MIN_BREADTH * 2, 87, 15, i, j, onPxW, onPxH, monitorHeight, EDIT_TEXT, true);

        if (canDelete(blockEntity))
            renderSmartButtonSprite(poseStack, multiBufferSource, width - 5 - 87, 180, OERenderUtils.MIN_BREADTH * 2, 87, 15, i, j, onPxW, onPxH, monitorHeight, DELETE_TEXT, true);
    }

    @Override
    public boolean done(ImageInfo imageInfo, String name) {
        setSelectedMusic(null);
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.editButton.visible = canEdit();
        this.deleteButton.visible = canDelete();

        if (!this.cashName.equals(getName())) {
            this.cashName = getName();
            this.nameEditBox.setValue(this.cashName);
        }
    }

    @Override
    protected @Nullable DoneType getDoneType() {
        return null;
    }

    private boolean canEdit(MusicManagerBlockEntity musicManagerBlockEntity) {
        var m = getSelectedMusic(musicManagerBlockEntity);
        return m != null && m.getOwner().equals(mc.player.getGameProfile().getId());
    }

    private boolean canDelete(MusicManagerBlockEntity musicManagerBlockEntity) {
        var pl = getSelectedMusicPlayList(musicManagerBlockEntity);
        var m = getSelectedMusic(musicManagerBlockEntity);
        return (pl != null && pl.getAuthority().getAuthorityType(mc.player.getGameProfile().getId()).canMusicDelete()) || (m != null && m.getOwner().equals(mc.player.getGameProfile().getId()));
    }


    private boolean canEdit() {
        var m = getSelectedMusic();
        return m != null && m.getOwner().equals(mc.player.getGameProfile().getId());
    }

    private boolean canDelete() {
        var pl = getSelectedMusicPlayList();
        var m = getSelectedMusic();
        return (pl != null && pl.getAuthority().getAuthorityType(mc.player.getGameProfile().getId()).canMusicDelete()) || (m != null && m.getOwner().equals(mc.player.getGameProfile().getId()));
    }

    @Override
    protected @NotNull String getMusicAuthor(@NotNull MusicManagerBlockEntity musicManagerBlockEntity) {
        var m = getSelectedMusic(musicManagerBlockEntity);
        if (m != null)
            return m.getAuthor();
        return "";
    }

    @Override
    protected @Nullable MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.PLAY_LIST;
    }

    @Override
    protected @NotNull String getMusicLoaderType(@NotNull MusicManagerBlockEntity musicManagerBlockEntity) {
        var m = getSelectedMusic(musicManagerBlockEntity);
        if (m != null)
            return m.getSource().getLoaderType();
        return "";
    }

    @Override
    protected @NotNull ImageInfo getImage(MusicManagerBlockEntity musicManagerBlockEntity) {
        var m = getSelectedMusic(musicManagerBlockEntity);
        if (m != null)
            return m.getImage();
        return ImageInfo.EMPTY;
    }

    @Override
    protected @NotNull String getName(@NotNull MusicManagerBlockEntity musicManagerBlockEntity) {
        var m = getSelectedMusic(musicManagerBlockEntity);
        if (m != null)
            return m.getName();
        return "";
    }

    @Override
    protected @NotNull MusicSource getMusicSource(@NotNull MusicManagerBlockEntity blockEntity) {
        var m = getSelectedMusic(blockEntity);
        if (m != null)
            return m.getSource();
        return MusicSource.EMPTY;
    }

    private void setSelectedMusic(@Nullable UUID uuid) {
        getScreen().insSelectedMusic(uuid);
    }

    @Nullable
    private Music getSelectedMusic() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getSelectedMusic(musicManagerBlockEntity);
        return null;
    }

    @Nullable
    private Music getSelectedMusic(MusicManagerBlockEntity musicManagerBlockEntity) {
        var id = getSelectedMusicRaw(musicManagerBlockEntity);
        var pl = getSelectedPlayList(musicManagerBlockEntity);
        if (id != null && pl != null) {
            var sms = getSyncManager().getMusics(pl);
            if (sms != null)
                return sms.stream().filter(n -> id.equals(n.getUuid())).findFirst().orElse(null);
        }
        return null;
    }

    @Nullable
    private UUID getSelectedMusicRaw() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getSelectedMusicRaw(musicManagerBlockEntity);
        return null;
    }

    @Nullable
    private UUID getSelectedMusicRaw(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getSelectedMusic(mc.player);
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
        return getSyncManager().getMyPlayList().stream().filter(n -> n.getUuid().equals(getSelectedPlayList(musicManagerBlockEntity))).findFirst().orElse(null);
    }

    @Nullable
    protected UUID getSelectedPlayList() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getSelectedPlayList(musicManagerBlockEntity);
        return null;
    }

    @Nullable
    private UUID getSelectedPlayList(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getSelectedPlayList(mc.player);
    }
}
