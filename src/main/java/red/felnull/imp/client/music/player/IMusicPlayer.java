package red.felnull.imp.client.music.player;

public interface IMusicPlayer {

    void ready(long startMiliSecond) throws Exception;

    void play();

    void playMisalignment(long zure);

    void playAutoMisalignment();

    void playAndReady(long startMiliSecond) throws Exception;

    void stop();

    boolean isPlaying();

    long getMaxMisalignment();

    long getCureentElapsed();

    long getDuration();

    Object getMusicSource();

    void setVolume(float vol);

    float getVolume();
}
