package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.components.SmartButton;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.loadertypes.IMPMusicLoaderTypes;
import dev.felnull.imp.client.music.loadertypes.YoutubeMusicLoaderType;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import dev.felnull.otyacraftengine.networking.BlockEntityExistence;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreatePlayListMMMonitor extends SavedPlayListBaseMMMonitor {
    protected static final Component IMPORT_TEXT = new TranslatableComponent("imp.button.import");
    protected static final Component IMPORTING_TEXT = new TranslatableComponent("imp.text.importing");
    protected static final Component IMPORT_FAILURE_TEXT = new TranslatableComponent("imp.text.importFailure");
    private ImportMusicLoader importMusicLoader;
    private boolean failureImportPlayList;

    public CreatePlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
        super(type, screen);
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        Component ipTx = null;
        if (isImporting()) {
            ipTx = IMPORTING_TEXT;
        } else if (failureImportPlayList) {
            ipTx = IMPORT_FAILURE_TEXT;
        } else if (getImportMusicsCount() > 0) {
            ipTx = new TranslatableComponent("imp.text.importMusicCount", getImportMusicsCount());
        }
        if (ipTx != null)
            drawSmartText(poseStack, ipTx, getStartX() + width - 95 + 7, getStartY() + 184);
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

        if (getImportMusicsCount(blockEntity) > 0)
            renderSmartTextSprite(poseStack, multiBufferSource, new TranslatableComponent("imp.text.importMusicCount", getImportMusicsCount(blockEntity)), width - 95 + 7, 184, OERenderUtil.MIN_BREADTH * 3, onPxW, onPxH, monitorHeight, i);
    }

    @Override
    public boolean done(ImageInfo imageInfo, String name) {
        if (isImporting()) return false;
        var pubType = getPublishingType();
        var initAuthType = getInitialAuthorityType();
        var invitePlayers = getInvitePlayers();
        var ipl = getImportPlayList();
        if (getImportMusicsCount() > 0 && !ipl.isEmpty()) {
            startImportMusicLoader(ipl);
            return false;
        }
        sendAddPacket(imageInfo, name, pubType, initAuthType, invitePlayers, new ArrayList<>());
        return true;
    }

    private void sendAddPacket(ImageInfo imageInfo, String name, PublishingType pubType, InitialAuthorityType initAuthType, List<UUID> invitePlayers, List<Music> importMusics) {
        NetworkManager.sendToServer(IMPPackets.MUSIC_PLAYLIST_ADD, new IMPPackets.MusicPlayListMessage(name, imageInfo, pubType == PublishingType.PUBLIC, initAuthType == InitialAuthorityType.MEMBER, invitePlayers, BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity()), importMusics).toFBB());
    }

    @Override
    protected DoneType getDoneType() {
        return DoneType.CREATE;
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.ADD_PLAY_LIST;
    }

    private int getImportMusicsCount() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getImportMusicsCount(musicManagerBlockEntity);
        return 0;
    }

    private void startImportMusicLoader(String id) {
        stopImportMusicLoader();
        failureImportPlayList = false;
        importMusicLoader = new ImportMusicLoader(id);
        importMusicLoader.start();
    }

    private void stopImportMusicLoader() {
        if (importMusicLoader != null) {
            importMusicLoader.interrupt();
            importMusicLoader = null;
        }
    }

    private String getImportPlayList() {
        if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlockEntity)
            return getImportPlayList(musicManagerBlockEntity);
        return "";
    }

    private String getImportPlayList(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getMyImportIdentifier();
    }

    private int getImportMusicsCount(MusicManagerBlockEntity musicManagerBlockEntity) {
        return musicManagerBlockEntity.getMyImportPlayListMusicCount();
    }

    private boolean isImporting() {
        return importMusicLoader != null && importMusicLoader.isAlive();
    }

    private class ImportMusicLoader extends Thread {
        private final String id;

        private ImportMusicLoader(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                List<Music> musics = new ArrayList<>();
                var pl = LavaPlayerUtil.loadTracks(getYoutubeLoaderType().getAudioPlayerManager(), id);
                if (pl.getLeft() == null) throw new IllegalStateException("Not PlayList");
                for (AudioTrack track : pl.getRight()) {
                    if (!track.getInfo().isStream) {
                        var ret = getYoutubeLoaderType().createResult(track);
                        var en = new ImportYoutubePlayListMMMonitor.YoutubePlayListEntry(ret.name(), ret.author(), ret.source(), ret.imageInfo());
                        var music = new Music(UUID.randomUUID(), en.name(), en.artist(), en.source(), en.imageInfo(), mc.player.getGameProfile().getId(), System.currentTimeMillis());
                        musics.add(music);
                    }
                }
                mc.submit(() -> {
                    var imageInfo = getImage();
                    var name = CreatePlayListMMMonitor.this.getName();
                    var pubType = getPublishingType();
                    var initAuthType = getInitialAuthorityType();
                    var invitePlayers = getInvitePlayers();
                    sendAddPacket(imageInfo, name, pubType, initAuthType, invitePlayers, musics);
                    insMonitor(MusicManagerBlockEntity.MonitorType.PLAY_LIST);
                });
            } catch (Exception ex) {
                failureImportPlayList = true;
            }
        }

        private YoutubeMusicLoaderType getYoutubeLoaderType() {
            return ((YoutubeMusicLoaderType) IMPMusicLoaderTypes.getLoaderType(IMPMusicLoaderTypes.YOUTUBE));
        }
    }
}
