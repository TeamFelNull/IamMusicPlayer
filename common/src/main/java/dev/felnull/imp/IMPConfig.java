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
    public boolean errorLog = false;

    @ConfigEntry.Category("client")
    public double volume = 1f;

    @ConfigEntry.Category("client")
    public int maxPlayCont = 8;

    @ConfigEntry.Category("client")
    public boolean spatial = true;

    @ConfigEntry.Category("client")
    public SubtitleType subtitleType = SubtitleType.OFF;

    @ConfigEntry.Category("client")
    public YoutubeSubtitleEnum youtubeSubtitleLanguage = YoutubeSubtitleEnum.AUTO;

    @ConfigEntry.Category("server")
    public long maxWaitTime = 1000 * 10;

    @ConfigEntry.Category("server")
    public long retryTime = 1000 * 3;
}
