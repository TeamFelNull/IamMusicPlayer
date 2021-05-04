package red.felnull.imp.client.music;

import net.minecraft.client.Minecraft;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.music.info.MusicPlayInfo;
import red.felnull.imp.music.info.tracker.MusicTracker;
import red.felnull.imp.music.resource.MusicLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MusicEngine {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final MusicEngine INSTANCE = new MusicEngine();
    protected final Map<UUID, MusicPlayingEntry> musicPlayers = new HashMap<>();
    protected final Map<UUID, MusicPlayInfo> updateInfos = new HashMap<>();
    private final Map<UUID, MusicPlayStartEntry> playedMusicIds = new HashMap<>();

    public static MusicEngine getInstance() {
        return INSTANCE;
    }

    public void ready(UUID uuid, MusicLocation musicLocation, long startPosition) {
        MusicLoaderThread ml = new MusicLoaderThread(uuid, musicLocation, startPosition);
        ml.start();
    }

    public void play(UUID uuid, long delay, MusicPlayInfo info) {
        playedMusicIds.put(uuid, new MusicPlayStartEntry(delay, info));
    }

    public void updateInfo(UUID uuid, MusicPlayInfo info) {
        updateInfos.put(uuid, info);
    }

    public void tick(boolean paused) {
    }

    public void tickNonPaused() {
        playedMusicIds.forEach((n, m) -> {
            musicPlayers.get(n).musicTracker = m.info.getTracker();
            MusicPlayingEntry entry = musicPlayers.get(n);
            entry.musicPlayer.setVolume(m.info.getVolume());
            entry.musicPlayer.linearAttenuation(m.info.getMaxDistance());
            entry.musicPlayer.play(m.delay);
        });
        playedMusicIds.clear();
        updateInfos.forEach((n, m) -> {
            musicPlayers.get(n).musicTracker = m.getTracker();
            MusicPlayingEntry entry = musicPlayers.get(n);
            entry.musicPlayer.setVolume(m.getVolume());
            entry.musicPlayer.linearAttenuation(m.getMaxDistance());
        });
        updateInfos.clear();
        musicPlayers.values().forEach(n -> {
            n.musicPlayer.update();
            n.musicPlayer.setSelfPosition(n.musicTracker.getTrackingPosition(mc.level));
        });
    }

    public void pause() {
        musicPlayers.values().forEach(n -> n.musicPlayer.pause());
    }

    public void resume() {
        musicPlayers.values().forEach(n -> n.musicPlayer.unpause());
    }

    public static class MusicPlayStartEntry {
        public final long delay;
        public final MusicPlayInfo info;

        public MusicPlayStartEntry(long delay, MusicPlayInfo info) {
            this.delay = delay;
            this.info = info;
        }
    }

    public static class MusicPlayingEntry {
        public final IMusicPlayer musicPlayer;
        public MusicTracker musicTracker;

        public MusicPlayingEntry(IMusicPlayer musicPlayer, MusicTracker musicTracker) {
            this.musicPlayer = musicPlayer;
            this.musicTracker = musicTracker;
        }
    }
}
