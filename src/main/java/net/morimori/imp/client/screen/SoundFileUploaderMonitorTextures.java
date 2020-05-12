package net.morimori.imp.client.screen;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.morimori.imp.IkisugiMusicPlayer;

public enum SoundFileUploaderMonitorTextures implements IStringSerializable {
	OFF("off", new ResourceLocation(IkisugiMusicPlayer.MODID,"textures/gui/container/sfu_monitor_off.png")),
	ON("on",new ResourceLocation(IkisugiMusicPlayer.MODID,"textures/gui/container/sfu_monitor_on.png")),
	YJ("yj",new ResourceLocation(IkisugiMusicPlayer.MODID,"textures/gui/container/sfu_monitor_yj.png")),
	GABADADDY("gabadaddy",new ResourceLocation(IkisugiMusicPlayer.MODID,"textures/gui/container/sfu_monitor_gabadaddy.png"));


	private final String name;
	private final ResourceLocation texture;

	SoundFileUploaderMonitorTextures(String string, ResourceLocation texture) {
		this.name = string;
		this.texture = texture;
	}

	public String toString() {
		return this.name;
	}

	public ResourceLocation getResourceLocation() {
		return this.texture;
	}

	@Override
	public String getName() {

		return this.name;
	}
}
