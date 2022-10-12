package dev.felnull.imp.client.nmusic.task;

public class MusicEngineTaskRunner implements MusicTaskRunner {
    private boolean destroy;

    @Override
    public boolean isDestroy() {
        return destroy;
    }

    public void destroy() {
        destroy = true;
    }
}
