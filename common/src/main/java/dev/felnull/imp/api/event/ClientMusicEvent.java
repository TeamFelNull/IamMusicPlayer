package dev.felnull.imp.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.felnull.imp.client.music.subtitle.SubtitleEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface ClientMusicEvent {
    Event<SubtitleAdd> ADD_SUBTITLE = EventFactory.createLoop();

    @Environment(EnvType.CLIENT)
    public interface SubtitleAdd {
        void add(SubtitleEntry entry);
    }
}
