package red.felnull.imp.client.music.player;

public interface IMusicPlayer {

    void ready(long startMiliSecond);

    void play();

    void playAndReady(long startMiliSecond);

    void stop();

    boolean isPlaying();

    long getCureentElapsed();

    long getDuration();

    Object getMusicSource();

}
