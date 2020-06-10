package net.morimori.imp.sound;

import java.nio.file.Path;

public class PlayData {

	public String url;
	public Path path;
	public WorldSoundKey wsk;
	public PlayDatasTypes type;

	public PlayData(String URL) {
		this.url = URL;
		this.type = PlayDatasTypes.URL_STREAM;
	}

	public PlayData(Path path) {
		this.path = path;
		this.type = PlayDatasTypes.FILE;
	}

	public PlayData(WorldSoundKey key) {
		this.wsk = key;
		this.type = PlayDatasTypes.WORLD;
	}

}

enum PlayDatasTypes {
	WORLD, URL_STREAM, FILE

}