package red.felnull.imp.client.music;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MusicThread extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(MusicThread.class);
    private static MusicThread thread;
    private boolean stoped;

    public static void startThread() {
        if (thread != null)
            thread.stoped();

        thread = new MusicThread();
        thread.start();
    }

    public MusicThread() {
        this.setName("Music thread");
    }

    @Override
    public void run() {
        LOGGER.info("Music thread Start");
        while (!stoped) {
            try {
                sleep(20);
                loop();
            } catch (Exception ex) {
                LOGGER.error("Music thread Error", ex);
            }
        }
        LOGGER.info("Music thread Stop");
    }

    public void stoped() {
        stoped = true;
    }

    private static void loop() {
        ClientWorldMusicManager.instance().loop();
    }
}
