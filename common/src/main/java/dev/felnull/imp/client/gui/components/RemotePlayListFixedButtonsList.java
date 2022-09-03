package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Function;

public class RemotePlayListFixedButtonsList extends PlayListFixedButtonsList {
    private final Function<MusicPlayList, Boolean> selected;

    public RemotePlayListFixedButtonsList(int x, int y, List<MusicPlayList> list, PressEntry<MusicPlayList> onPressEntry, Function<MusicPlayList, Boolean> selected) {
        super(x, y, 68, 35, 5, Component.translatable("imp.fixedList.myPlaylist"), list, onPressEntry);
        this.selected = selected;
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, MusicPlayList item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHoveredOrFocused(bnum));
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
        OERenderUtils.poseScaleAll(poseStack, sc);
        drawSmartText(poseStack, Component.literal(OEClientUtils.getWidthOmitText(item.getName(), getOneButtonWidth() - sx - 2 + 20, "...")), (x + sx) / sc, (y + 1f) / sc);
        poseStack.popPose();
    }
}
