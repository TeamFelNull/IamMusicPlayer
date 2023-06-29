package dev.felnull.imp.client.gui.components;

import dev.felnull.otyacraftengine.client.gui.components.FixedListWidget;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class AuthorityPlayersFixedListWidget extends PlayersFixedListWidget {
    private final Function<UUID, Boolean> selected;

    public AuthorityPlayersFixedListWidget(int x, int y, int width, int height, @NotNull Component message, int entryShowCount, @NotNull List<UUID> entryList, @Nullable PressEntry<UUID> onPressEntry, @Nullable FixedListWidget<UUID> old, Function<UUID, Boolean> selected) {
        super(x, y, width, height, message, entryShowCount, entryList, onPressEntry, old);
        this.selected = selected;
    }

   /* @Override
    protected void renderOneButton(PoseStack poseStack, UUID item, int lnum, int bnum, int bX, int bY, int mx, int my, float parTick, boolean selected) {
        int k = this.getYImage(this.isEntryHovered(bnum));
        if (this.selected.apply(item))
            k = 0;
        drawSmartButtonBox(poseStack, bX, bY, getIndividualWidth(), getIndividualHeight(), k);
        OERenderUtils.drawPlayerFace(poseStack, item, bX + 1, bY + 1, getIndividualHeight() - 2);
        drawSmartFixedWidthText(poseStack, getMessage(lnum), bX + getIndividualHeight() + 1, bY + (getIndividualHeight() - 6.5f) / 2f, getIndividualWidth() - 2 - getIndividualHeight() - 1);
    }*/

    @Override
    protected void renderOneButton(GuiGraphics guiGraphics, UUID item, int lnum, int bnum, int bX, int bY, int mx, int my, float parTick, boolean selected) {
        int k = this.getYImage(this.isEntryHovered(bnum));
        if (this.selected.apply(item))
            k = 0;
        drawSmartButtonBox(guiGraphics, bX, bY, getIndividualWidth(), getIndividualHeight(), k);
        OERenderUtils.drawPlayerFace(guiGraphics.pose(), item, bX + 1, bY + 1, getIndividualHeight() - 2);
        drawSmartFixedWidthText(guiGraphics, getMessage(lnum), bX + getIndividualHeight() + 1, bY + (getIndividualHeight() - 6.5f) / 2f, getIndividualWidth() - 2 - getIndividualHeight() - 1);
    }
}
