package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import red.felnull.imp.client.gui.IMPFonts;
import red.felnull.imp.client.music.subtitle.SubtitleManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicSubtitleOverlay extends GuiComponent {
    private static final Minecraft mc = Minecraft.getInstance();
    private final Map<SubtitleManager.MusicSubtitleEntry, Long> subtitleEntries = new HashMap<>();

    public void addSubtitle(SubtitleManager.MusicSubtitleEntry subtitleEntry) {
        subtitleEntries.put(subtitleEntry, System.currentTimeMillis());
    }

    public void render(PoseStack poseStack) {
        Font font = mc.font;

        int num = 0;
        List<SubtitleManager.MusicSubtitleEntry> removes = new ArrayList<>();
        for (Map.Entry<SubtitleManager.MusicSubtitleEntry, Long> entry : subtitleEntries.entrySet()) {
            num++;

            int h = getMinHeight() + (2 + font.lineHeight) * num;
            if (h <= getMaxHeight()) {
                Component text = entry.getKey().text().copy().withStyle(IMPFonts.FLOPDE_SIGN_FONT);
                drawText(poseStack, font.lineHeight, getWidth() / 2, getHeight() - h, text);
            }

            if (System.currentTimeMillis() >= entry.getValue() + entry.getKey().duration() - 30)
                removes.add(entry.getKey());
        }
        removes.forEach(subtitleEntries::remove);
    }

    public void clear() {
        subtitleEntries.clear();
    }

    private void drawText(PoseStack poseStack, int height, int x, int y, Component text) {
        Font font = mc.font;
        int width = font.width(text) + 2;
        x -= width / 2;
        fill(poseStack, x, y, x + width, y + height, mc.options.getBackgroundColor(0.8F));
        font.draw(poseStack, text, x + 1, y + 1, 14737632);
    }

    private int getWidth() {
        return mc.getWindow().getGuiScaledWidth();
    }

    private int getHeight() {
        return mc.getWindow().getGuiScaledHeight();
    }

    private int getMinHeight() {
        int h = 0;

        if (mc.screen == null) {
            h += 22;
            if (!mc.player.isSpectator() && !mc.player.isCreative())
                h += 18;
        } else if (mc.screen instanceof ChatScreen) {
            h += 15;
        }

        return h;
    }

    private int getMaxHeight() {
        return getHeight();
    }
}
