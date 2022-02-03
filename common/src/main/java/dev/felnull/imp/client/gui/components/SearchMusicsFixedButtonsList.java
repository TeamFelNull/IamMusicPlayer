package dev.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.client.gui.IIMPSmartRender;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.SearchMusicMMMonitor;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.List;

public class SearchMusicsFixedButtonsList extends FixedButtonsList<SearchMusicMMMonitor.SearchMusicEntry> implements IIMPSmartRender {
    public SearchMusicsFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<SearchMusicMMMonitor.SearchMusicEntry> list, PressEntry<SearchMusicMMMonitor.SearchMusicEntry> onPressEntry) {
        super(x, y, w, h, MusicManagerMonitor.WIDGETS_TEXTURE, 0, 20, 256, 256, num, name, list, n -> new TextComponent(n.name()), onPressEntry);
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, SearchMusicMMMonitor.SearchMusicEntry item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), this.getYImage(this.isHovered(bnum)));
        var img = item.imageInfo();
        float sx = 1;

        if (!img.isEmpty()) {
            sx += getOneButtonHeight() - 2 + 1;
            PlayImageRenderer.getInstance().draw(img, poseStack, x + 1, y + 1, getOneButtonHeight() - 2, false);
        }
        drawSmartFixedWidthText(poseStack, new TextComponent(item.name()), x + sx, y + 2, getOneButtonWidth() - sx - 2);
        drawSmartFixedWidthText(poseStack, new TextComponent(item.artist()), x + sx, y + 12, getOneButtonWidth() - sx - 2);

    }
}
