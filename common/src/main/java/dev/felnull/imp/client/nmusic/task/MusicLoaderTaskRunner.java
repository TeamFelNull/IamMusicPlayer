package dev.felnull.imp.client.nmusic.task;

import java.util.function.BooleanSupplier;

public class MusicLoaderTaskRunner implements MusicTaskRunner {
    private final MusicEngineTaskRunner engineTaskRunner;
    private final BooleanSupplier stopped;

    public MusicLoaderTaskRunner(MusicEngineTaskRunner engineTaskRunner, BooleanSupplier stopped) {
        this.engineTaskRunner = engineTaskRunner;
        this.stopped = stopped;
    }

    @Override
    public boolean isDestroy() {
        return engineTaskRunner.isDestroy() || stopped.getAsBoolean();
    }
}
