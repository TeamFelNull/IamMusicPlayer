package dev.felnull.imp;

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
    public String relayServerURL = "https://raw.githubusercontent.com/TeamFelnull/IamMusicPlayer/master/relay_server.json";

    @ConfigEntry.Category("client")
    public String lavaPlayerNativesURL = "https://raw.githubusercontent.com/TeamFelnull/IamMusicPlayer/master/lavaplayer/natives_link.json";

    @ConfigEntry.Category("server")
    public long maxWaitTime = 1000 * 10;

    @ConfigEntry.Category("server")
    public long retryTime = 1000 * 3;

    @ConfigEntry.Category("server")
    public boolean dropItemRing = true;

    @ConfigEntry.Gui.RequiresRestart
    @ConfigEntry.Category("integration")
    public boolean patchouliIntegration = true;

    @ConfigEntry.Category("debug")
    public boolean showMusicLines = false;
}
