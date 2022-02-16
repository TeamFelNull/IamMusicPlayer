package dev.felnull.imp.client.gui.screen.monitor.music_manager;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.client.music.loadertypes.IMPMusicLoaderTypes;
import dev.felnull.imp.client.music.loadertypes.YoutubeMusicLoaderType;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.otyacraftengine.networking.BlockEntityExistence;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImportYoutubePlayListMusicsMMMonitor extends ImportYoutubePlayListBaseMMMonitor {
    protected static final Component IMPORTING_TEXT = new TranslatableComponent("imp.text.importing");
    protected static final Component IMPORT_FAILURE_TEXT = new TranslatableComponent("imp.text.importFailure");
    private ImportMusicLoader importMusicLoader;
    private boolean failureImportPlayList;


    public ImportYoutubePlayListMusicsMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen) {
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
        } else if (getImportPlayListMusicCount() > 0) {
            ipTx = new TranslatableComponent("imp.text.importMusicCount", getImportPlayListMusicCount());
        }
        if (ipTx != null)
            drawSmartText(poseStack, ipTx, getStartX() + width - 95 + 7, getStartY() + 184);
    }

    @Override
    protected void onImport() {
        if (isImporting()) return;
        var ipl = getImportPlayList();
        if (getImportPlayListMusicCount() > 0 && !ipl.isEmpty())
            startImportMusicLoader(ipl);
    }

    @Override
    protected MusicManagerBlockEntity.MonitorType getParentType() {
        return MusicManagerBlockEntity.MonitorType.IMPORT_MUSICS_SELECT;
    }

    private boolean isImporting() {
        return importMusicLoader != null && importMusicLoader.isAlive();
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
                    if (getScreen().getBlockEntity() instanceof MusicManagerBlockEntity musicManagerBlock)
                        NetworkManager.sendToServer(IMPPackets.MULTIPLE_MUSIC_ADD, new IMPPackets.MultipleMusicAddMessage(musicManagerBlock.getMySelectedPlayList(), musics, BlockEntityExistence.getByBlockEntity(getScreen().getBlockEntity())).toFBB());
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
