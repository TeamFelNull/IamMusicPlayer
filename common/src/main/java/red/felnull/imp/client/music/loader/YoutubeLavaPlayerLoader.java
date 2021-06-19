package red.felnull.imp.client.music.loader;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import red.felnull.imp.data.resource.ImageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class YoutubeLavaPlayerLoader extends LavaPlayerLoader implements IMusicSearchable {
    public YoutubeLavaPlayerLoader() {
        super("youtube", "BaW_jenozKc", new YoutubeAudioSourceManager(true));
    }

    @Override
    public List<SearchData> search(String identifier) {
        List<SearchData> list = new ArrayList();
        AtomicBoolean ff = new AtomicBoolean(false);
        getAudioPlayerManager().loadItemOrdered(UUID.randomUUID(), "ytsearch:" + identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if (!track.getInfo().isStream)
                    list.add(toMusicSearchData(track));
                ff.set(true);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                playlist.getTracks().stream().filter(n -> !n.getInfo().isStream).forEach(n -> list.add(toMusicSearchData(n)));
                ff.set(true);
            }

            @Override
            public void noMatches() {
                ff.set(true);
            }

            @Override
            public void loadFailed(FriendlyException ex) {
                ff.set(true);
            }
        });
        long ft = System.currentTimeMillis();

        while (!ff.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - ft > 30000)
                break;
        }

        return list;
    }

    @Override
    protected SearchData toMusicSearchData(AudioTrack track) {
        return new SearchData(track.getInfo().title, track.getInfo().title + " - " + track.getInfo().author, track.getIdentifier(), new ImageInfo(ImageInfo.ImageType.YOUTUBE_THUMBNAIL, track.getIdentifier()), track.getDuration(), track.getInfo().author);
    }
}
