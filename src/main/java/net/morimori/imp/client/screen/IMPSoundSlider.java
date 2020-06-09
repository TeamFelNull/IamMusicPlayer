package net.morimori.imp.client.screen;

import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.client.resources.I18n;
import net.morimori.imp.IamMusicPlayer;
import net.morimori.imp.file.Options;

public class IMPSoundSlider extends AbstractSlider {
	public static float AllSoundVolume = 1;

	public IMPSoundSlider(int xIn, int yIn, int widthIn, int heightIn) {
		super(xIn, yIn, widthIn, heightIn, AllSoundVolume);

		this.updateMessage();
	}

	@Override
	protected void updateMessage() {
		String s = (float) this.value == (float) this.getYImage(false) ? I18n.format("options.off")
				: (int) ((float) this.value * 100.0F) + "%";
		this.setMessage(I18n.format("soundCategory." + IamMusicPlayer.MODID) + ": " + s);
	}

	@Override
	protected void applyValue() {
		AllSoundVolume = (float) this.value;
		Options.writeOption(true);
	}

}
