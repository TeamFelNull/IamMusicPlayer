package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.MusicPlayList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class MyPlayListFixedButtonsList extends PlayListFixedButtonsList {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private final Function<MusicPlayList, Boolean> selected;

    public MyPlayListFixedButtonsList(int x, int y, List<MusicPlayList> list, PressEntry<MusicPlayList> onPressEntry, Function<MusicPlayList, Boolean> selected) {
        super(x, y, 100, 168, 8, new TranslatableComponent("imp.fixedList.myPlaylist"), list, onPressEntry);
        this.selected = selected;
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, MusicPlayList item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHovered(bnum));
        if (selected.apply(item))
            k = 0;
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);

        var img = item.getImage();

        float sx = 1;

        if (!img.isEmpty()) {
            sx += getOneButtonHeight() - 2 + 1;
            PlayImageRenderer.getInstance().draw(img, poseStack, x + 1, y + 1, getOneButtonHeight() - 2);
        }

        drawSmartFixedWidthString(poseStack, new TextComponent(item.getName()), x + sx, y + 2, getOneButtonWidth() - sx - 2);
        drawSmartFixedWidthString(poseStack, new TextComponent(dateFormat.format(new Date(item.getCreateDate()))), x + sx, y + 12, getOneButtonWidth() - sx - 2);
    }


}
