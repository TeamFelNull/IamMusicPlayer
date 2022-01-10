package dev.felnull.imp.client.music.loadertypes;

import dev.felnull.imp.music.resource.ImageInfo;
import dev.felnull.imp.music.resource.MusicSource;
import net.minecraft.network.chat.Component;

import java.util.List;

public record MusicLoadResult(MusicSource source, ImageInfo imageInfo, String name, List<Component> info) {
}
