package dev.felnull.imp.client.gui.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.client.nmusic.MusicEngine;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;

public class MusicLinesOverlay extends GuiComponent {
    private static final Minecraft mc = Minecraft.getInstance();

    public void render(PoseStack poseStack, float tickDelta) {
        var mm = MusicEngine.getInstance();
        int fh = mc.font.lineHeight;

        drawText(poseStack, mm.getDebugString(), 2, 2);

        drawMusicLine(poseStack, 1, fh + 3);

    }

    private void drawMusicLine(PoseStack poseStack, int x, int y) {
        var ms = new MusicSource("youtube", "fcohtest", 11451419);
        String duStr = ms.isLive() ? "Live" : FNStringUtil.getTimeFormat(ms.getDuration());
        drawText(poseStack, String.format("%s:%s %s", ms.getLoaderType(), ms.getIdentifier(), duStr), x + 1, y);

        int sw = mc.getWindow().getGuiScaledWidth();
        int fh = mc.font.lineHeight;

        OERenderUtils.drawFill(poseStack, x, y + fh - 1, sw - 2, y + fh - 1 + 63, -1873784752);
        OERenderUtils.drawFill(poseStack, x + 2, y + fh - 1 + 61, sw - 4, y + fh - 1 + 61 + 1, 0xFFFFFFFF);

        int ssc = 10;
        for (int i = 0; i < ssc; i++) {
            float ow = (((float) sw - 4f) - ((float) x + 2f)) / ((float) ssc - 1f);
            OERenderUtils.drawFill(poseStack, x + 2 + (ow * i), y + fh - 1 + 60, x + 2 + (ow * i) + 1, y + fh - 1 + 60 + 3, 0XFFDCDCDC);
        }
    }

    private void drawText(PoseStack poseStack, String text, int x, int y) {
        int j = mc.font.lineHeight;
        int k = mc.font.width(text);
        fill(poseStack, 1, y - 1, 2 + k + 1, y + j - 1, -1873784752);
        mc.font.draw(poseStack, text, x, y, 14737632);
    }
}
