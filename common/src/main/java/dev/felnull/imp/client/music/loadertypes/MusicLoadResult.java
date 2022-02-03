package dev.felnull.imp.client.music.loadertypes;

import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;

public record MusicLoadResult(MusicSource source, ImageInfo imageInfo, String name, String author) {
}
