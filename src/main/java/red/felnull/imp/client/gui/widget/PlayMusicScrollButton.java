package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.client.util.RenderUtil;
import red.felnull.imp.musicplayer.PlayMusic;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;
import red.felnull.otyacraftengine.client.gui.widget.ScrollListButton;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.client.util.IKSGTextureUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayMusicScrollButton extends ScrollListButton {
    private final List<PlayMusic> playMusic;

    public PlayMusicScrollButton(int x, int y, int sizeX, int sizeY, int sizeOne, ScrollBarSlider scrollBar, List<PlayMusic> playMusic, IPressable pressed) {
        super(x, y, sizeX, sizeY, sizeOne, 0, scrollBar, null, pressed);
        this.playMusic = playMusic;
    }

    @Override
    public void renderOneList(MatrixStack matrix, int x, int y, int num, int upOver, int downOver) {
        IKSGRenderUtil.guiBindAndBlit(MusicSharingDeviceScreen.MSD_GUI_TEXTURES2, matrix, x, y + upOver, 53, 40 + upOver, 158, 40 - downOver, 256, 256);
        PlayMusic pm = playMusic.get(num);
        int upzure = 2 < upOver ? upOver - 2 : 0;
        int downzure = 2 < downOver ? downOver - 2 : 0;
        RenderUtil.drwPlayImage(matrix, pm.getImage(), x + 2, y + 2, 36, upzure, downzure);
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        if (upOver < 13 && downOver <= 37) {
            drawHorizontalMovementString(matrix, fontrenderer, pm.getName(), "name." + pm.getUUID(), 30, x + 40, y + 3, 115, 30, MusicSharingDeviceScreen.fontStyle);
        }
        if (upOver < 25 && downOver <= 25) {
            List<ITextComponent> descs = new ArrayList<>();

            if (!pm.getArtist().isEmpty())
                descs.add(new TranslationTextComponent("playmusic.desc.artist", pm.getArtist()));
            if (!pm.getAlbum().isEmpty())
                descs.add(new TranslationTextComponent("playmusic.desc.album", pm.getAlbum()));
            if (!pm.getGenre().isEmpty())
                descs.add(new TranslationTextComponent("playmusic.desc.genre", pm.getGenre()));
            if (!pm.getYear().isEmpty())
                descs.add(new TranslationTextComponent("playmusic.desc.year", pm.getYear()));
            if (!pm.getTimeStamp().isEmpty())
                descs.add(new TranslationTextComponent("playmusic.desc.timestamp", pm.getTimeStamp()));

            TranslationTextComponent musicdesc = new TranslationTextComponent("playmusic.desc.all." + descs.size(), descs.toArray());

            drawHorizontalMovementString(matrix, fontrenderer, musicdesc.getString(), "desc." + pm.getUUID(), 30, x + 40, y + 16, 115, 30, MusicSharingDeviceScreen.fontStyle);
        }
        if (upOver < 38 && downOver <= 12) {
            IKSGRenderUtil.drawString(fontrenderer, matrix, IKSGStyles.withStyle(new StringTextComponent(pm.getCreatePlayerName()), MusicSharingDeviceScreen.fontStyle), x + 50, y + 29, 0);
        }
        int fupzure = 29 < upOver ? upOver - 29 : 0;
        int fdownzure = 3 < downOver ? downOver - 3 : 0;
        IKSGRenderUtil.matrixPush(matrix);
        ResourceLocation plskin = IKSGTextureUtil.getPlayerSkinTexture(pm.getCreatePlayerName());
        IKSGRenderUtil.guiBindAndBlit(plskin, matrix, x + 40, y + 29 + fupzure, 8, 8 + fupzure, 8, 8 - fupzure - fdownzure, 64, 64);
        IKSGRenderUtil.guiBindAndBlit(plskin, matrix, x + 40, y + 29 + fupzure, 40, 8 + fupzure, 8, 8 - fupzure - fdownzure, 64, 64);
        IKSGRenderUtil.matrixPop(matrix);
    }

    public static final Map<String, Integer> MISALIGNEDS = new HashMap<>();
    public static final Map<String, Long> MISALIGNEDS_LASTTIMES = new HashMap<>();

    public static void drawHorizontalMovementString(MatrixStack matrix, FontRenderer fontRenderer, String text, String id, int blank, int x, int y, int width, int speed, Style... style) {
        IFormattableTextComponent textc = IKSGStyles.withStyle(new StringTextComponent(text), style);
        int textSize = fontRenderer.func_238414_a_(textc);
        if (width >= textSize) {
            IKSGRenderUtil.drawString(fontRenderer, matrix, textc, x, y, 0);
            return;
        }
        int allsize = textSize + blank;
        if (!MISALIGNEDS.containsKey(id)) {
            MISALIGNEDS.put(id, 0);
            MISALIGNEDS_LASTTIMES.put(id, System.currentTimeMillis());
        } else {
            long conttime = System.currentTimeMillis() - MISALIGNEDS_LASTTIMES.get(id);
            if (conttime >= 1000 / speed) {
                int zures = MISALIGNEDS.get(id);
                if (zures >= allsize)
                    MISALIGNEDS.put(id, 1);
                else
                    MISALIGNEDS.put(id, zures + 1);
                MISALIGNEDS_LASTTIMES.put(id, System.currentTimeMillis());
            }
        }
        int zure = MISALIGNEDS.get(id);
        // IKSGRenderUtil.drawString(fontRenderer, matrix, textc, x - zure, y, 0);

        String intext = text;
        if (allsize - zure <= width) {
            for (int i = 0; i < text.length(); i++) {
                String cutble = cutForBack(text, i);
                int cuttoblesize = fontRenderer.func_238414_a_(IKSGStyles.withStyle(new StringTextComponent(cutble), style));
                if (width - allsize - zure > cuttoblesize)
                    break;
                intext = cutble;
            }
        } else {
            intext = "";
        }
        IFormattableTextComponent inextc = IKSGStyles.withStyle(new StringTextComponent(intext), style);
        IKSGRenderUtil.drawString(fontRenderer, matrix, inextc, x + allsize - zure, y, 0);
    }

    public static String cutForBack(String text, int num) {
        return text.substring(0, text.length() - num);
    }

    public static String cutForFront(String text, int num) {
        return text.substring(0, 0);
    }

    @Override
    protected int getCont() {
        return this.playMusic.size();
    }

}
