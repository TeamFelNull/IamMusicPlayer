package dev.felnull.imp.util;

import java.util.concurrent.atomic.AtomicBoolean;

public class FlagThread extends Thread {
    private final AtomicBoolean running = new AtomicBoolean(true);

    public boolean isRunning() {
        return running.get();
    }

    public boolean isStopped() {
        return !running.get();
    }

    public void stopped() {
        running.set(false);
    }
}
