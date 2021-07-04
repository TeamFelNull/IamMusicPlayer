package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import red.felnull.imp.client.data.SimplePlayerData;
import red.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.List;
import java.util.function.Consumer;

public class PlayersFixedButtonsList extends FixedButtonsList<SimplePlayerData> implements IMSDSmartRender {
    public PlayersFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<SimplePlayerData> list, Consumer<PressState<SimplePlayerData>> onPress) {
        super(x, y, w, h, num, name, list, n -> new TextComponent(n.name()), onPress);
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, SimplePlayerData item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHovered(bnum));
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);
        drawPrettyString(poseStack, (MutableComponent) getMessage(lnum), x + 3 + getOneButtonHeight() - 2, y + ((float) this.getOneButtonHeight() - 8f) / 2f, 0);

        IKSGRenderUtil.drawPlayerFase(poseStack, item.name(), x + 1, y + 1, getOneButtonHeight() - 2);
    }
}
