package red.felnull.imp.client.screen;

import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.file.Options;

public class IMPSoundSlider extends AbstractSlider {
    public static float AllSoundVolume = 1;

    public IMPSoundSlider(int xIn, int yIn, int widthIn, int heightIn) {
        super(xIn, yIn, widthIn, heightIn, new StringTextComponent("test"), AllSoundVolume);
        this.func_230979_b_();
    }


    @Override
    protected void func_230979_b_() {
        String s = (float) this.field_230683_b_ == (float) this.func_230989_a_(false) ? I18n.format("options.off")
                : (int) ((float) this.field_230683_b_ * 100.0F) + "%";
        this.func_238482_a_(new TranslationTextComponent("soundCategory." + IamMusicPlayer.MODID, s));
    }

    @Override
    protected void func_230972_a_() {
        AllSoundVolume = (float) this.field_230683_b_;
        Options.writeOption(true);
    }
}
