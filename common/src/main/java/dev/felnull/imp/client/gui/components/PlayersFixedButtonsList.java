package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.UUID;

public class PlayersFixedButtonsList extends FixedButtonsList<UUID> implements IIMPSmartRender {
    public PlayersFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<UUID> list, PressEntry<UUID> onPressEntry) {
        super(x, y, w, h, MusicManagerMonitor.WIDGETS_TEXTURE, 0, 20, 256, 256, num, name, list, n -> {
            var str = OEClientUtils.getPlayerNameByUUID(n).orElseGet(n::toString);
            return Component.literal(str);
        }, onPressEntry);
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, UUID item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), this.getYImage(this.isHoveredOrFocused(bnum)));
        OERenderUtils.drawPlayerFace(poseStack, item, x + 1, y + 1, getOneButtonHeight() - 2);
        drawSmartFixedWidthText(poseStack, getMessage(lnum), x + getOneButtonHeight() + 1, y + (getOneButtonHeight() - 6.5f) / 2f, getOneButtonWidth() - 2 - getOneButtonHeight() - 1);
    }
}
