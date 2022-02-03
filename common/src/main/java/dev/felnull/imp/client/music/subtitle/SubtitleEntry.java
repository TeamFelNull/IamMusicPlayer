package dev.felnull.imp.client.music.subtitle;

import dev.felnull.imp.client.music.player.IMusicPlayer;
import dev.felnull.imp.music.MusicPlaybackInfo;
import net.minecraft.network.chat.Component;

public record SubtitleEntry(Component component, IMusicPlayer musicPlayer, MusicPlaybackInfo playbackInfo,
                            long duration) {
}
