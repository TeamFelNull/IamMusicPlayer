package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import red.felnull.imp.client.gui.screen.monitor.MSDBaseMonitor;
import red.felnull.imp.client.renderer.PlayImageRenderer;
import red.felnull.imp.music.resource.simple.SimpleMusicPlayList;
import red.felnull.otyacraftengine.client.gui.components.FixedButtonsList;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayListFixedButtonsList extends FixedButtonsList<SimpleMusicPlayList> implements IMSDSmartRender {
    private final Function<SimpleMusicPlayList, Boolean> selected;

    public PlayListFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<SimpleMusicPlayList> list, Function<SimpleMusicPlayList, Component> listName, Consumer<PressState<SimpleMusicPlayList>> onPress, Function<SimpleMusicPlayList, Boolean> selected) {
        super(x, y, w, h, MSDBaseMonitor.MSD_WIDGETS, 0, 20, 256, 256, num, name, list, listName, onPress);
        this.selected = selected;
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, SimpleMusicPlayList item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHovered(bnum));
        if (selected.apply(item))
            k = 0;
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);
        PlayImageRenderer.getInstance().draw(item.getImage(), poseStack, x + 1, y + 1, getOneButtonHeight() - 2);
    }
}
