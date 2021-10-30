package dev.felnull.imp;

import dev.felnull.imp.client.music.subtitle.SubtitleType;
import dev.felnull.imp.client.music.subtitle.YoutubeSubtitleEnum;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = IamMusicPlayer.MODID)
@Config.Gui.Background("textures/block/note_block.png")
public class IMPConfig implements ConfigData {

    @ConfigEntry.Category("client")
    public float volume = 1f;

    @ConfigEntry.Category("client")
    public boolean spatial = true;

    @ConfigEntry.Category("client")
    public YoutubeSubtitleEnum youtubeSubtitleLanguage = YoutubeSubtitleEnum.AUTO;

    @ConfigEntry.Category("client")
    public SubtitleType subtitleType = SubtitleType.VANILLA;


/*
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Category("client")
    public MusicPlaySystem playSystem = MusicPlaySystem.OPEN_AL_SPATIAL;

    @ConfigEntry.Category("client")
    public SubtitleSystem subtitleSystem = SubtitleSystem.VANILLA;
*/

}
