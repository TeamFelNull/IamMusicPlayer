package red.felnull.imp.sound;

import java.nio.file.Path;

public class PlayData {

	public String url;
	public Path path;
	public WorldSoundKey wsk;
	public PlayDatasTypes type;

	public String name;

	public PlayData(String URL) {
		this.url = URL;
		this.type = PlayDatasTypes.URL_STREAM;
		this.name = URL;
	}

	public PlayData(Path path) {
		this.path = path;
		this.type = PlayDatasTypes.FILE;
		this.name = path.toFile().getName();
	}

	public PlayData(WorldSoundKey key) {
		this.wsk = key;
		this.type = PlayDatasTypes.WORLD;
		this.name = key.getName();
	}

	public static enum PlayDatasTypes {
		WORLD, URL_STREAM, FILE

	}
}
