package red.felnull.imp.data;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PlayListGuildManeger {

    private static PlayListGuildManeger INSTANCE;

    public PlayListGuildManeger() {
    }

    public static void init() {
        INSTANCE = new PlayListGuildManeger();
    }

    public static PlayListGuildManeger instance() {
        return INSTANCE;
    }

    @OnlyIn(Dist.CLIENT)
    public void createPlayListRequest(String name, byte[] image) {

        System.out.println("test");

    }

}
