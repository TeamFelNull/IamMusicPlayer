package red.felnull.imp.client.music;

import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.music.resource.MusicLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MusicEngine {
    private static final MusicEngine INSTANCE = new MusicEngine();
    protected final Map<UUID, IMusicPlayer> musicPlayers = new HashMap<>();
    private final Map<UUID, Long> playedMusicIds = new HashMap<>();

    public static MusicEngine getInstance() {
        return INSTANCE;
    }

    public void ready(UUID uuid, MusicLocation musicLocation, long startPosition) {
        MusicLoaderThread ml = new MusicLoaderThread(uuid, musicLocation, startPosition);
        ml.start();
    }

    public void play(UUID uuid, long delay) {
        playedMusicIds.put(uuid, delay);
    }

    public void tick(boolean paused) {

    }

    public void tickNonPaused() {
        playedMusicIds.forEach((n, m) -> musicPlayers.get(n).play(m));
        playedMusicIds.clear();
        musicPlayers.values().forEach(IMusicPlayer::update);
    }

    public void pause() {
        musicPlayers.values().forEach(IMusicPlayer::pause);
    }

    public void resume() {
        musicPlayers.values().forEach(IMusicPlayer::unpause);
    }

}
