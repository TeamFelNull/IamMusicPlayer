package red.felnull.imp.client.music.player;

import com.github.kiulian.downloader.YoutubeException;
import com.sedmelluq.discord.lavaplayer.format.AudioDataFormatTools;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import red.felnull.imp.client.util.YoutubeUtils;

import java.io.IOException;

public class YoutubeMusicPlayer extends URLNotStreamMusicPlayer {
    private final LavaMusicLoader youtubeMusicLoader;
    private final String videoID;

    public YoutubeMusicPlayer(long rery, String videoID, LavaMusicLoader loader, LavaMusicLoader youtubeLoader) throws IOException, YoutubeException {
        super(rery, YoutubeUtils.getYoutubeMa4DirectLink(videoID), loader);
        this.videoID = videoID;
        this.youtubeMusicLoader = youtubeLoader;
    }

    @Override
    public void ready(long position) throws Exception {
        if (url != null) {
            super.ready(position);
            if (ready) return;
        }
        startPosition = position;
        this.trackLoaded = false;
        youtubeMusicLoader.getAudioPlayerManager().loadItem(videoID, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setPosition(position);
                audioPlayer.startTrack(track, false);
                if (!track.getInfo().isStream)
                    duration = track.getDuration();

                trackLoaded = true;
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                exception = new IllegalStateException("ambiguous");
                trackLoaded = true;
            }

            @Override
            public void noMatches() {
                exception = new IllegalStateException("nomatche");
                trackLoaded = true;
            }

            @Override
            public void loadFailed(FriendlyException ex) {
                exception = ex;
                trackLoaded = true;
            }
        });

        while (!trackLoaded) {
            Thread.sleep(20);
        }

        if (exception != null) {
            exception = null;
            trackLoaded = false;
        } else {
            stream = AudioPlayerInputStream.createStream(audioPlayer, dataformat, dataformat.frameDuration(), false);
            stereo = AudioDataFormatTools.toAudioFormat(dataformat).getChannels() >= 2;

            loadOpenAL();

            ready = true;
        }
    }

    @Override
    public Object getMusicSource() {
        return videoID;
    }

}
