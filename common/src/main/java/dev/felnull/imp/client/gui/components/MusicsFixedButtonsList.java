package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.network.chat.Component;

import java.util.Date;
import java.util.List;

public class MusicsFixedButtonsList extends FixedButtonsList<Music> implements IIMPSmartRender {
    public static final Component UNKNOWN_PLAYER_TEXT = Component.translatable("imp.text.unknownPlayer");

    public MusicsFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<Music> list, PressEntry<Music> onPressEntry) {
        super(x, y, w, h, MusicManagerMonitor.WIDGETS_TEXTURE, 0, 20, 256, 256, num, name, list, n -> Component.literal(n.getName()), onPressEntry);
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, Music item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), this.getYImage(this.isHoveredOrFocused(bnum)));
        var img = item.getImage();
        float sx = 2;

        if (!img.isEmpty()) {
            sx += getOneButtonHeight() - 2 + 1;
            PlayImageRenderer.getInstance().draw(img, poseStack, x + 1, y + 1, getOneButtonHeight() - 2, false);
        }

        drawSmartFixedWidthText(poseStack, Component.literal(item.getName()), x + sx, y + 2, getOneButtonWidth() - sx - 2);
        drawSmartFixedWidthText(poseStack, Component.literal(item.getAuthor()), x + sx, y + 13, 90);
        OERenderUtils.drawPlayerFace(poseStack, item.getOwner(), x + sx, y + 23, 9);
        var pname = OEClientUtils.getPlayerNameByUUID(item.getOwner()).map(n -> (Component) Component.literal(n)).orElse(UNKNOWN_PLAYER_TEXT);
        drawSmartFixedWidthText(poseStack, pname, x + sx + 12, y + 24, 90);
        drawSmartFixedWidthText(poseStack, Component.literal(MyPlayListFixedButtonsList.dateFormat.format(new Date(item.getCreateDate()))), x + sx + 88 + 15, y + 24, 90);

    }
}
