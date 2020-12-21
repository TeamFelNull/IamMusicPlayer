package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import red.felnull.imp.client.gui.screen.IMPAbstractEquipmentScreen;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

import java.util.List;

public class JoinPlayListScrollButton extends ScrollListButton {
    private final List<PlayList> playLists;

    public JoinPlayListScrollButton(int x, int y, int sizeX, int sizeY, int sizeOne, ScrollBarSlider scrollBar, List<PlayList> playlists, IPressable pressed) {
        super(x, y, sizeX, sizeY, sizeOne, 0, scrollBar, null, pressed);
        this.playLists = playlists;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 48, upOver, 187, 40 - downOver, 256, 256);

        PlayList pl = playLists.get(num);
        int upzure = 2 < upOver ? upOver - 2 : 0;
        int downzure = 2 < downOver ? downOver - 2 : 0;
        RenderUtil.drwPlayImage(matrix, pl.getImage(), x + 2, y + 2, 36, upzure, downzure);
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        if (upOver < 12 && downOver <= 38) {
            IKSGRenderUtil.drawString(fontrenderer, matrix, IKSGStyles.withStyle(new StringTextComponent(pl.getName()),  IMPAbstractEquipmentScreen.smart_fontStyle), x + 40, y + 2, 0);
        }
        if (upOver < 24 && downOver <= 26) {
            IKSGRenderUtil.drawString(fontrenderer, matrix, IKSGStyles.withStyle(new StringTextComponent(pl.getTimeStamp()),  IMPAbstractEquipmentScreen.smart_fontStyle), x + 40, y + 14, 0);
        }
        if (upOver < 37 && downOver <= 13) {
            IKSGRenderUtil.drawString(fontrenderer, matrix, IKSGStyles.withStyle(new StringTextComponent(pl.getCreatePlayerName()),  IMPAbstractEquipmentScreen.smart_fontStyle), x + 50, y + 27, 0);
        }
        int fupzure = 27 < upOver ? upOver - 27 : 0;
        int fdownzure = 5 < downOver ? downOver - 5 : 0;
        IKSGRenderUtil.matrixPush(matrix);
        ResourceLocation plskin = IKSGTextureUtil.getPlayerSkinTexture(pl.getCreatePlayerName());
        IKSGRenderUtil.guiBindAndBlit(plskin, matrix, x + 40, y + 27 + fupzure, 8, 8 + fupzure, 8, 8 - fupzure - fdownzure, 64, 64);
        IKSGRenderUtil.guiBindAndBlit(plskin, matrix, x + 40, y + 27 + fupzure, 40, 8 + fupzure, 8, 8 - fupzure - fdownzure, 64, 64);
        IKSGRenderUtil.matrixPop(matrix);


    }

    @Override
    protected int getCont() {
        return this.playLists.size();
    }

}
