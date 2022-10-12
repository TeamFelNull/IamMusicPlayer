package dev.felnull.imp.client.nmusic.task;

public interface MusicTask {
    void execute();

    void destroy();

    void destroyOnTick();
}
