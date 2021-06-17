package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.data.IMPSyncClientManager;
import red.felnull.imp.client.gui.components.PublishedPlayListFixedButtonsList;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.music.resource.simple.SimpleMusicPlayList;
import red.felnull.otyacraftengine.api.SimpleMessageSender;

import java.util.ArrayList;
import java.util.List;

public class AddPlaylistMonitor extends MSDBaseMonitor {
    private final List<SimpleMusicPlayList> playLists = new ArrayList<>();

    public AddPlaylistMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdMonitor.addPlaylist"), msdScreen, parentScreen, x, y, width, height);
    }

    @Override
    public void init() {
        super.init();
        IMPSyncClientManager.getInstance().syncPublicPlayLists();

        this.addRenderableWidget(new PublishedPlayListFixedButtonsList(x + 1, y + 21, 197, 100, 5, new TextComponent("Play List"), this.playLists, n -> new TextComponent(n.getName()), (n) -> {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("UUID", n.item().getUUID());
            SimpleMessageSender.sendToServer(new ResourceLocation(IamMusicPlayer.MODID, "simple_request"), 0, tag);
            insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.PLAYLIST);
        }));

        addCreateSmartButton(new TranslatableComponent("imp.msdButton.create"), x + 175, y + 12, 23, 8, n -> {
            insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.CREATE_PLAYLIST);
        });

    }

    @Override
    public void tick() {
        super.tick();
        playLists.clear();
        playLists.addAll(IMPSyncClientManager.getInstance().getPublicPlayLists());
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        fillXGrayLine(poseStack, x + 1, y + 11, 197);
        fillXGrayLine(poseStack, x + 1, y + 20, 197);
        fillLightGray(poseStack, x + 1, y + 12, 174, 8);
    }
}
