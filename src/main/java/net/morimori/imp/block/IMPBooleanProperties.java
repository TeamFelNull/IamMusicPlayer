package net.morimori.imp.block;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.morimori.imp.client.screen.SoundFileUploaderMonitorTextures;
import net.morimori.imp.client.screen.SoundFileUploaderWindwos;

public class IMPBooleanProperties {
	public static final BooleanProperty ON = BooleanProperty.create("on");
	public static final BooleanProperty WALL = BooleanProperty.create("wall");
	public static final EnumProperty<SoundFileUploaderMonitorTextures> SOUNDFILE_UPLOADER_MONITOR = EnumProperty
			.create("monitor", SoundFileUploaderMonitorTextures.class);
	public static final EnumProperty<SoundFileUploaderWindwos> SOUNDFILE_UPLOADER_WINDWOS = EnumProperty
			.create("windwos", SoundFileUploaderWindwos.class);
	public static final EnumProperty<CassetteDeckStates> CASSETTE_DECK_STATES = EnumProperty
			.create("cd_state", CassetteDeckStates.class);
	public static final IntegerProperty VOLUME_0_8 = IntegerProperty.create("volume", 0, 32);
}
