package red.felnull.imp.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import red.felnull.imp.client.data.MusicSourceClientReferencesType;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.Arrays;
import java.util.List;

public class PlayMusicSourceReferenceButton extends ImageButton {
    private MusicSharingDeviceScreen screen;

    public PlayMusicSourceReferenceButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, IPressable onPressIn, MusicSharingDeviceScreen screen) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPressIn);
        this.screen = screen;
    }

    public void func_230431_b_(MatrixStack matrix, int mouseX, int mouseY, float partick) {
        super.func_230431_b_(matrix, mouseX, mouseY, partick);
        int size = 10;
        IKSGRenderUtil.guiBindAndBlit(screen.getMusicSourceClientReferencesType().getTextuerLocation(), matrix, this.field_230690_l_ + (9 - size / 2), this.field_230691_m_ + (7 - size / 2), 0, 0, size, size, size, size);
    }

    @Override
    public void func_230982_a_(double x, double y) {
        super.func_230982_a_(x, y);
        List<MusicSourceClientReferencesType> mscrt = Arrays.asList(MusicSourceClientReferencesType.values());
        int crnum = mscrt.indexOf(screen.getMusicSourceClientReferencesType());
        if (mscrt.size() - 1 == crnum) {
            screen.setMusicSourceClientReferencesType(mscrt.get(0));
        } else {
            screen.setMusicSourceClientReferencesType(mscrt.get(crnum + 1));
        }
    }
}
