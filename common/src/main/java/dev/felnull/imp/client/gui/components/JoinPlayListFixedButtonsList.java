package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Date;
import java.util.List;

public class JoinPlayListFixedButtonsList extends PlayListFixedButtonsList {
    public JoinPlayListFixedButtonsList(int x, int y, List<MusicPlayList> list, PressEntry<MusicPlayList> onPressEntry) {
        super(x, y, 368, 168, 6, new TranslatableComponent("imp.fixedList.joinPlaylist"), list, onPressEntry);
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, MusicPlayList item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHovered(bnum));
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);

        var img = item.getImage();

        float sx = 1;

        if (!img.isEmpty()) {
            sx += getOneButtonHeight() - 2 + 1;
            PlayImageRenderer.getInstance().draw(img, poseStack, x + 1, y + 1, getOneButtonHeight() - 2);
        }

        drawSmartFixedWidthString(poseStack, new TextComponent(item.getName()), x + sx, y + 3, 90);
        drawSmartFixedWidthString(poseStack, new TextComponent(MyPlayListFixedButtonsList.dateFormat.format(new Date(item.getCreateDate()))), x + sx, y + 17, 90);

        OERenderUtil.drawPlayerFace(poseStack, item.getAuthority().getOwnerName(), x + sx + 101, y + 2, 9);
        drawSmartFixedWidthString(poseStack, new TextComponent(item.getAuthority().getOwnerName()), x + sx + 112, y + 3, 90);
        drawSmartFixedWidthString(poseStack, new TranslatableComponent("imp.text.musicCount", item.getMusicList().size()), x + sx + 101, y + 17, 45);
        drawSmartFixedWidthString(poseStack, new TranslatableComponent("imp.text.playerCount", item.getPlayerCount()), x + sx + 156, y + 17, 45);

        if (item.getAuthority().getAuthorityType(mc.player.getGameProfile().getId()).isInvitation()) {
            drawSmartFixedWidthString(poseStack, new TranslatableComponent("imp.text.invitation"), x + sx + 204, y + 3, 100, 0xFF0000FF);
        }
    }
}
