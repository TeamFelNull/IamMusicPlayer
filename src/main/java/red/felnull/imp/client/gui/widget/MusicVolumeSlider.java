package red.felnull.imp.client.gui.widget;

import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.client.music.ClientWorldMusicManager;

public class MusicVolumeSlider extends AbstractSlider {
    public MusicVolumeSlider(int x, int y, int width) {
        super(x, y, width, 20, StringTextComponent.EMPTY, ClientWorldMusicManager.instance().getMusicVolume());
        this.func_230979_b_();
    }

    @Override
    protected void func_230979_b_() {
        ITextComponent itextcomponent = this.sliderValue == 0f ? DialogTexts.OPTIONS_OFF : new StringTextComponent((int) (this.sliderValue * 100.0D) + "%");
        this.setMessage((new TranslationTextComponent("imp.musicVolume")).appendString(": ").append(itextcomponent));
    }

    @Override
    protected void func_230972_a_() {
        ClientWorldMusicManager.instance().setMusicVolume(sliderValue);
    }

}
