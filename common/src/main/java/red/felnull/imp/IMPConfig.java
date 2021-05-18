package red.felnull.imp;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import red.felnull.imp.client.music.player.MusicPlaySystem;
import red.felnull.imp.client.music.subtitle.SubtitleSystem;

@Config(name = IamMusicPlayer.MODID)
@Config.Gui.Background("textures/block/note_block.png")
public class IMPConfig implements ConfigData {

    @ConfigEntry.Category("client")
    public float volume = 1f;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Category("client")
    public MusicPlaySystem playSystem = MusicPlaySystem.OPEN_AL_SPATIAL;

    @ConfigEntry.Category("client")
    public SubtitleSystem subtitleSystem = SubtitleSystem.VANILLA;
}
