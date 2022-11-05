package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.music.media.IMPMusicMedias;
import dev.felnull.otyacraftengine.client.gui.components.FixedListWidget;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class MusicLoaderTypesFixedListWidget extends IMPBaseFixedListWidget<String> {
    public static final ResourceLocation UPLOAD_ICON = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/loader_types/upload.png");
    private final Function<String, Boolean> selected;

    public MusicLoaderTypesFixedListWidget(int x, int y, int width, int height, @NotNull Component message, int entryShowCount, @NotNull List<String> entryList, @Nullable PressEntry<String> onPressEntry, @Nullable FixedListWidget<String> old, Function<String, Boolean> selected) {
        super(x, y, width, height, message, entryShowCount, entryList, n -> {
            var lt = IMPMusicMedias.getMedia(n);
            return lt == null ? Component.translatable("imp.loaderType." + n) : lt.getMediaName();
        }, onPressEntry, false, old);
        this.selected = selected;
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, String item, int lnum, int bnum, int bX, int bY, int mx, int my, float parTick, boolean selected) {
        drawSmartButtonBox(poseStack, bX, bY, getIndividualWidth(), getIndividualHeight(), this.selected.apply(item) ? 0 : this.getYImage(this.isEntryHovered(bnum)));
        var type = IMPMusicMedias.getMedia(item);
        int tx = bX + 2;
        if ((type != null && type.getIcon() != null) || "upload".equals(item)) {
            var icon = type != null ? type.getIcon() : UPLOAD_ICON;
            OERenderUtils.drawTexture(icon, poseStack, bX + 1, bY + 1, 0, 0, getIndividualHeight() - 2, getIndividualHeight() - 2, getIndividualHeight() - 2, getIndividualHeight() - 2);
            tx += getIndividualHeight() - 1;
        }
        drawSmartFixedWidthText(poseStack, getMessage(lnum), tx, bY + (getIndividualHeight() - 6.5f) / 2f, getIndividualWidth() - 2 - getIndividualHeight() - 1);
    }
}
