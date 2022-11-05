package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.gui.components.FixedListWidget;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

public class JoinPlayListFixedListWidget extends PlayListFixedListWidget {
    public JoinPlayListFixedListWidget(int x, int y, @NotNull List<MusicPlayList> entryList, @Nullable PressEntry<MusicPlayList> onPressEntry, @Nullable FixedListWidget<MusicPlayList> old) {
        super(x, y, 368, 168, Component.translatable("imp.fixedList.joinPlaylist"), 6, entryList, onPressEntry, false, old);
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, MusicPlayList item, int lnum, int bnum, int bX, int bY, int mx, int my, float parTick, boolean selected) {
        int k = this.getYImage(this.isEntryHovered(bnum));
        drawSmartButtonBox(poseStack, bX, bY, getIndividualWidth(), getIndividualHeight(), k);

        var img = item.getImage();

        float sx = 1;

        if (!img.isEmpty()) {
            sx += getIndividualHeight() - 2 + 1;
            PlayImageRenderer.getInstance().draw(img, poseStack, bX + 1, bY + 1, getIndividualHeight() - 2);
        }

        drawSmartFixedWidthText(poseStack, Component.literal(item.getName()), bX + sx, bY + 3, 90);
        drawSmartFixedWidthText(poseStack, Component.literal(MyPlayListFixedListWidget.dateFormat.format(new Date(item.getCreateDate()))), bX + sx, bY + 17, 90);

        OERenderUtils.drawPlayerFace(poseStack, item.getAuthority().getOwnerName(), bX + sx + 101, bY + 2, 9);
        drawSmartFixedWidthText(poseStack, Component.literal(item.getAuthority().getOwnerName()), bX + sx + 112, bY + 3, 90);
        drawSmartFixedWidthText(poseStack, Component.translatable("imp.text.musicCount", item.getMusicList().size()), bX + sx + 101, bY + 17, 45);
        drawSmartFixedWidthText(poseStack, Component.translatable("imp.text.playerCount", item.getPlayerCount()), bX + sx + 156, bY + 17, 45);

        if (item.getAuthority().getAuthorityType(IIMPSmartRender.mc.player.getGameProfile().getId()).isInvitation()) {
            drawSmartFixedWidthText(poseStack, Component.translatable("imp.text.invitation"), bX + sx + 204, bY + 3, 100, 0xFF0000FF);
        }
    }
}
