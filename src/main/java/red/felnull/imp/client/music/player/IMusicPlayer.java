package red.felnull.imp.client.music.player;

public interface IMusicPlayer {
    void play(long startMiliSecond);

    void stop();

    boolean isPlaying();

    long getCureentElapsed();

    long getDuration();

    Object getMusicSource();

}
