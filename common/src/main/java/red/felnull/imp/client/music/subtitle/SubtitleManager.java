package red.felnull.imp.client.music.subtitle;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.gui.components.MusicSubtitleOverlay;
import red.felnull.imp.client.music.MusicEngine;
import red.felnull.imp.client.util.SubtitleUtil;
import red.felnull.otyacraftengine.client.util.IKSGClientUtil;

import java.util.*;

public class SubtitleManager {
    private static final SubtitleManager INSTANCE = new SubtitleManager();
    private static final Minecraft mc = Minecraft.getInstance();
    protected final Map<UUID, IMusicSubtitle> subtitles = new HashMap<>();
    protected final Map<UUID, Long> subtitleLastTimes = new HashMap<>();
    public final MusicSubtitleOverlay overlay = new MusicSubtitleOverlay();

    public static SubtitleManager getInstance() {
        return INSTANCE;
    }

    public Optional<String> getDefaultSubtitle(List<String> langCodes) {

        if (langCodes.contains(IamMusicPlayer.CONFIG.subtitleLanguage))
            return Optional.of(IamMusicPlayer.CONFIG.subtitleLanguage);

        Optional<String> al = langCodes.stream().filter(n -> mc.getLanguageManager().getSelected().getCode().contains(n)).findFirst();

        if (al.isPresent())
            return al;

        if (langCodes.contains("en"))
            return Optional.of("en");

        return Optional.empty();
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

                m.getSubtitles().stream().filter(l -> l.startTime() >= po && l.startTime() <= po + sa).forEach(addSubs::add);

                subtitleLastTimes.put(n, po);

                addSubs.forEach(l -> {
                    if (IamMusicPlayer.CONFIG.subtitleSystem == SubtitleSystem.OVERLAY) {
                        overlay.addSubtitle(l);
                    } else if (IamMusicPlayer.CONFIG.subtitleSystem == SubtitleSystem.VANILLA) {
                        IKSGClientUtil.addSubtitle(l.text(), l.duration(), () -> m.getMusicPlayer().getSelfPosition());
                    }
                });
            }
        });

        removedSubs.forEach(n -> {
            subtitles.remove(n);
            subtitleLastTimes.remove(n);
        });

        if (mc.level == null)
            overlay.clear();
    }

    public Component createSubtitle(String text) {

        try {
            if (!SubtitleUtil.getFirstCodes(text).isEmpty()) {
                List<Component> comps = SubtitleUtil.getHTMLSubtitleComponents(text);

                MutableComponent component = null;

                for (Component comp : comps) {
                    if (component == null)
                        component = comp.copy();
                    else
                        component.append(comp);
                }

                if (component != null)
                    return component;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        text = SubtitleUtil.convertDisplayableSubtitle(text);

        return new TextComponent(text);
    }

    public static record MusicSubtitleEntry(long startTime, long duration, Component text) {
    }

}
