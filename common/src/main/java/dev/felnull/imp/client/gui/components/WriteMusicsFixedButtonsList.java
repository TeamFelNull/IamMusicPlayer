package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;
import java.util.function.Function;

public class WriteMusicsFixedButtonsList extends MusicsFixedButtonsList {
    private final Function<Music, Boolean> selected;

    public WriteMusicsFixedButtonsList(int x, int y, List<Music> list, PressEntry<Music> onPressEntry, Function<Music, Boolean> selected) {
        super(x, y, 129, 42, 6, new TranslatableComponent("imp.fixedList.myMusic"), list, onPressEntry);
        this.selected = selected;
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, Music item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHovered(bnum));
        if (selected.apply(item))
            k = 0;
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);

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
