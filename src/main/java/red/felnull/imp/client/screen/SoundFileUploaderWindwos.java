package red.felnull.imp.client.screen;

import net.minecraft.util.IStringSerializable;

public enum SoundFileUploaderWindwos implements IStringSerializable {
	NONE("none"),
	DESKTOP("desktop"),
	CLIENT_FILESELECT("client_fileselect"),
	SERVER_FILESELECT("server_fileselect"),
	UPLOAD_FILE("upload_file"),
	NO_ANTENA("error_antena"),
	UPLOAD_COFIN("upload_cofin"),
	EDIT_FILE("edit_file");


	private final String name;

	SoundFileUploaderWindwos(String string) {
		this.name = string;
	}

	@Override
	public String func_176610_l() {

		return name;
	}

}
