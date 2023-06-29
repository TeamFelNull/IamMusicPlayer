package dev.felnull.imp.client.gui.components;

import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.otyacraftengine.client.gui.components.FixedListWidget;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

public class MusicsFixedListWidget extends IMPBaseFixedListWidget<Music> {
    public static final Component UNKNOWN_PLAYER_TEXT = Component.translatable("imp.text.unknownPlayer");

    public MusicsFixedListWidget(int x, int y, int width, int height, @NotNull Component message, int entryShowCount, @NotNull List<Music> entryList, @Nullable PressEntry<Music> onPressEntry, boolean selectable, @Nullable FixedListWidget<Music> old) {
        super(x, y, width, height, message, entryShowCount, entryList, n -> Component.literal(n.getName()), onPressEntry, selectable, old);
    }

    /*@Override
    protected void renderOneButton(PoseStack poseStack, Music item, int lnum, int bnum, int bX, int bY, int mx, int my, float parTick, boolean selected) {
        drawSmartButtonBox(poseStack, bX, bY, getIndividualWidth(), getIndividualHeight(), this.getYImage(this.isEntryHovered(bnum)));
        var img = item.getImage();
        float sx = 2;

        if (!img.isEmpty()) {
            sx += getIndividualHeight() - 2 + 1;
            PlayImageRenderer.getInstance().draw(img, poseStack, bX + 1, bY + 1, getIndividualHeight() - 2, false);
        }

        drawSmartFixedWidthText(poseStack, Component.literal(item.getName()), bX + sx, bY + 2, getIndividualWidth() - sx - 2);
        drawSmartFixedWidthText(poseStack, Component.literal(item.getAuthor()), bX + sx, bY + 13, 90);
        OERenderUtils.drawPlayerFace(poseStack, item.getOwner(), bX + sx, bY + 23, 9);
        var pname = OEClientUtils.getPlayerNameByUUID(item.getOwner()).map(n -> (Component) Component.literal(n)).orElse(UNKNOWN_PLAYER_TEXT);
        drawSmartFixedWidthText(poseStack, pname, bX + sx + 12, bY + 24, 90);
        drawSmartFixedWidthText(poseStack, Component.literal(MyPlayListFixedListWidget.dateFormat.format(new Date(item.getCreateDate()))), bX + sx + 88 + 15, bY + 24, 90);
    }*/

    @Override
    protected void renderOneButton(GuiGraphics guiGraphics, Music item, int lnum, int bnum, int bX, int bY, int mx, int my, float parTick, boolean selected) {
        drawSmartButtonBox(guiGraphics, bX, bY, getIndividualWidth(), getIndividualHeight(), this.getYImage(this.isEntryHovered(bnum)));
        var img = item.getImage();
        float sx = 2;

        if (!img.isEmpty()) {
            sx += getIndividualHeight() - 2 + 1;
            PlayImageRenderer.getInstance().draw(img, guiGraphics.pose(), bX + 1, bY + 1, getIndividualHeight() - 2, false);
        }

        drawSmartFixedWidthText(guiGraphics, Component.literal(item.getName()), bX + sx, bY + 2, getIndividualWidth() - sx - 2);
        drawSmartFixedWidthText(guiGraphics, Component.literal(item.getAuthor()), bX + sx, bY + 13, 90);
        OERenderUtils.drawPlayerFace(guiGraphics.pose(), item.getOwner(), bX + sx, bY + 23, 9);
        var pname = OEClientUtils.getPlayerNameByUUID(item.getOwner()).map(n -> (Component) Component.literal(n)).orElse(UNKNOWN_PLAYER_TEXT);
        drawSmartFixedWidthText(guiGraphics, pname, bX + sx + 12, bY + 24, 90);
        drawSmartFixedWidthText(guiGraphics, Component.literal(MyPlayListFixedListWidget.dateFormat.format(new Date(item.getCreateDate()))), bX + sx + 88 + 15, bY + 24, 90);
    }
}
