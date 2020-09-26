package red.felnull.imp.client.data;

import net.minecraft.util.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;

public enum MusicSourceClientReferencesType {
    LOCAL_FILE(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_source_references/local_file.png")),
    URL(new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/music_source_references/url.png"));
    private final ResourceLocation textuerLocation;

    private MusicSourceClientReferencesType(ResourceLocation textuerLocation) {
        this.textuerLocation = textuerLocation;
    }

    public ResourceLocation getTextuerLocation() {
        return textuerLocation;
    }
}
