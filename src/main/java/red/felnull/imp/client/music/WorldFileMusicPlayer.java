package red.felnull.imp.client.music;

public class WorldFileMusicPlayer implements IMusicPlayer {
    private final String uuid;

    public WorldFileMusicPlayer(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public void play(long startMiliSecond) {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public long getCureentElapsed() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public Object getMusicSource() {
        return uuid;
    }
}
