package dev.felnull.imp.client.music.loadertypes;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.client.gui.screen.monitor.SearchMusicMMMonitor;
import dev.felnull.imp.client.util.LavaPlayerUtil;
import dev.felnull.imp.music.resource.ImageInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class YoutubeMusicLoaderType extends AbstractLavaPlayerMusicLoaderType {
    private static final Component YT_ENTER_TEXT = new TranslatableComponent("imp.text.enterText.youtube");

    public YoutubeMusicLoaderType() {
        super(IMPMusicLoaderTypes.YOUTUBE);
    }

    @Override
    public boolean isSearchable() {
        return true;
    }

    @Override
    public void registerSourceManager(AudioPlayerManager audioPlayerManager) {
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
    }

    @Override
    protected ImageInfo createThumbnail(AudioTrack track) {
        return new ImageInfo(ImageInfo.ImageType.YOUTUBE_THUMBNAIL, track.getIdentifier());
    }

    @Override
    public boolean match(AudioTrack track) {
        return track.getSourceManager() instanceof YoutubeAudioSourceManager;
    }

    @Override
    public List<SearchMusicMMMonitor.SearchMusicEntry> search(String name) throws InterruptedException {
        List<SearchMusicMMMonitor.SearchMusicEntry> musics = new ArrayList<>();
        List<AudioTrack> tracks;
        try {
            tracks = LavaPlayerUtil.searchYoutube(getAudioPlayerManager(), name);
        } catch (ExecutionException e) {
            return musics;
        }
        for (AudioTrack track : tracks) {
            var ret = createResult(track);
            musics.add(new SearchMusicMMMonitor.SearchMusicEntry(ret.name(), track.getInfo().author, ret.source(), ret.imageInfo()));
        }
        return musics;
    }

    @Override
    public Component getEnterText() {
        return YT_ENTER_TEXT;
    }
}
