package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.music.subtitle.SubtitleEntry;
import dev.felnull.imp.client.music.subtitle.SubtitleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicSubtitleOverlay extends Overlay {
    public static final MusicSubtitleOverlay SUBTITLE_OVERLAY = new MusicSubtitleOverlay();
    private static final Minecraft mc = Minecraft.getInstance();
    private final Map<SubtitleEntry, Long> SUBTITLES = new HashMap<>();
    private final List<SubtitleEntry> REMOVE_SUBTITLES = new ArrayList<>();
    private final Map<SubtitleEntry, Long> UPDATE_SUBTITLES = new HashMap<>();
    private final Map<SubtitleEntry, Boolean> PAUSED_SUBTITLES = new HashMap<>();
    private final Map<SubtitleEntry, Long> PAUSED_TIME_SUBTITLES = new HashMap<>();

    public void addSubtitle(SubtitleEntry entry) {
        SUBTITLES.put(entry, System.currentTimeMillis());
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        if (IamMusicPlayer.CONFIG.subtitleType != SubtitleType.OVERLAY) {
            SUBTITLES.clear();
            REMOVE_SUBTITLES.clear();
            UPDATE_SUBTITLES.clear();
            PAUSED_SUBTITLES.clear();
            PAUSED_TIME_SUBTITLES.clear();
            return;
        }

        SUBTITLES.forEach((n, m) -> {
            var pc = PAUSED_SUBTITLES.get(n);
            var cp = n.musicPlayer().isPaused();
            if (pc != null && pc != cp) {
                if (cp) {
                    PAUSED_TIME_SUBTITLES.put(n, System.currentTimeMillis());
                } else {
                    var pt = PAUSED_TIME_SUBTITLES.get(n);
                    if (pt != null) {
                        UPDATE_SUBTITLES.put(n, m + (System.currentTimeMillis() - pt));
                    }
                    PAUSED_TIME_SUBTITLES.clear();
                }
            }
            PAUSED_SUBTITLES.put(n, cp);

            if (((m + n.duration()) < System.currentTimeMillis() && n.musicPlayer().isPlaying()) || n.musicPlayer().isFinished() || (!n.musicPlayer().isPaused() && !n.musicPlayer().isPlaying()))
                REMOVE_SUBTITLES.add(n);
        });
        SUBTITLES.putAll(UPDATE_SUBTITLES);
        UPDATE_SUBTITLES.clear();
        REMOVE_SUBTITLES.forEach(SUBTITLES::remove);
        REMOVE_SUBTITLES.clear();

        if (SUBTITLES.isEmpty())
            return;
        int fh = mc.font.lineHeight;
        int mh = mc.getWindow().getGuiScaledHeight() - getMinHeight() - 1;
        mh -= SUBTITLES.size() * (fh + 1);
        int ch = 0;
        for (SubtitleEntry sub : SUBTITLES.keySet()) {
            drawText(poseStack, mh + ch, sub.component());
            ch += fh + 1;
        }
    }

    private void drawText(PoseStack poseStack, int y, Component text) {
        var x = mc.getWindow().getGuiScaledWidth() / 2;
        Font font = mc.font;
        int width = font.width(text) + 2;
        x -= width / 2;
        fill(poseStack, x, y, x + width, y + font.lineHeight, mc.options.getBackgroundColor(0.8F));
        font.draw(poseStack, text, x + 1, y + 1, 14737632);
    }

    private int getMinHeight() {
        int h = 0;

        if (mc.screen == null) {
            h += 22;
            if (mc.player != null && !mc.player.isSpectator() && !mc.player.isCreative()) {
                h += 17;
                if (mc.player.getArmorValue() > 0)
                    h += 10;
            }
        } else if (mc.screen instanceof ChatScreen) {
            h += 15;
        }

        return h;
    }
}
