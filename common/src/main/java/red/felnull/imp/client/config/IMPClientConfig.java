package red.felnull.imp.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import red.felnull.imp.IamMusicPlayer;

@Config(name = IamMusicPlayer.MODID + "_client")
@Config.Gui.Background("textures/block/note_block.png")
public class IMPClientConfig implements ConfigData {
    public float volume = 0f;
}
