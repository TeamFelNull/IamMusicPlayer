package red.felnull.imp.client.data;

import com.github.kiulian.downloader.YoutubeException;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.player.LavaMusicLoader;
import red.felnull.imp.client.music.player.URLNotStreamMusicPlayer;
import red.felnull.imp.client.music.player.YoutubeMusicPlayer;
import red.felnull.imp.exception.IMPFFmpegException;
import red.felnull.imp.exception.IMPWorldMusicException;
import red.felnull.imp.music.resource.PlayLocation;
import red.felnull.imp.music.resource.PlayMusic;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public enum MusicSourceClientReferencesType {
    //LOCAL_FILE(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_source_references/local_file.png"), PlayLocation.LocationType.WORLD_FILE),
    YOUTUBE(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_source_references/youtube.png"), PlayLocation.LocationType.YOUTUBE),
    URL(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_source_references/url.png"), PlayLocation.LocationType.URL);
    private final ResourceLocation textuerLocation;
    private final PlayLocation.LocationType lctype;

    private static final LavaMusicLoader urlMusicLoader = new LavaMusicLoader(new HttpAudioSourceManager());
    private static final LavaMusicLoader youtubeMusicLoader = new LavaMusicLoader(new YoutubeAudioSourceManager());

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

    public static IMusicPlayer getMusicPlayer(PlayMusic music) throws YoutubeException, IOException, IMPWorldMusicException, InterruptedException, IMPFFmpegException {
        return MusicSourceClientReferencesType.getTypeByLocationType(music.getMusicLocation().getLocationType()).getMusicPlayer(music.getMusicLocation().getIdOrURL());
    }

    public IMusicPlayer getMusicPlayer(String src) throws YoutubeException, IOException {
        long rerytime = System.currentTimeMillis();

        switch (this) {
            case YOUTUBE:
                return new YoutubeMusicPlayer(rerytime, src, urlMusicLoader, youtubeMusicLoader);
            case URL:
                return new URLNotStreamMusicPlayer(rerytime, new URL(src), urlMusicLoader);
            //   case LOCAL_FILE:
            //       return new WorldFileMusicPlayer(rerytime, src);
            default:
                return null;
        }
    }
}



