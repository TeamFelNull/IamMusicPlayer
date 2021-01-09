package red.felnull.imp.client.music.player;

public interface IMusicPlayer {

    void ready(long startMiliSecond);

    void play();

    void playMisalignment(long zure);

    void playAutoMisalignment();

    void playAndReady(long startMiliSecond);

    void stop();

    boolean isPlaying();

    long getMaxMisalignment();

    long getCureentElapsed();

    long getDuration();

    Object getMusicSource();

    void setVolume(float vol);

    float getVolume();
}
