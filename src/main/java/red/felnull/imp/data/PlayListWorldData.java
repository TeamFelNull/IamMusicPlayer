package red.felnull.imp.data;

import net.minecraft.nbt.CompoundNBT;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.data.WorldData;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PlayListWorldData extends WorldData {
    @Override
    public Path getSavedFolderPath() {
        return Paths.get(IamMusicPlayer.MODID).resolve("playlist_data.dat");
    }

    @Override
    public CompoundNBT getInitialNBT(CompoundNBT tag) {
        tag.put("playlists", new CompoundNBT());
        tag.put("players", new CompoundNBT());
        return tag;
    }
}
