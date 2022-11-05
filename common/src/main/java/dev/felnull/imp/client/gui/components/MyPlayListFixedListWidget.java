package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.music.resource.MusicPlayList;
import dev.felnull.otyacraftengine.client.gui.components.FixedListWidget;
import dev.felnull.otyacraftengine.client.util.OEClientUtils;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class MyPlayListFixedListWidget extends PlayListFixedListWidget {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private final Function<MusicPlayList, Boolean> selected;

    public MyPlayListFixedListWidget(int x, int y, @NotNull List<MusicPlayList> entryList, @Nullable PressEntry<MusicPlayList> onPressEntry, @Nullable FixedListWidget<MusicPlayList> old, Function<MusicPlayList, Boolean> selected) {
        super(x, y, 100, 168, Component.translatable("imp.fixedList.myPlaylist"), 8, entryList, onPressEntry, false, old);
        this.selected = selected;
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, MusicPlayList item, int lnum, int bnum, int bX, int bY, int mx, int my, float parTick, boolean selected) {
        int k = this.getYImage(this.isEntryHovered(bnum));
        if (this.selected.apply(item))
            k = 0;
        drawSmartButtonBox(poseStack, bX, bY, getIndividualWidth(), getIndividualHeight(), k);

        var img = item.getImage();

        float sx = 1;

        if (!img.isEmpty()) {
            sx += getIndividualHeight() - 2 + 1;
            PlayImageRenderer.getInstance().draw(img, poseStack, bX + 1, bY + 1, getIndividualHeight() - 2);
        }

        drawSmartText(poseStack, Component.literal(OEClientUtils.getWidthOmitText(item.getName(), getIndividualWidth() - sx - 2, "...")), bX + sx, bY + 2);
        drawSmartFixedWidthText(poseStack, Component.literal(dateFormat.format(new Date(item.getCreateDate()))), bX + sx, bY + 12, getIndividualWidth() - sx - 2);
    }
}
