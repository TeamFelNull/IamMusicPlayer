package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.StringTextComponent;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.List;

public class DetailsPlayersScrollButton extends ScrollListButton {
    private final List<String> playersLists;

    public DetailsPlayersScrollButton(int x, int y, int sizeX, int sizeY, int sizeOne, ScrollBarSlider scrollBar, List<String> playersLists, IPressable pressed) {
        super(x, y, sizeX, sizeY, sizeOne, 0, scrollBar, null, pressed);
        this.playersLists = playersLists;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 53, 112 + upOver, 175, 10 - downOver);
        String uuid = playersLists.get(num);

        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer font = minecraft.fontRenderer;

        IKSGRenderUtil.drawPlayerFaseByUUID(matrix, uuid, x + 1, y + 1);
        IKSGRenderUtil.drawString(font, matrix, new StringTextComponent("TEST"), x + 10, y + 1, 0);

    }

    @Override
    protected int getCont() {
        return this.playersLists.size();
    }
}
