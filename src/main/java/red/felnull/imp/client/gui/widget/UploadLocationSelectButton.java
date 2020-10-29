package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.client.data.MusicSourceClientReferencesType;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

public class UploadLocationSelectButton extends ImageButton {
    private static final ResourceLocation WorldIcon = new ResourceLocation("textures/misc/unknown_server.png");
    private final ITextComponent text;
    private final boolean isComingSoon;
    private final MusicSharingDeviceScreen screen;
    private final MusicSharingDeviceScreen.UploadLocation locationType;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffText;
    private final ResourceLocation resourceLocation;

    public UploadLocationSelectButton(MusicSharingDeviceScreen screen, MusicSharingDeviceScreen.UploadLocation location, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, IPressable onPressIn, boolean isComingSoon) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPressIn);
        this.text = !isComingSoon ? IKSGStyles.withStyle(new TranslationTextComponent("uploadlocation." + location.name().toLowerCase()), MusicSharingDeviceScreen.fontStyle) : IKSGStyles.withStyle(new TranslationTextComponent("msd.comingsoon"), MusicSharingDeviceScreen.fontStyle);
        this.isComingSoon = isComingSoon;
        this.screen = screen;
        this.locationType = location;
        this.xTexStart = xTexStartIn;
        this.yTexStart = yTexStartIn;
        this.yDiffText = yDiffTextIn;
        this.resourceLocation = resourceLocationIn;
    }

    public void func_230431_b_(MatrixStack matrix, int mouseX, int mouseY, float parTic) {

        if (isComingSoon || screen.uploadLocation != locationType) {
            super.func_230431_b_(matrix, mouseX, mouseY, parTic);
        } else {
            int i = this.yTexStart + this.field_230689_k_ * 2;
            if (this.func_230449_g_()) {
                i += this.yDiffText;
            }
            IKSGRenderUtil.guiBindAndBlit(resourceLocation, matrix, this.field_230690_l_, this.field_230691_m_, xTexStart, i, this.field_230688_j_, this.field_230689_k_, 256, 256);
        }
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        int drsize = isComingSoon ? field_230688_j_ - 2 : field_230688_j_ - 15;
        int stsize = fontrenderer.func_238414_a_(text);
        float size = drsize >= stsize ? 1 : (float) drsize / (float) stsize;
        int x = isComingSoon ? this.field_230690_l_ + this.field_230688_j_ / 2 + 1 : this.field_230690_l_ + 16;
        int y = this.field_230691_m_ + (this.field_230689_k_ - fontrenderer.FONT_HEIGHT) / 2 + 1;
        IKSGRenderUtil.matrixPush(matrix);
        IKSGRenderUtil.matrixScalf(matrix, size);
        if (isComingSoon)
            IKSGRenderUtil.drawCenterString(fontrenderer, matrix, text, (int) ((float) x / size), (int) ((float) y / size), 0, false);
        else
            IKSGRenderUtil.drawString(fontrenderer, matrix, text, (int) ((float) x / size), (int) ((float) y / size), 0, false);
        IKSGRenderUtil.matrixPop(matrix);

        if (!isComingSoon) {
            if (locationType == MusicSharingDeviceScreen.UploadLocation.URL) {
                IKSGRenderUtil.guiBindAndBlit(MusicSourceClientReferencesType.URL.getTextuerLocation(), matrix, this.field_230690_l_ + 2, this.field_230691_m_ + 2, 0, 0, 11, 11, 11, 11);
            } else if (locationType == MusicSharingDeviceScreen.UploadLocation.WORLD) {
                IKSGRenderUtil.guiBindAndBlit(WorldIcon, matrix, this.field_230690_l_ + 2, this.field_230691_m_ + 2, 0, 0, 11, 11, 11, 11);
            }
        }
    }
}
