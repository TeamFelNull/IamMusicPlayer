package red.felnull.imp.client.data;

import com.github.kiulian.downloader.YoutubeException;
import javazoom.jl.decoder.BitstreamException;
import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.music.IMusicPlayer;
import red.felnull.imp.client.music.URLNotStreamMusicPlayer;
import red.felnull.imp.client.music.YoutubeMusicPlayer;
import red.felnull.imp.musicplayer.PlayLocation;
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

    public IMusicPlayer getMusicPlayer(String src) throws YoutubeException, EncoderException, BitstreamException, IOException {
        switch (this) {
            case YOUTUBE:
                return new YoutubeMusicPlayer(src);
            case URL:
                return new URLNotStreamMusicPlayer(new URL(src));
            case LOCAL_FILE:
                return null;
            default:
                return null;
        }
    }
}



