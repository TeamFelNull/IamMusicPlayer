package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import red.felnull.imp.client.gui.screen.monitor.MSDBaseMonitor;
import red.felnull.imp.client.renderer.PlayImageRenderer;
import red.felnull.imp.music.resource.simple.SimpleMusicPlayList;
import red.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import red.felnull.otyacraftengine.client.util.IKSGClientUtil;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PublishedPlayListFixedButtonsList extends FixedButtonsList<SimpleMusicPlayList> implements IMSDSmartRender {
    public PublishedPlayListFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<SimpleMusicPlayList> list, Function<SimpleMusicPlayList, Component> listName, Consumer<PressState<SimpleMusicPlayList>> onPress) {
        super(x, y, w, h, MSDBaseMonitor.MSD_WIDGETS, 0, 20, 256, 256, num, name, list, listName, onPress);
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, SimpleMusicPlayList item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHovered(bnum));
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);
        PlayImageRenderer.getInstance().draw(item.getImage(), poseStack, x + 1, y + 1, getOneButtonHeight() - 2);
        drawPrettyString(poseStack, (MutableComponent) getMessage(lnum), x + getOneButtonHeight() + 1, y + 1, 0);

        String plName = IKSGClientUtil.getPlayerNameByUUID(item.getOwner());
        if (IKSGPlayerUtil.getFakePlayerName().equals(plName))
            plName = item.getOwner().toString();

        IKSGRenderUtil.drawPlayerFace(poseStack, item.getOwner(), x + getOneButtonHeight() + 1, (int) (y + getOneButtonHeight() / 2f + 1));
        drawPrettyString(poseStack, new TextComponent(plName), x + getOneButtonHeight() + 10, (int) (y + getOneButtonHeight() / 2f) + 1, 0);

    }
}
