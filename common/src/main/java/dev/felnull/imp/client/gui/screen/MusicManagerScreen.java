package dev.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.PowerButton;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.music.MusicLoadThread;
import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.inventory.MusicManagerMenu;
import dev.felnull.imp.music.MusicPlaybackInfo;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.server.music.ringer.MusicRingManager;
import dev.felnull.otyacraftengine.util.OENbtUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MusicManagerScreen extends IMPBaseContainerScreen<MusicManagerMenu> {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/music_manager_base.png");
    private final Map<MusicManagerBlockEntity.MonitorType, MusicManagerMonitor> monitors = new HashMap<>();
    private final UUID musicPlayerId = UUID.randomUUID();
    public boolean lastSearch;
    protected MusicManagerMonitor monitor;
    public byte[] musicFileImage;

    public MusicManagerScreen(MusicManagerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 386;
        this.imageHeight = 227;
        this.bgTextureWidth = 512;
        this.bgTextureHeight = 512;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new PowerButton(this, leftPos + 368, topPos + 4, 12, 12, 386, 0, BG_TEXTURE, bgTextureWidth, bgTextureHeight));
        changeScreenMonitor(getBEMonitorType());
        insMonitor(getBEMonitorType());
    }

    public void insMonitor(MusicManagerBlockEntity.MonitorType type) {
        if (monitor != null && monitor.getType() != type)
            stopMusic();

        var tag = new CompoundTag();
        tag.putString("type", type.getName());
        instruction("set_monitor", 0, tag);
    }

    public void insAddPlayList(UUID playListId) {
        var tag = new CompoundTag();
        tag.putUUID("playlist", playListId);
        instruction("add_playlist", 0, tag);
    }

    private void changeScreenMonitor(MusicManagerBlockEntity.MonitorType type) {
        if (monitor != null) {
            if (monitor.getType() != MusicManagerBlockEntity.MonitorType.UPLOAD_MUSIC || type != MusicManagerBlockEntity.MonitorType.ADD_MUSIC)
                musicFileImage = null;
            monitor.renderables.forEach(n -> {
                if (n instanceof GuiEventListener guiEventListener)
                    removeWidget(guiEventListener);
            });
            monitor.depose();
        }

        if (!monitors.containsKey(type))
            monitors.put(type, MusicManagerMonitor.createdMusicMonitor(type, this));

        monitor = monitors.get(type);
        monitor.init(leftPos, topPos);
        monitor.renderables.forEach(n -> {
            if (n instanceof AbstractWidget widget)
                addRenderableWidget(widget);
        });
    }

    private MusicManagerBlockEntity.MonitorType getBEMonitorType() {
        if (getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return musicManagerBlockEntity.getMyMonitor(mc.player);
        return MusicManagerBlockEntity.MonitorType.OFF;
    }

    public void insImportPlayListMusicCount(int count) {
        var tag = new CompoundTag();
        tag.putInt("count", count);
        instruction("set_import_playlist_music_count", 0, tag);
    }

    public void insImportPlayListAuthor(String author) {
        var tag = new CompoundTag();
        tag.putString("author", author);
        instruction("set_import_playlist_author", 0, tag);
    }

    public void insImportPlayListName(String name) {
        var tag = new CompoundTag();
        tag.putString("name", name);
        instruction("set_import_playlist_name", 0, tag);
    }

    public void insImportIdentifier(String identifier) {
        var tag = new CompoundTag();
        tag.putString("id", identifier);
        instruction("set_import_identifier", 0, tag);
    }

    public void insImageURL(String url) {
        var tag = new CompoundTag();
        tag.putString("url", url);
        instruction("set_image_url", 0, tag);
    }

    public void insImage(ImageInfo image) {
        var tag = new CompoundTag();
        OENbtUtil.writeSerializable(tag, "image", image);
        instruction("set_image", 0, tag);
    }

    public void insCreateName(String name) {
        var tag = new CompoundTag();
        tag.putString("name", name);
        instruction("set_create_name", 0, tag);
    }

    public void insPublishing(String publishing) {
        var tag = new CompoundTag();
        tag.putString("publishing", publishing);
        instruction("set_publishing", 0, tag);
    }

    public void insInitialAuthority(String initialAuthority) {
        var tag = new CompoundTag();
        tag.putString("initial_authority", initialAuthority);
        instruction("set_initial_authority", 0, tag);
    }

    public void insInvitePlayerName(String name) {
        var tag = new CompoundTag();
        tag.putString("name", name);
        instruction("set_invite_player_name", 0, tag);
    }

    public void insInvitePlayers(List<UUID> players) {
        var tag = new CompoundTag();
        OENbtUtil.writeUUIDList(tag, "players", players);
        instruction("set_invite_players", 0, tag);
    }

    public void insSelectedPlayList(UUID selectedPlayList) {
        var tag = new CompoundTag();
        if (selectedPlayList != null)
            tag.putUUID("playlist", selectedPlayList);
        instruction("set_selected_playlist", 0, tag);
    }

    public void insSelectedMusic(@Nullable UUID selectedMusic) {
        var tag = new CompoundTag();
        if (selectedMusic != null)
            tag.putUUID("music", selectedMusic);
        instruction("set_selected_music", 0, tag);
    }

    public void insMusicLoaderType(String name) {
        var tag = new CompoundTag();
        tag.putString("name", name);
        instruction("set_music_loader_type", 0, tag);
    }

    public void insMusicSourceName(String name) {
        var tag = new CompoundTag();
        tag.putString("name", name);
        instruction("set_music_source_name", 0, tag);
    }

    public void insMusicSource(MusicSource source) {
        if (source == null)
            source = MusicSource.EMPTY;
        var tag = new CompoundTag();
        OENbtUtil.writeSerializable(tag, "MusicSource", source);
        instruction("set_music_source", 0, tag);
    }

    public void insMusicSearchName(String name) {
        var tag = new CompoundTag();
        tag.putString("name", name);
        instruction("set_music_search_name", 0, tag);
    }

    public void insMusicAuthor(String author) {
        var tag = new CompoundTag();
        tag.putString("author", author);
        instruction("set_music_author", 0, tag);
    }

    @Override
    public void onInstructionReturn(String name, int num, CompoundTag data) {
        super.onInstructionReturn(name, num, data);
        if ("add_playlist".equals(name)) {
            if (data.contains("playlist")) {
                insSelectedPlayList(data.getUUID("playlist"));
            }
        } else if ("set_selected_playlist".equals(name)) {
            if (data.contains("playlist")) {
                monitor.onUpdateSelectedPlayList(data.getUUID("playlist"));
            } else {
                monitor.onUpdateSelectedPlayList(null);
            }
        }
    }

    @Override
    protected ResourceLocation getBackGrandTexture() {
        return BG_TEXTURE;
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int i, int j) {
        this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {
        super.renderBg(poseStack, f, i, j);
        if (monitor != null)
            monitor.render(poseStack, f, i, j);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (monitor != null) {
            if (getBEMonitorType() != monitor.getType())
                changeScreenMonitor(getBEMonitorType());
            monitor.tick();
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        if (monitor != null)
            monitor.depose();

        stopMusic();
    }

    @Override
    public void onFilesDrop(List<Path> list) {
        if (monitor != null)
            monitor.onFilesDrop(list);
    }

    public void playMusic(MusicSource source, long postion) {
        stopMusic();
        getMusicEngine().loadAddMusicPlayer(musicPlayerId, new MusicPlaybackInfo(MusicRingManager.PLAYER_TRACKER, MusicRingManager.createPlayerTracker(mc.player), 1, 10), source, postion, (result, time, player, retry) -> {
            getMusicEngine().playMusicPlayer(musicPlayerId, 0);
        });
    }

    public void stopMusic() {
        getMusicEngine().stopMusicPlayer(musicPlayerId);
        getMusicEngine().stopLoadMusicPlayer(musicPlayerId);
    }

    public boolean isMusicPlaying() {
        return getMusicEngine().isPlaying(musicPlayerId);
    }

    public boolean isMusicLoading() {
        return getMusicEngine().isLoad(musicPlayerId);
    }

    private MusicEngine getMusicEngine() {
        return MusicEngine.getInstance();
    }

    public IMusicPlayer getMusicPlayer() {
        return getMusicEngine().getMusicPlayer(musicPlayerId);
    }

    public MusicLoadThread getLoadingMusic() {
        return getMusicEngine().getLoadingMusic(musicPlayerId);
    }
}
