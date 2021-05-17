package red.felnull.imp.client.music.subtitle;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import red.felnull.imp.client.music.MusicEngine;
import red.felnull.otyacraftengine.client.util.IKSGClientUtil;

import java.util.*;

public class SubtitleManager {
    private static final SubtitleManager INSTANCE = new SubtitleManager();
    private static final Minecraft mc = Minecraft.getInstance();
    protected final Map<UUID, IMusicSubtitle> subtitles = new HashMap<>();
    protected final Map<UUID, Long> subtitleLastTimes = new HashMap<>();

    public static SubtitleManager getInstance() {
        return INSTANCE;
    }

    public Optional<String> getDefaultSubtitle(List<String> langCodes) {
        return langCodes.stream().filter(n -> mc.getLanguageManager().getSelected().getCode().contains(n)).findFirst();
    }

    public void tick(boolean paused) {
        MusicEngine me = MusicEngine.getInstance();
        List<UUID> removedSubs = new ArrayList<>();

        subtitles.forEach((n, m) -> {
            if (m.isLoaded()) {
                if (!me.isExist(n)) {
                    removedSubs.add(n);
                    return;
                }
                long po = m.getMusicPlayer().getPosition();
                if (!subtitleLastTimes.containsKey(n))
                    subtitleLastTimes.put(n, po);

                long sa = po - subtitleLastTimes.get(n);
                List<MusicSubtitleEntry> addSubs = new ArrayList<>();

                m.getSubtitles().stream().filter(l -> l.getStartTime() >= po && l.getStartTime() <= po + sa).forEach(addSubs::add);

                subtitleLastTimes.put(n, po);

                addSubs.forEach(l -> IKSGClientUtil.addSubtitle(new TextComponent(l.getText()), l.getDuration(), () -> m.getMusicPlayer().getSelfPosition()));
            }
        });

        removedSubs.forEach(n -> {
            subtitles.remove(n);
            subtitleLastTimes.remove(n);
        });
    }


    public static class MusicSubtitleEntry {
        private final long startTime;
        private final long duration;
        private final String text;

        public MusicSubtitleEntry(long startTime, long duration, String text) {
            this.startTime = startTime;
            this.duration = duration;
            this.text = text;
        }

        public long getDuration() {
            return duration;
        }

        public long getStartTime() {
            return startTime;
        }

        public String getText() {
            return text;
        }
    }

}
