package red.felnull.imp.client.gui.widget;

import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.client.gui.widget.ScrollBarSlider;

public class MSDScrollBarSlider extends ScrollBarSlider {
    private static ResourceLocation MSD_SCROLL = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/msd_scroll_bar.png");

    public MSDScrollBarSlider(int x, int y, int size, float maxValue, float defValue, int rang) {
        super(x, y, size, maxValue, defValue, rang);
    }

    protected ResourceLocation getTexture() {
        return MSD_SCROLL;
    }


}
