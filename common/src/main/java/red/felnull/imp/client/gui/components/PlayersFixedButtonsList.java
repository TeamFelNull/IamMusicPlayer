package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import red.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.List;
import java.util.function.Consumer;

public class PlayersFixedButtonsList extends FixedButtonsList<PlayerInfo> implements IMSDSmartRender {
    public PlayersFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<PlayerInfo> list, Consumer<PressState<PlayerInfo>> onPress) {
        super(x, y, w, h, num, name, list, n -> new TextComponent(n.getProfile().getName()), onPress);
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, PlayerInfo item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHovered(bnum));
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);
        drawPrettyString(poseStack, (MutableComponent) getMessage(lnum), x + 3 + getOneButtonHeight() - 2, y + ((float) this.getOneButtonHeight() - 8f) / 2f, 0);

        IKSGRenderUtil.drawPlayerFase(poseStack, item.getProfile().getName(), x + 1, y + 1, getOneButtonHeight() - 2);
    }
}
