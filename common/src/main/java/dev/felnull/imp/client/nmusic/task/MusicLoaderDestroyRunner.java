package dev.felnull.imp.client.nmusic.task;

import java.util.function.BooleanSupplier;

public class MusicLoaderDestroyRunner implements MusicDestroyRunner {
    private final MusicEngineDestroyRunner engineTaskRunner;
    private final BooleanSupplier stopped;

    public MusicLoaderDestroyRunner(MusicEngineDestroyRunner engineTaskRunner, BooleanSupplier stopped) {
        this.engineTaskRunner = engineTaskRunner;
        this.stopped = stopped;
    }

    @Override
    public boolean isDestroy() {
        return engineTaskRunner.isDestroy() || stopped.getAsBoolean();
    }
}
