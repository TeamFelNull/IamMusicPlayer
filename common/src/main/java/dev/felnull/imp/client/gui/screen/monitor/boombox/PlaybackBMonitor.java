package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.block.BoomboxData;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.music.resource.MusicSource;
import dev.felnull.imp.util.IMPItemUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaybackBMonitor extends PlayBackFiniteBaseBMMonitor {
    private static final Component NO_ANTENNA_TEXT = Component.translatable("imp.text.noAntenna");
    private static final Component NO_CASSETTE_TAPE_TEXT = Component.translatable("imp.text.noCassetteTape");
    private static final Component NO_MUSIC_CASSETTE_TAPE_TEXT = Component.translatable("imp.text.noMusicCassetteTape");

    public PlaybackBMonitor(BoomboxData.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    protected @NotNull ImageInfo getPlayBackImage(BoomboxData data) {
        var m = getMusic(data);
        if (m != null)
            return m.getImage();
        return ImageInfo.EMPTY;
    }

    @Override
    protected @NotNull String getPlayBackName(BoomboxData data) {
        var m = getMusic(data);
        if (m != null)
            return m.getName();
        return "";
    }

    @Override
    protected @NotNull String getPlayBackAuthor(BoomboxData data) {
        var m = getMusic(data);
        if (m != null)
            return m.getAuthor();
        return "";
    }

    @Nullable
    protected Music getMusic(BoomboxData data) {
        var tape = data.getCassetteTape();
        if (IMPItemUtil.isCassetteTape(tape))
            return CassetteTapeItem.getMusic(tape);
        return null;
    }

    @Override
    protected boolean canPlay(BoomboxData data) {
        return IMPItemUtil.isCassetteTape(data.getCassetteTape()) && getMusic(data) != null;
    }

    @Override
    protected @NotNull MusicSource getPlayBackSource(BoomboxData data) {
        var m = getMusic(data);
        if (m != null)
            return m.getSource();
        return MusicSource.EMPTY;
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        getScreen().lastNoAntenna = 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, float f, int mouseX, int mouseY) {
        super.render(guiGraphics, f, mouseX, mouseY);
        if (!getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape())) {
            Music music = CassetteTapeItem.getMusic(getCassetteTape());
            if (music == null)
                drawSmartCenterText(guiGraphics, NO_MUSIC_CASSETTE_TAPE_TEXT, getStartX() + width / 2f, getStartY() + (height - 10f) / 2f);
        } else {
            drawSmartCenterText(guiGraphics, NO_CASSETTE_TAPE_TEXT, getStartX() + width / 2f, getStartY() + (height - 10f) / 2f);
        }

        long noAntennaTime = System.currentTimeMillis() - getScreen().lastNoAntenna;
        if (noAntennaTime <= 3000) {
            float alpha = Math.min(3f - ((float) noAntennaTime / 3000f) * 3f, 1f);
            int ad = (int) ((float) 0xff * alpha);
            float fl = mc.font.width(NO_ANTENNA_TEXT) + 6f;
            float st = ((float) width - fl) / 2f;
            float sy = ((float) height - 10f) / 2f;
            drawFill(guiGraphics, (int) (getStartX() + st), (int) (getStartY() + sy), (int) fl, 10, 0xa9a9a9 | (ad << 24));
            drawSmartText(guiGraphics, NO_ANTENNA_TEXT, getStartX() + st + 3, getStartY() + sy + 1f, (Math.max(ad, 5) << 24));
        }
    }

    @Override
    public void renderAppearance(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight, BoomboxData data) {
        super.renderAppearance(poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight, data);
        var cassetteTape = data.getCassetteTape();

        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        if (!cassetteTape.isEmpty() && IMPItemUtil.isCassetteTape(cassetteTape)) {
            Music music = CassetteTapeItem.getMusic(cassetteTape);
            if (music == null)
                renderSmartCenterTextSprite(poseStack, multiBufferSource, NO_MUSIC_CASSETTE_TAPE_TEXT, ((float) width / 2f), (((float) height - 10f) / 2f), OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);
        } else {
            renderSmartCenterTextSprite(poseStack, multiBufferSource, NO_CASSETTE_TAPE_TEXT, ((float) width / 2f), (((float) height - 10f) / 2f), OERenderUtils.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, i);
        }
    }

}
