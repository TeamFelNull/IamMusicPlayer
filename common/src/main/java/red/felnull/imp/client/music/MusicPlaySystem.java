package red.felnull.imp.client.music;

import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import org.jetbrains.annotations.NotNull;

public enum MusicPlaySystem implements SelectionListEntry.Translatable {
    OPEN_AL_SPATIAL("spatial"),
    OPEN_AL_NONSPATIAL("non_spatial"),
    JAVA_SOUND_API("jsa");

    private final String name;

    MusicPlaySystem(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getKey() {
        return "musicPlaySystem." + name;
    }
}
