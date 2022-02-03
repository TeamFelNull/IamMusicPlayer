package dev.felnull.imp.client.music.subtitle;

import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import org.jetbrains.annotations.NotNull;

public enum SubtitleType implements SelectionListEntry.Translatable {
    OFF("off"),
    VANILLA("vanilla"),
    OVERLAY("overlay");

    private final String name;

    SubtitleType(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getKey() {
        return "subtitleType." + name;
    }
}
