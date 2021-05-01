package red.felnull.imp.client.music;

import net.minecraft.client.Minecraft;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.resource.MusicLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MusicEngine {
    private static final MusicEngine INSTANCE = new MusicEngine();
    protected final Map<UUID, IMusicPlayer> musicPlayers = new HashMap<>();
    protected final Map<UUID, MusicPlayInfo> updateInfos = new HashMap<>();
    private final Map<UUID, MusicPlayEntry> playedMusicIds = new HashMap<>();

    public static MusicEngine getInstance() {
        return INSTANCE;
    }

    public void ready(UUID uuid, MusicLocation musicLocation, long startPosition) {
        MusicLoaderThread ml = new MusicLoaderThread(uuid, musicLocation, startPosition);
        ml.start();
    }

    public void play(UUID uuid, long delay, MusicPlayInfo info) {
        playedMusicIds.put(uuid, new MusicPlayEntry(delay, info));
    }

    public void updateInfo(UUID uuid, MusicPlayInfo info) {
        updateInfos.put(uuid, info);
    }

    public void tick(boolean paused) {
    }

    public void tickNonPaused() {
        playedMusicIds.forEach((n, m) -> {
            musicPlayers.get(n).setSelfPosition(m.getInfo().getPosition());
            musicPlayers.get(n).setVolume(m.getInfo().getVolume());
            musicPlayers.get(n).linearAttenuation(m.getInfo().getMaxDistance());
            musicPlayers.get(n).play(m.getDelay());
        });
        playedMusicIds.clear();
        updateInfos.forEach((n, m) -> {
            musicPlayers.get(n).setSelfPosition(m.getPosition());
            musicPlayers.get(n).setVolume(m.getVolume());
            musicPlayers.get(n).linearAttenuation(m.getMaxDistance());
        });
        updateInfos.clear();
        musicPlayers.values().forEach(IMusicPlayer::update);
    }

    public void pause() {
        musicPlayers.values().forEach(IMusicPlayer::pause);
    }

    public void resume() {
        musicPlayers.values().forEach(IMusicPlayer::unpause);
    }

    public static class MusicPlayEntry {
        private final long delay;
        private final MusicPlayInfo info;

        public MusicPlayEntry(long delay, MusicPlayInfo info) {
            this.delay = delay;
            this.info = info;
        }

        public long getDelay() {
            return delay;
        }

        public MusicPlayInfo getInfo() {
            return info;
        }
    }
}
