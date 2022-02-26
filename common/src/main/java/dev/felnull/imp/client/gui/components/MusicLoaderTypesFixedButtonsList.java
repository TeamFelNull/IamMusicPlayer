package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.music.loadertypes.IMPMusicLoaderTypes;
import dev.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Function;

public class MusicLoaderTypesFixedButtonsList extends FixedButtonsList<String> implements IIMPSmartRender {
    public static final ResourceLocation UPLOAD_ICON = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_manager/loader_types/upload.png");
    private final Function<String, Boolean> selected;

    public MusicLoaderTypesFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<String> list, PressEntry<String> onPressEntry, Function<String, Boolean> selected) {
        super(x, y, w, h, MusicManagerMonitor.WIDGETS_TEXTURE, 0, 20, 256, 256, num, name, list, n -> {
            var lt = IMPMusicLoaderTypes.getMusicLoaderTypes().get(n);
            return lt == null ? new TranslatableComponent("imp.loaderType." + n) : lt.getName();
        }, onPressEntry);
        this.selected = selected;
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, String item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), selected.apply(item) ? 0 : this.getYImage(this.isHoveredOrFocused(bnum)));
        var type = IMPMusicLoaderTypes.getMusicLoaderTypes().get(item);
        int tx = x + 2;
        if ((type != null && type.getIcon() != null) || "upload".equals(item)) {
            var icon = type != null ? type.getIcon() : UPLOAD_ICON;
            OERenderUtil.drawTexture(icon, poseStack, x + 1, y + 1, 0, 0, getOneButtonHeight() - 2, getOneButtonHeight() - 2, getOneButtonHeight() - 2, getOneButtonHeight() - 2);
            tx += getOneButtonHeight() - 1;
        }
        drawSmartFixedWidthText(poseStack, getMessage(lnum), tx, y + (getOneButtonHeight() - 6.5f) / 2f, getOneButtonWidth() - 2 - getOneButtonHeight() - 1);
    }
}
