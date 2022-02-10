package dev.felnull.imp.client.gui.screen.monitor.boombox;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.utils.value.IntValue;
import dev.felnull.fnjl.util.FNStringUtil;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.gui.components.VolumeWidget;
import dev.felnull.imp.client.gui.screen.BoomboxScreen;
import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.imp.util.IMPItemUtil;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PlayingBMonitor extends BoomboxMonitor {
    protected static final ResourceLocation PLAYING_BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/playing.png");
    protected static final ResourceLocation PLAYING_IMAGE_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/monitor/playing_image.png");
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

        this.addRenderWidget(new VolumeWidget(getStartX() + 168, getStartY() + 14, new IntValue() {
            @Override
            public void accept(int value) {
                setVolume(value);
            }

            @Override
            public int getAsInt() {
                return getScreen().getVolume();
            }
        }, () -> false));
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);
        if (!getCassetteTape().isEmpty() && IMPItemUtil.isCassetteTape(getCassetteTape())) {
            Music music = CassetteTapeItem.getMusic(getCassetteTape());
            if (music != null) {
                OERenderUtil.drawTexture(PLAYING_BG_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
                if (!music.getImage().isEmpty())
                    OERenderUtil.drawTexture(PLAYING_IMAGE_TEXTURE, poseStack, getStartX(), getStartY(), 0f, 0f, width, height, width, height);
                int sx = 2;
                if (!music.getImage().isEmpty()) {
                    getPlayImageRenderer().draw(music.getImage(), poseStack, getStartX() + 1, getStartY() + 1, height - 2);
                    sx += height - 2;
                }
                drawSmartCenterText(poseStack, new TextComponent(OERenderUtil.getWidthString(music.getName(), width - sx - 2, "...")), getStartX() + sx + (width - sx - 2f) / 2f, getStartY() + 3);

                drawSmartText(poseStack, new TextComponent(FNStringUtil.getTimeProgress(getScreen().getMusicPosition(), music.getSource().getDuration())), getStartX() + 38, getStartY() + 14, 0XFF115D0E);


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

    @Override
    public void renderAppearance(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, float f, float monitorWidth, float monitorHeight, ItemStack cassetteTape) {
        super.renderAppearance(poseStack, multiBufferSource, i, j, f, monitorWidth, monitorHeight, cassetteTape);
        float onPxW = monitorWidth / (float) width;
        float onPxH = monitorHeight / (float) height;
        if (!cassetteTape.isEmpty() && IMPItemUtil.isCassetteTape(cassetteTape)) {
            Music music = CassetteTapeItem.getMusic(cassetteTape);
            if (music != null) {
                OERenderUtil.renderTextureSprite(PLAYING_BG_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 2, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);
                if (!music.getImage().isEmpty())
                    OERenderUtil.renderTextureSprite(PLAYING_IMAGE_TEXTURE, poseStack, multiBufferSource, 0, 0, OERenderUtil.MIN_BREADTH * 3, 0, 0, 0, monitorWidth, monitorHeight, 0, 0, width, height, width, height, i, j);

                int sx = 2;
                if (!music.getImage().isEmpty()) {
                    getPlayImageRenderer().renderSprite(music.getImage(), poseStack, multiBufferSource, 1 * onPxW, monitorHeight - (1 + height - 2) * onPxH, OERenderUtil.MIN_BREADTH * 4, (height - 3) * onPxH, i, j);
                    sx += height - 2;
                }
                renderSmartCenterTextSprite(poseStack, multiBufferSource, new TextComponent(OERenderUtil.getWidthString(music.getName(), width - sx - 2, "...")), sx + (width - sx - 2f) / 2f, 3, OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, 2, i);
            } else {
                renderSmartCenterTextSprite(poseStack, multiBufferSource, NO_MUSIC_CASSETTE_TAPE_TEXT, ((float) width / 2f), (((float) height - 10f) / 2f), OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, 2, i);
            }
        } else {
            renderSmartCenterTextSprite(poseStack, multiBufferSource, NO_CASSETTE_TAPE_TEXT, ((float) width / 2f), (((float) height - 10f) / 2f), OERenderUtil.MIN_BREADTH * 2, onPxW, onPxH, monitorHeight, 2, i);
        }
    }

    private void setVolume(int volume) {
        getScreen().insVolume(volume);
    }
}
