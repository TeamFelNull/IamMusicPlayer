package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.client.data.MusicSourceClientReferencesType;
import red.felnull.imp.client.gui.screen.IMPAbstractEquipmentScreen;
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
        this.text = !isComingSoon ? IKSGStyles.withStyle(new TranslationTextComponent("uploadlocation." + location.name().toLowerCase()), IMPAbstractEquipmentScreen.smart_fontStyle) : IKSGStyles.withStyle(new TranslationTextComponent("msd.comingsoon"), IMPAbstractEquipmentScreen.smart_fontStyle);
        this.isComingSoon = isComingSoon;
        this.screen = screen;
        this.locationType = location;
        this.xTexStart = xTexStartIn;
        this.yTexStart = yTexStartIn;
        this.yDiffText = yDiffTextIn;
        this.resourceLocation = resourceLocationIn;
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float parTic) {

     /*   if (isComingSoon || screen.uploadLocation != locationType) {
            super.renderButton(matrix, mouseX, mouseY, parTic);
        } else {
            int i = this.yTexStart + this.height * 2;
            if (this.isHovered()) {
                i += this.yDiffText;
            }
            IKSGRenderUtil.guiBindAndBlit(resourceLocation, matrix, this.x, this.y, xTexStart, i, this.width, this.height, 256, 256);
        }*/
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        int drsize = isComingSoon ? width - 2 : width - 15;
        int stsize = fontrenderer.getStringPropertyWidth(text);
        float size = drsize >= stsize ? 1 : (float) drsize / (float) stsize;
        int x = isComingSoon ? this.x + this.width / 2 + 1 : this.x + 16;
        int y = this.y + (this.height - fontrenderer.FONT_HEIGHT) / 2 + 1;
        IKSGRenderUtil.matrixPush(matrix);
        IKSGRenderUtil.matrixScalf(matrix, size);
        if (isComingSoon)
            IKSGRenderUtil.drawCenterString(fontrenderer, matrix, text, (int) ((float) x / size), (int) ((float) y / size), 0, false);
        else
            IKSGRenderUtil.drawString(fontrenderer, matrix, text, (int) ((float) x / size), (int) ((float) y / size), 0, false);
        IKSGRenderUtil.matrixPop(matrix);

        if (!isComingSoon) {
            if (locationType == MusicSharingDeviceScreen.UploadLocation.URL) {
                IKSGRenderUtil.guiBindAndBlit(MusicSourceClientReferencesType.URL.getTextuerLocation(), matrix, this.x + 2, this.y + 2, 0, 0, 11, 11, 11, 11);
            } else if (locationType == MusicSharingDeviceScreen.UploadLocation.WORLD) {
                IKSGRenderUtil.guiBindAndBlit(WorldIcon, matrix, this.x + 2, this.y + 2, 0, 0, 11, 11, 11, 11);
            }
        }
    }
}
