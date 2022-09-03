package dev.felnull.imp.client.gui.components;

import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class MusicVolumeSlider extends AbstractSliderButton {
    public MusicVolumeSlider(int x, int y, int w) {
        super(x, y, w, 20, Component.literal(""), IamMusicPlayer.CONFIG.volume);
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        Component component = (float) this.value == (float) this.getYImage(false) ? CommonComponents.OPTION_OFF : Component.literal((int) (this.value * 100.0D) + "%");
        this.setMessage((Component.translatable("soundCategory." + IamMusicPlayer.MODID)).append(": ").append(component));
    }

    @Override
    protected void applyValue() {
        IamMusicPlayer.CONFIG.volume = value;
    }
}
