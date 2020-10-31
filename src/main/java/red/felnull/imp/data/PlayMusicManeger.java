package red.felnull.imp.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.client.data.MusicSourceClientReferencesType;
import red.felnull.imp.client.data.MusicUploader;
import red.felnull.imp.handler.MusicReceiveHandler;
import red.felnull.imp.musicplayer.PlayImage;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.imp.musicplayer.PlayLocation;
import red.felnull.imp.musicplayer.PlayMusic;
import red.felnull.imp.packet.PacketHandler;
import red.felnull.imp.packet.PlayMusicCreateRequestMessage;
import red.felnull.otyacraftengine.api.DataSendReceiverManager;
import red.felnull.otyacraftengine.data.SendReceiveLogger;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;
import red.felnull.otyacraftengine.util.IKSGStringUtil;

import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayMusicManeger {
    private static PlayMusicManeger INSTANCE;

    public static void init() {
        INSTANCE = new PlayMusicManeger();
    }

    public static PlayMusicManeger instance() {
        return INSTANCE;
    }

    public static final Map<String, PlayMusicEntry> waitingDownloads = new HashMap<>();

    @OnlyIn(Dist.CLIENT)
    public void createPlayMusicRequest(String name, PlayList playList, PlayImage image, byte[] imageData, PlayLocation location, MusicSourceClientReferencesType uploadtype, String pathOrURL, String artist, String album, String year, String genre) {

        if (playList == PlayList.ALL)
            return;

        PacketHandler.INSTANCE.sendToServer(new PlayMusicCreateRequestMessage(name, playList.getUUID(), image, location, artist, album, year, genre));

        if (location.getLocationType() == PlayLocation.LocationType.WORLD_FILE) {
            if (uploadtype == MusicSourceClientReferencesType.LOCAL_FILE) {
                MusicUploader.instance().startUpload(name, Paths.get(pathOrURL), location.getIdOrURL(), image, imageData);
            } else if (uploadtype == MusicSourceClientReferencesType.URL) {
                try {
                    MusicUploader.instance().startUpload(name, new URL(pathOrURL), location.getIdOrURL(), image, imageData);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        if (image.getImageType() == PlayImage.ImageType.IMGAE)
            DataSendReceiverManager.instance().sendToServer(IMPWorldData.IMAGE, image.getName(), imageData);


    }

    public void createPlayMusic(ServerPlayerEntity player, PlayList playList, String name, PlayImage image, PlayLocation musicLocation, String artist, String album, String year, String genre) {
        String plUUID = UUID.randomUUID().toString();
        PlayMusic playMusic = new PlayMusic(plUUID, name, image, IKSGPlayerUtil.getUserName(player), IKSGPlayerUtil.getUUID(player), IKSGStringUtil.getTimeStamp(), musicLocation, artist, album, year, genre, 0);
        if (musicLocation.getLocationType() == PlayLocation.LocationType.URL || (musicLocation.getLocationType() == PlayLocation.LocationType.WORLD_FILE && MusicReceiveHandler.downloadble.containsKey(musicLocation.getIdOrURL()) && MusicReceiveHandler.downloadble.get(musicLocation.getIdOrURL()) == SendReceiveLogger.SRResult.SUCCESS)) {
            PlayMusic.addPlayMusic(playMusic);
            playMusic.addPlayMusicToPlayList(playList);
            if (musicLocation.getLocationType() == PlayLocation.LocationType.WORLD_FILE)
                MusicReceiveHandler.downloadble.remove(musicLocation.getIdOrURL());
        } else if (musicLocation.getLocationType() == PlayLocation.LocationType.WORLD_FILE) {
            waitingDownloads.put(musicLocation.getIdOrURL(), new PlayMusicEntry(playMusic, playList));
        }
    }

    public CompoundNBT getAllPlayMusicNBT(ServerPlayerEntity playerEntity, PlayList list) {
        CompoundNBT tag = new CompoundNBT();
        if (list.equals(PlayList.ALL)) {
            List<PlayList> plist = PlayList.getJoinedPlayLists(playerEntity);
            plist.forEach(n -> {
                List<PlayMusic> jpl = PlayMusic.getIncludedPlayMusics(n);
                jpl.forEach(n2 -> tag.put(n2.getUUID(), n2.write(new CompoundNBT())));
            });
            return tag;
        } else {
            List<PlayMusic> jpl = PlayMusic.getIncludedPlayMusics(list);
            jpl.forEach(n -> tag.put(n.getUUID(), n.write(new CompoundNBT())));
            return tag;
        }
    }

    public class PlayMusicEntry {
        private final PlayMusic music;
        private final PlayList list;

        public PlayMusicEntry(PlayMusic music, PlayList list) {
            this.music = music;
            this.list = list;
        }

        public PlayMusic getMusic() {
            return music;
        }

        public PlayList getList() {
            return list;
        }
    }

}

