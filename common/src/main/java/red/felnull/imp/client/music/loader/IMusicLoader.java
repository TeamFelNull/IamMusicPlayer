package red.felnull.imp.client.music.loader;

import com.mojang.blaze3d.vertex.PoseStack;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.subtitle.IMusicSubtitle;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.imp.music.resource.MusicSource;

public interface IMusicLoader {
    void init();

    IMusicPlayer createMusicPlayer(MusicSource location);

    default IMusicSubtitle createMusicSubtitle(IMusicPlayer musicPlayer, MusicSource location) {
        return null;
    }

    void renderIcon(PoseStack poseStack, int x, int y, int w, int h);

    SearchData getPlayMusicData(String identifier);

    public static record SearchData(String name, String setName, String identifier, ImageInfo image, long duration, String author) {
    }
}
