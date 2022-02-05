package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.util.IMPItemUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class PlayingBMonitor extends BoomboxMonitor {
    protected static final ResourceLocation PLAYING_BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/playing.png");
    protected static final ResourceLocation PLAYING_NO_IMAGE_BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/playing_noimage.png");
    private static final Component NO_ANTENNA_TEXT = new TranslatableComponent("imp.text.noAntenna");
    private static final Component NO_CASSETTE_TAPE_TEXT = new TranslatableComponent("imp.text.noCassetteTape");
    private static final Component NO_MUSIC_CASSETTE_TAPE_TEXT = new TranslatableComponent("imp.text.noMusicCassetteTape");

    public PlayingBMonitor(BoomboxBlockEntity.MonitorType monitorType, BoomboxScreen screen) {
        super(monitorType, screen);
    }

    @Override
    public void init(int leftPos, int topPos) {
        super.init(leftPos, topPos);
        getScreen().lastNoAntenna = 0;
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        if (!getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape())) {
            Music music = CassetteTapeItem.getMusic(getCassetteTape());
            if (music != null) {
                OERenderUtil.drawTexture(music.getImage().isEmpty() ? PLAYING_NO_IMAGE_BG_TEXTURE : PLAYING_BG_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
                int sx = 2;
                if (!music.getImage().isEmpty()) {
                    getPlayImageRenderer().draw(music.getImage(), poseStack, getStartX() + 1, getStartY() + 1, height - 2);
                    sx += height - 2;
                }
                drawSmartCenterText(poseStack, new TextComponent(OERenderUtil.getWidthString(music.getName(), width - sx - 2, "...")), getStartX() + sx + (width - sx - 2f) / 2f, getStartY() + 3);
            } else {
                drawSmartCenterText(poseStack, NO_MUSIC_CASSETTE_TAPE_TEXT, getStartX() + width / 2f, getStartY() + (height - 10f) / 2f);
            }
        } else {
            drawSmartCenterText(poseStack, NO_CASSETTE_TAPE_TEXT, getStartX() + width / 2f, getStartY() + (height - 10f) / 2f);
        }

        long noAntennaTime = System.currentTimeMillis() - getScreen().lastNoAntenna;
        if (noAntennaTime <= 3000) {
            float alpha = Math.min(3f - ((float) noAntennaTime / 3000f) * 3f, 1f);
            int ad = (int) ((float) 0xff * alpha);
            float fl = mc.font.width(NO_ANTENNA_TEXT) + 6f;
            float st = ((float) width - fl) / 2f;
            float sy = ((float) height - 10f) / 2f;
            drawFill(poseStack, (int) (getStartX() + st), (int) (getStartY() + sy), (int) fl, 10, 0xa9a9a9 | (ad << 24));
            drawSmartText(poseStack, NO_ANTENNA_TEXT, getStartX() + st + 3, getStartY() + sy + 1f, (Math.max(ad, 5) << 24));
        }
    }
}
