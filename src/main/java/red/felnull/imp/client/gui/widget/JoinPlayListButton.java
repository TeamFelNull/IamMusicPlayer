package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.musicplayer.PlayList;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.List;

public class JoinPlayListButton extends ScrollListButton {
    private final List<PlayList> playLists;

    public JoinPlayListButton(int x, int y, int sizeX, int sizeY, int sizeOne, ScrollBarSlider scrollBar, List<PlayList> playlists, IPressable pressed) {
        super(x, y, sizeX, sizeY, sizeOne, 0, scrollBar, null, pressed);
        this.playLists = playlists;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 48, upOver, 187, 40 - downOver, 256, 256);

        PlayList pl = playLists.get(num);
        /*    int size = 36;
        float w = pl.getImageWidth();
        float h = pl.getImageHeight();
        int aw = 0;
        int ah = 0;
        if (w == h) {
            aw = size;
            ah = size;
        } else if (w > h) {
            aw = size;
            ah = (int) ((float) size * (h / w));
        } else if (w < h) {
            aw = (int) ((float) size * (w / h));
            ah = size;
        }
        int tatezure = 2 + (size - ah) / 2;
        int upzure = tatezure < upOver ? upOver - tatezure : 0;
        int downzure = tatezure < downOver ? downOver - tatezure : 0;
        IKSGRenderUtil.guiBindAndBlit(IKSGTextureUtil.getReceiveTexture(IMPWorldData.IMAGE, pl.getImageUUID()), matrix, x + 2 + (size - aw) / 2, y + tatezure + upzure, 0, upzure, aw, ah - upzure - downzure, aw, ah);
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        if (upOver < 12 && downOver <= 38) {
            IKSGRenderUtil.drawString(fontrenderer, matrix, new StringTextComponent(pl.getName()), x + 40, y + 2, 0);
        }
        if (upOver < 24 && downOver <= 26) {
            IKSGRenderUtil.drawString(fontrenderer, matrix, new StringTextComponent(pl.getTimeStamp()), x + 40, y + 14, 0);
        }
        if (upOver < 37 && downOver <= 13) {
            IKSGRenderUtil.drawString(fontrenderer, matrix, new StringTextComponent(pl.getCreatePlayerName()), x + 50, y + 27, 0);
        }
        int fupzure = 27 < upOver ? upOver - 27 : 0;
        int fdownzure = 5 < downOver ? downOver - 5 : 0;
        IKSGRenderUtil.matrixPush(matrix);
        ResourceLocation plskin = IKSGTextureUtil.getPlayerSkinTexture(pl.getCreatePlayerName());
        IKSGRenderUtil.guiBindAndBlit(plskin, matrix, x + 40, y + 27 + fupzure, 8, 8 + fupzure, 8, 8 - fupzure - fdownzure, 64, 64);
        IKSGRenderUtil.guiBindAndBlit(plskin, matrix, x + 40, y + 27 + fupzure, 40, 8 + fupzure, 8, 8 - fupzure - fdownzure, 64, 64);
        IKSGRenderUtil.matrixPop(matrix);
      */
        RenderUtil.drwPlayImage(matrix, pl.getImage(), x + 1, y + 1, 16);
    }

    @Override
    protected int getCont() {
        return this.playLists.size();
    }

}
