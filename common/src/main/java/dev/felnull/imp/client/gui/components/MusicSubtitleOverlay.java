package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.music.MusicEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.network.chat.Component;

import java.util.List;

public class MusicSubtitleOverlay extends Overlay {
    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        List<Component> subs = MusicEngine.getInstance().getSubtitles();
        if (subs.isEmpty())
            return;
        int fh = mc.font.lineHeight;
        int mh = mc.getWindow().getGuiScaledHeight() - getMinHeight() - 1;
        mh -= subs.size() * (fh + 1);
        int ch = 0;
        for (Component sub : subs) {
            drawText(poseStack, mh + ch, sub);
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
