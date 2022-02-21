package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class RemoteMusicsFixedButtonsList extends MusicsFixedButtonsList {

    public RemoteMusicsFixedButtonsList(int x, int y, List<Music> list, PressEntry<Music> onPressEntry) {
        super(x, y, 129, 35, 5, new TranslatableComponent("imp.fixedList.myMusic"), list, onPressEntry);
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, Music item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), this.getYImage(this.isHovered(bnum)));

        float sx = 1;
        var img = item.getImage();
        if (!img.isEmpty()) {
            sx += getOneButtonHeight() - 2 + 1;
            PlayImageRenderer.getInstance().draw(img, poseStack, x + 1, y + 1, getOneButtonHeight() - 2);
        }

        poseStack.pushPose();
        float sc = 0.7f;
        OERenderUtil.poseScaleAll(poseStack, sc);
        drawSmartText(poseStack, new TextComponent(OERenderUtil.getWidthString(item.getName(), getOneButtonWidth() - sx - 2 + 47, "...")), (x + sx) / sc, (y + 1f) / sc);
        poseStack.popPose();
    }
}
