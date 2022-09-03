package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class MemberPlayersFixedButtonsList extends PlayersFixedButtonsList {
    private final Supplier<MusicPlayList> playListSupplier;

    public MemberPlayersFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<UUID> list, PressEntry<UUID> onPressEntry, Supplier<MusicPlayList> playListSupplier) {
        super(x, y, w, h, num, name, list, onPressEntry);
        this.playListSupplier = playListSupplier;
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, UUID item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), this.getYImage(this.isHoveredOrFocused(bnum)));
        OERenderUtils.drawPlayerFace(poseStack, item, x + 1, y + 1, getOneButtonHeight() - 2);
        drawSmartFixedWidthText(poseStack, getMessage(lnum), x + getOneButtonHeight() + 1, y + 2f, getOneButtonWidth() - 2 - getOneButtonHeight() - 1);
        var mp = playListSupplier.get();
        if (mp != null) {
            drawSmartFixedWidthText(poseStack, mp.getAuthority().getAuthorityType(item).getText(), x + getOneButtonHeight() + 1, y + 11f, getOneButtonWidth() - 2 - getOneButtonHeight() - 1);
        }
    }
}
