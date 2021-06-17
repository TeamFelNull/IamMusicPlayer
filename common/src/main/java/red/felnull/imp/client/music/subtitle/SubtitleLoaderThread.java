package red.felnull.imp.client.music.subtitle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.music.resource.MusicSource;

import java.util.UUID;
import java.util.function.Supplier;

public class SubtitleLoaderThread extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(SubtitleLoaderThread.class);
    private final UUID uuid;
    private final MusicSource location;
    private final IMusicSubtitle subtitle;
    private final Supplier<Boolean> stopped;

    public SubtitleLoaderThread(UUID uuid, MusicSource location, IMusicSubtitle subtitle, Supplier<Boolean> stopped) {
        this.setName("Subtitle Loader Thread: " + location.getIdentifier());
        this.uuid = uuid;
        this.stopped = stopped;
        this.subtitle = subtitle;
        this.location = location;
    }

    @Override
    public void run() {
        if (stopped.get())
            return;

        try {
            subtitle.init();
            if (stopped.get())
                return;
            SubtitleManager.getInstance().subtitles.put(uuid, subtitle);
        } catch (Exception ex) {
            LOGGER.error("Failed to load subtitle: " + location.getIdentifier(), ex);
        }
    }
}
