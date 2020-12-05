package red.felnull.imp.client.data;


import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

public class YoutubeData {
    private static DefaultAudioPlayerManager audioPlayerManager;

    public static void init() {
        audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
    }

    public static DefaultAudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }
}
