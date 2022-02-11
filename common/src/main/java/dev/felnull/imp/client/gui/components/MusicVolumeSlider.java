package dev.felnull.imp.client.gui.components;

import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class MusicVolumeSlider extends AbstractSliderButton {
    public MusicVolumeSlider(int x, int y, int w) {
        super(x, y, w, 20, TextComponent.EMPTY, IamMusicPlayer.CONFIG.volume);
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        Component component = (float) this.value == (float) this.getYImage(false) ? CommonComponents.OPTION_OFF : new TextComponent((int) (this.value * 100.0D) + "%");
        this.setMessage((new TranslatableComponent("soundCategory." + IamMusicPlayer.MODID)).append(": ").append(component));
    }

    @Override
    protected void applyValue() {
        IamMusicPlayer.CONFIG.volume = value;
    }
}
