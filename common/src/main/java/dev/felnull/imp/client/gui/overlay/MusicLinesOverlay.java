package dev.felnull.imp.client.gui.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.music.MusicEntry;
import dev.felnull.imp.client.music.player.MusicLoadChunk;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Map;
import java.util.UUID;

public class MusicLinesOverlay {
    private static final Minecraft mc = Minecraft.getInstance();

    public void render(GuiGraphics guiGraphics, float tickDelta) {
        var me = MusicEngine.getInstance();
        int fh = mc.font.lineHeight;

        drawText(guiGraphics, me.getDebugString(), 2, 2);

        int ct = 0;
        for (Map.Entry<UUID, MusicEntry> entry : me.getMusicEntries().entrySet()) {
            drawMusicLine(guiGraphics, 1, fh + 3 + (ct * (65 + mc.font.lineHeight)), entry.getKey(), entry.getValue());
            ct++;
        }
    }

    private void drawMusicLine(GuiGraphics guiGraphics, int x, int y, UUID uuid, MusicEntry entry) {
        PoseStack poseStack = guiGraphics.pose();

        var source = entry.getSource();
        String duStr = source.isLive() ? ("Live(" + FNStringUtil.getTimeFormat(entry.getCurrentPosition()) + ")") : FNStringUtil.getTimeProgress(entry.getCurrentPosition(), source.getDuration());

        String text;

        if (entry.isPlaying()) {
            text = " §2Playing§r";
        } else if (!entry.isLoaded()) {
            text = " §6Loading§r";
        } else {
            text = " §3Waiting§r";
        }


        String idf = source.getIdentifier();
        if (idf.length() > 15)
            idf = idf.substring(0, 15) + "...";

        String mst;
        if (source.getLoaderType().isEmpty()) {
            mst = idf;
        } else {
            mst = String.format("%s:%s", source.getLoaderType(), idf);
        }

        drawText(guiGraphics, String.format("%s %s", mst, duStr) + text, x + 1, y);

        int sw = mc.getWindow().getGuiScaledWidth();
        int fh = mc.font.lineHeight;

        OERenderUtils.drawFill(poseStack, x, y + fh - 1, sw - 2, y + fh - 1 + 63, -1873784752);
        OERenderUtils.drawFill(poseStack, x + 2, y + fh - 1 + 61, sw - 4, y + fh - 1 + 61 + 1, 0xFFFFFFFF);

        int ssc = 10;
        for (int i = 0; i < ssc; i++) {
            float ow = (((float) sw - 4f) - ((float) x + 2f)) / ((float) ssc - 1f);
            OERenderUtils.drawFill(poseStack, x + 2 + (ow * i), y + fh - 1 + 60, x + 2 + (ow * i) + 1, y + fh - 1 + 60 + 3, 0XFFDCDCDC);
        }
        int all = (sw - 4) - (x + 2);

        long liveLoop = 1000 * 60;
        long liveLoopPos = entry.getCurrentPosition() % liveLoop;
        long loopCount = entry.getCurrentPosition() / liveLoop;

        var chunks = entry.getLoadChunks();

        if (!source.isLive()) {
            for (MusicLoadChunk chunk : chunks) {
                boolean l = (chunk.position() / 1000) % 2 == 0;
                drawLine(poseStack, x, y, (float) chunk.position() / (float) source.getDuration(), ((float) chunk.duration() / (float) source.getDuration()) * (float) all, 1f, l ? 0x7000FF00 : 0x7000C900);
            }
        } else {
            for (MusicLoadChunk chunk : chunks) {
                if (chunk.position() / liveLoop != loopCount)
                    continue;
                boolean l = (chunk.position() / 1000) % 2 == 0;
                long liveLoopChunkPos = chunk.position() % liveLoop;
                drawLine(poseStack, x, y, (float) liveLoopChunkPos / (float) liveLoop, ((float) chunk.duration() / (float) liveLoop) * (float) all, 1f, l ? 0x7000FF00 : 0x7000C900);
            }
        }

        if (!source.isLive()) {
            drawPositionLine(poseStack, x, y, (float) entry.getStartPosition() / (float) source.getDuration(), 0xFF0000FF);
        } else {
            if (loopCount == 0)
                drawPositionLine(poseStack, x, y, (float) entry.getStartPosition() / liveLoop, 0xFF0000FF);
        }


        int len = all / 10;
        float crunt = (float) entry.getCurrentPosition() / (float) source.getDuration();
        float ol = 1f / 60f / (float) source.getDuration();

        for (int i = 0; i < len; i++) {
            drawLine(poseStack, x, y, ol * i, ol, 1f, 0xFFFF00FF);
        }

        if (!source.isLive()) {
            drawLine(poseStack, x, y, crunt, 1f, 1f, 0XFFFFFF00);
        } else {
            float lcrunt = (float) liveLoopPos / (float) liveLoop;
            drawLine(poseStack, x, y, lcrunt, 1f, 1f, 0XFFFFFF00);
        }
    }

    private void drawPositionLine(PoseStack poseStack, float x, float y, float position, int color) {
        drawLine(poseStack, x, y, position, 1f, 1f, color);
    }

    private void drawLine(PoseStack poseStack, float x, float y, float position, float w, float h, int color) {
        int fh = mc.font.lineHeight;
        int sw = mc.getWindow().getGuiScaledWidth();
        float p = (((float) sw - 4f) - (x + 2f)) * position;
        h *= 60f;
        OERenderUtils.drawFill(poseStack, x + 2 + p, y + fh - 1 + 1 + ((60f - h) / 2f), x + 2 + p + w, y + fh - 1 + 1 + ((60f - h) / 2f) + h, color);
    }

    private void drawText(GuiGraphics guiGraphics, String text, int x, int y) {
        int j = mc.font.lineHeight;
        int k = mc.font.width(text);
        guiGraphics.fill(1, y - 1, 2 + k + 1, y + j - 1, -1873784752);
        //mc.font.draw(poseStack, text, x, y, 14737632);

        guiGraphics.drawString(mc.font, text, x, y, 14737632);
    }
}
