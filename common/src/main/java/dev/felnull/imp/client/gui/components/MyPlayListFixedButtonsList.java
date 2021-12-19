package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.MusicPlayList;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Function;

public class MyPlayListFixedButtonsList extends PlayListFixedButtonsList {
    private final Function<MusicPlayList, Boolean> selected;

    public MyPlayListFixedButtonsList(int x, int y, Component name, List<MusicPlayList> list, PressEntry<MusicPlayList> onPressEntry, Function<MusicPlayList, Boolean> selected) {
        super(x, y, 100, 168, 8, name, list, onPressEntry);
        this.selected = selected;
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, MusicPlayList item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHovered(bnum));
        if (selected.apply(item))
            k = 0;
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);

        var img = item.getImage();
        if (!img.isEmpty()) {
            PlayImageRenderer.getInstance().draw(img, poseStack, x + 1, y + 1, getOneButtonHeight() - 2);
        }
    }
}
