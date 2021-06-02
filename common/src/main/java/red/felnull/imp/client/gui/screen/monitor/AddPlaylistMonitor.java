package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.components.PublishedPlayListFixedButtonsList;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.music.resource.MusicPlayList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddPlaylistMonitor extends MSDBaseMonitor {
    private final List<MusicPlayList> playList = new ArrayList<>();

    public AddPlaylistMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdmonitor.addplaylist"), msdScreen, parentScreen, x, y, width, height);
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
        playList.add(new MusicPlayList(UUID.randomUUID(), "TEST", null, null, null, null));
    }

    @Override
    public void init() {
        super.init();

        this.addRenderableWidget(new PublishedPlayListFixedButtonsList(x + 1, y + 21, 197, 100, 5, new TextComponent("Play List"), this.playList, n -> new TextComponent(n.getName()), (n) -> {
            System.out.println(n.item().getName());
        }));

        addCreateSmartButton(new TranslatableComponent("imp.msdbutton.create"), x + 175, y + 12, 23, 8, n -> {

        });

    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        fillXGrayLine(poseStack, x + 1, y + 11, 197);
        fillXGrayLine(poseStack, x + 1, y + 20, 197);
        fillLightGray(poseStack, x + 1, y + 12, 174, 8);
    }
}
