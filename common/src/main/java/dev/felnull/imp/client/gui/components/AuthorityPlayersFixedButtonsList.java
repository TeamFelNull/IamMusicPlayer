package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class AuthorityPlayersFixedButtonsList extends PlayersFixedButtonsList {
    private final Function<UUID, Boolean> selected;

    public AuthorityPlayersFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<UUID> list, PressEntry<UUID> onPressEntry, Function<UUID, Boolean> selected) {
        super(x, y, w, h, num, name, list, onPressEntry);
        this.selected = selected;
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, UUID item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHoveredOrFocused(bnum));
        if (selected.apply(item))
            k = 0;
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);
        OERenderUtils.drawPlayerFace(poseStack, item, x + 1, y + 1, getOneButtonHeight() - 2);
        drawSmartFixedWidthText(poseStack, getMessage(lnum), x + getOneButtonHeight() + 1, y + (getOneButtonHeight() - 6.5f) / 2f, getOneButtonWidth() - 2 - getOneButtonHeight() - 1);
    }
}
