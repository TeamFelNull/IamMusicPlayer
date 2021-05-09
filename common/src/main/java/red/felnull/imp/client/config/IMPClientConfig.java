package red.felnull.imp.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.music.MusicPlaySystem;

@Config(name = IamMusicPlayer.MODID + "_client")
@Config.Gui.Background("textures/block/note_block.png")
public class IMPClientConfig implements ConfigData {

    public float volume = 1f;

    public MusicPlaySystem playSystem = MusicPlaySystem.OPEN_AL_MONO;
}
