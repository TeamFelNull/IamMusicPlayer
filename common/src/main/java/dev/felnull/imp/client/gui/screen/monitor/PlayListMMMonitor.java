package dev.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.blockentity.MusicManagerBlockEntity;
import dev.felnull.imp.client.gui.screen.MusicManagerScreen;
import dev.felnull.imp.music.resource.MusicPlayList;

import java.util.ArrayList;
import java.util.List;

public class PlayListMMMonitor extends MusicManagerMonitor {
    private final List<MusicPlayList> musicPlayLists = new ArrayList<>();

    public PlayListMMMonitor(MusicManagerBlockEntity.MonitorType type, MusicManagerScreen screen, int leftPos, int topPos) {
        super(type, screen, leftPos, topPos);
    }

    @Override
    public void init() {
        super.init();
        //  addRenderWidget(new PlayListFixedButtonsList(leftPos + x, topPos + y, 50, 100, 10, new TextComponent("test"), musicPlayLists, (fixedButtonsList, playList, i, i1) -> {
        //       System.out.println("test");
        //  }));
    }

    @Override
    public void render(PoseStack poseStack, float f, int mouseX, int mouseY) {
        super.render(poseStack, f, mouseX, mouseY);

    }

    @Override
    public void tick() {
        super.tick();
        musicPlayLists.clear();
        musicPlayLists.addAll(getSyncManager().getMyPlayList());
    }
}
