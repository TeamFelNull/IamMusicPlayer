package dev.felnull.imp.client.music.task;

public class MusicEngineDestroyRunner implements MusicDestroyRunner {
    private boolean destroy;

    @Override
    public boolean isDestroy() {
        return destroy;
    }

    public void destroy() {
        destroy = true;
    }
}
