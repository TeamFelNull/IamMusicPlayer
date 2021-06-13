package red.felnull.imp.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.client.gui.IMPFonts;
import red.felnull.imp.client.gui.screen.monitor.MSDBaseMonitor;
import red.felnull.imp.data.resource.AdministratorInformation;
import red.felnull.otyacraftengine.client.gui.components.FixedButtonsList;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AdminPlayersFixedButtonsList extends FixedButtonsList<AdminInfoData> implements IMSDSmartRender {
    public AdminPlayersFixedButtonsList(int x, int y, int w, int h, int num, Component name, List<AdminInfoData> list, Function<AdminInfoData, Component> listName, Consumer<PressState<AdminInfoData>> onPress) {
        super(x, y, w, h, num, name, list, listName, onPress);
    }

    @Override
    protected void renderOneButton(PoseStack poseStack, AdminInfoData item, int lnum, int bnum, int x, int y, int mx, int my, float parTick) {
        int k = this.getYImage(this.isHovered(bnum));
        drawSmartButtonBox(poseStack, x, y, getOneButtonWidth(), getOneButtonHeight(), k);
        drawPrettyString(poseStack, (MutableComponent) getMessage(lnum), x + 3 + getOneButtonHeight() - 2, y + ((float) this.getOneButtonHeight() - 8f) / 2f, 0);

        IKSGRenderUtil.drawPlayerFase(poseStack, item.playerInfo().getProfile().getName(), x + 1, y + 1, getOneButtonHeight() - 2);

        boolean red = item.type() == AdministratorInformation.AuthorityType.BAN;

        int zure = red ? 0 : 7;

        IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 1, y + 1, 0, 62 + zure, 7, 7);

        MutableComponent component = new TranslatableComponent("imp.msdAuthority." + item.type().getNmae()).withStyle(IMPFonts.FLOPDE_SIGN_FONT).withStyle(red ? ChatFormatting.RED : ChatFormatting.GREEN);
        int cw = getFont().width(component);
        drawPrettyString(poseStack, component, x + getOneButtonWidth() - 3 - cw, y + ((float) this.getOneButtonHeight() - 8f) / 2f, 0);

    }
}
