package dev.felnull.imp.client.gui.components;

import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.gui.components.FixedListWidget;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class WritePlayListFixedListWidget extends PlayListFixedListWidget {
    private final Function<MusicPlayList, Boolean> selected;

    public WritePlayListFixedListWidget(int x, int y, @NotNull List<MusicPlayList> entryList, @Nullable PressEntry<MusicPlayList> onPressEntry, @Nullable FixedListWidget<MusicPlayList> old, Function<MusicPlayList, Boolean> selected) {
        super(x, y, 68, 42, Component.translatable("imp.fixedList.myPlaylist"), 6, entryList, onPressEntry, false, old);
        this.selected = selected;
    }

    @Override
    protected void renderOneButton(GuiGraphics guiGraphics, MusicPlayList item, int lnum, int bnum, int bX, int bY, int mx, int my, float parTick, boolean selected) {
        int k = this.getYImage(this.isEntryHovered(bnum));
        if (this.selected.apply(item))
            k = 0;
        drawSmartButtonBox(guiGraphics, bX, bY, getIndividualWidth(), getIndividualHeight(), k);
        float sx = 1;
        var img = item.getImage();
        if (!img.isEmpty()) {
            sx += getIndividualHeight() - 2 + 1;
            PlayImageRenderer.getInstance().draw(img, guiGraphics.pose(), bX + 1, bY + 1, getIndividualHeight() - 2);
        }
        guiGraphics.pose().pushPose();
        float sc = 0.7f;
        OERenderUtils.poseScaleAll(guiGraphics.pose(), sc);
        drawSmartText(guiGraphics, Component.literal(OEClientUtils.getWidthOmitText(item.getName(), getIndividualWidth() - sx - 2 + 20, "...")), (bX + sx) / sc, (bY + 1f) / sc);
        guiGraphics.pose().popPose();
    }
}
