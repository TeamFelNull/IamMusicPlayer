package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.otyacraftengine.client.gui.components.FixedListWidget;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class PlayersFixedListWidget extends IMPBaseFixedListWidget<UUID> {
    public PlayersFixedListWidget(int x, int y, int width, int height, @NotNull Component message, int entryShowCount, @NotNull List<UUID> entryList, @Nullable PressEntry<UUID> onPressEntry, @Nullable FixedListWidget<UUID> old) {
        super(x, y, width, height, message, entryShowCount, entryList, n -> {
            var str = OEClientUtils.getPlayerNameByUUID(n).orElseGet(n::toString);
            return Component.literal(str);
        }, onPressEntry, false, old);
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, UUID item, int lnum, int bnum, int bX, int bY, int mx, int my, float parTick, boolean selected) {
        drawSmartButtonBox(poseStack, bX, bY, getIndividualWidth(), getIndividualHeight(), this.getYImage(this.isEntryHovered(bnum)));
        OERenderUtils.drawPlayerFace(poseStack, item, bX + 1, bY + 1, getIndividualHeight() - 2);
        drawSmartFixedWidthText(poseStack, getMessage(lnum), bX + getIndividualHeight() + 1, bY + (getIndividualHeight() - 6.5f) / 2f, getIndividualWidth() - 2 - getIndividualHeight() - 1);
    }
}
