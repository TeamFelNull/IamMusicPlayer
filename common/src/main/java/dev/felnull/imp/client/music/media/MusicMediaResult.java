package dev.felnull.imp.client.music.media;

import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;

public record MusicMediaResult(MusicSource source, ImageInfo imageInfo, String name, String author) {
}
