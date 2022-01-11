package dev.felnull.imp.client.music.loadertypes;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.felnull.imp.music.resource.ImageInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SoundCloudMusicLoaderType extends AbstractLavaPlayerMusicLoaderType {
    public SoundCloudMusicLoaderType() {
        super(IMPMusicLoaderTypes.SOUNDCLOUD);

    }

    @Override
    public boolean isSearchable() {
        return false;
    }

    @Override
    public void registerSourceManager(AudioPlayerManager audioPlayerManager) {
        audioPlayerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
    }

    @Override
    public boolean match(AudioTrack track) {
        return track.getSourceManager() instanceof SoundCloudAudioSourceManager;
    }

    @Override
    public String getIdentifier(AudioTrack track) {
        return track.getInfo().uri;
    }

    @Override
    protected ImageInfo createThumbnail(AudioTrack track) {
        try {
            Document doc = Jsoup.connect(track.getInfo().uri).get();
            var img = doc.select("img").attr("itemprop", "image").first();
            var src = img.attr("src");
            return new ImageInfo(ImageInfo.ImageType.URL, src);
        } catch (Exception ignored) {
        }
        return null;
    }
}
