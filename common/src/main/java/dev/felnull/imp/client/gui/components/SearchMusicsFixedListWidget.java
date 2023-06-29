package dev.felnull.imp.client.gui.components;

import dev.felnull.imp.client.music.media.MusicMediaResult;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.otyacraftengine.client.gui.components.FixedListWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SearchMusicsFixedListWidget extends IMPBaseFixedListWidget<MusicMediaResult> {
    public SearchMusicsFixedListWidget(int x, int y, int width, int height, @NotNull Component message, int entryShowCount, @NotNull List<MusicMediaResult> entryList, @Nullable PressEntry<MusicMediaResult> onPressEntry, @Nullable FixedListWidget<MusicMediaResult> old) {
        super(x, y, width, height, message, entryShowCount, entryList, n -> Component.literal(n.name()), onPressEntry, false, old);
    }

    @Override
    protected void renderOneButton(GuiGraphics guiGraphics, MusicMediaResult item, int lnum, int bnum, int bX, int bY, int mx, int my, float parTick, boolean selected) {
        drawSmartButtonBox(guiGraphics, bX, bY, getIndividualWidth(), getIndividualHeight(), this.getYImage(this.isEntryHovered(bnum)));
        var img = item.imageInfo();
        float sx = 1;

        if (!img.isEmpty()) {
            sx += getIndividualHeight() - 2 + 1;
            PlayImageRenderer.getInstance().draw(img, guiGraphics.pose(), bX + 1, bY + 1, getIndividualHeight() - 2, false);
        }
        drawSmartFixedWidthText(guiGraphics, Component.literal(item.name()), bX + sx, bY + 2, getIndividualWidth() - sx - 2);
        drawSmartFixedWidthText(guiGraphics, Component.literal(item.author()), bX + sx, bY + 12, getIndividualWidth() - sx - 2);
    }
}
