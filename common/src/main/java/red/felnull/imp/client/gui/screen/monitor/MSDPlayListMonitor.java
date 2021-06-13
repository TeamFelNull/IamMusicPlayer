package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.components.PlayListFixedButtonsList;
import red.felnull.imp.client.gui.components.MusicFixedButtonsList;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.music.resource.MusicPlayList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MSDPlayListMonitor extends MSDBaseMonitor {
    private final List<MusicPlayList> playList = new ArrayList<>();
    private final List<Music> musics = new ArrayList<>();


    public MSDPlayListMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdMonitor.playlist"), msdScreen, parentScreen, x, y, width, height);
        this.renderHeader = false;
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));


        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));
        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));
        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));
        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));
        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));
        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));
        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));
        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));
        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));
        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));
        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));
        musics.add(new Music(UUID.randomUUID(), "TEST", 0, null, null, null, null));

    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(new PlayListFixedButtonsList(x + 1, y + 21, 29, 100, 5, new TextComponent("Play List"), this.playList, n -> new TextComponent(n.getName()), (n) -> {
            System.out.println(n.item().getName());
        }));
        this.addRenderableWidget(new MusicFixedButtonsList(x + 30, y + 21, 169, 100, 5, new TextComponent("Musics"), this.musics, n -> new TextComponent(n.getName()), (n) -> {
            System.out.println(n.item().getName());
        }));

        this.addRenderableWidget(new ImageButton(x + 1, y + 1, 20, 19, 18, 20, 19, MSD_WIDGETS, 256, 256, n -> {

        }));

        this.addRenderableWidget(new ImageButton(x + 22, y + 1, 7, 19, 38, 20, 19, MSD_WIDGETS, 256, 256, n -> insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.ADD_PLAYLIST)));

        this.addRenderableWidget(new ImageButton(x + 191, y + 1, 7, 19, 38, 20, 19, MSD_WIDGETS, 256, 256, n -> {

        }));
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        fillXGrayLine(poseStack, x + 1, y + 20, 28);
        fillLightGray(poseStack, x + 30, y + 1, 160, 19);
        fillXGrayLine(poseStack, x + 30, y + 11, 160);
        fillXGrayLine(poseStack, x + 22, y + 20, 7);
        fillXGrayLine(poseStack, x + 190, y + 120, 9);
        fillYGrayLine(poseStack, x + 21, y + 1, 120);
        fillYGrayLine(poseStack, x + 29, y + 1, 120);
        fillYGrayLine(poseStack, x + 190, y + 1, 120);
        fillXGrayLine(poseStack, x + 30, y + 20, 168);
        fillXGrayLine(poseStack, x + 191, y + 21, 7);
        fillYGrayLine(poseStack, x + 198, y + 20, 100);
        fillXGrayLine(poseStack, x + 22, y + 21, 7);
        fillXGrayLine(poseStack, x + 22, y + 120, 7);
    }
}
