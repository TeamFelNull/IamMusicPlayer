package red.felnull.imp.client.data;

import com.github.kiulian.downloader.YoutubeException;
import javazoom.jl.decoder.BitstreamException;
import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.player.URLNotStreamMusicPlayer;
import red.felnull.imp.client.music.player.WorldFileMusicPlayer;
import red.felnull.imp.client.music.player.YoutubeMusicPlayer;
import red.felnull.imp.exception.IMPFFmpegException;
import red.felnull.imp.exception.IMPWorldMusicException;
import red.felnull.imp.music.resource.PlayLocation;
import red.felnull.imp.music.resource.PlayMusic;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public enum MusicSourceClientReferencesType {
    LOCAL_FILE(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_source_references/local_file.png"), PlayLocation.LocationType.WORLD_FILE),
    URL(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_source_references/url.png"), PlayLocation.LocationType.URL),
    YOUTUBE(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_source_references/youtube.png"), PlayLocation.LocationType.YOUTUBE);
    private final ResourceLocation textuerLocation;
    private final PlayLocation.LocationType lctype;

    MusicSourceClientReferencesType(ResourceLocation textuerLocation, PlayLocation.LocationType locationType) {
        this.textuerLocation = textuerLocation;
        this.lctype = locationType;
    }

    public ResourceLocation getTextuerLocation() {
        return textuerLocation;
    }

    public PlayLocation.LocationType getLocationType() {
        return lctype;
    }

    public static MusicSourceClientReferencesType getTypeByLocationType(PlayLocation.LocationType type) {
        return Arrays.stream(values()).filter(n -> n.getLocationType() == type).findFirst().get();
    }

    public static IMusicPlayer getMusicPlayer(PlayMusic music) throws YoutubeException, EncoderException, BitstreamException, IOException, IMPWorldMusicException, InterruptedException, IMPFFmpegException {
        return MusicSourceClientReferencesType.getTypeByLocationType(music.getMusicLocation().getLocationType()).getMusicPlayer(music.getMusicLocation().getIdOrURL());
    }

    public IMusicPlayer getMusicPlayer(String src) throws YoutubeException, EncoderException, BitstreamException, IOException, IMPWorldMusicException, InterruptedException, IMPFFmpegException {
        switch (this) {
            case YOUTUBE:
                return new YoutubeMusicPlayer(src);
            case URL:
                return new URLNotStreamMusicPlayer(new URL(src));
            case LOCAL_FILE:
                return new WorldFileMusicPlayer(src);
            default:
                return null;
        }
    }
}



