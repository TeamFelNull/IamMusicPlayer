package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.data.IMPSyncClientManager;
import red.felnull.imp.client.gui.IMPFonts;
import red.felnull.imp.client.gui.components.MSDSmartButton;
import red.felnull.imp.client.gui.components.MusicFixedButtonsList;
import red.felnull.imp.client.gui.components.PlayListFixedButtonsList;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.music.resource.Music;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.imp.music.resource.simple.SimpleMusicPlayList;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayListMonitor extends MSDBaseMonitor {
    private final List<SimpleMusicPlayList> playList = new ArrayList<>();
    private final List<Music> musics = new ArrayList<>();
    private MSDSmartButton allButton;
    private ImageButton createMusicButton;
    private ImageButton detailsButton;

    public PlayListMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdMonitor.playlist"), msdScreen, parentScreen, x, y, width, height);
        this.renderHeader = false;
    }

    @Override
    public void init() {
        super.init();
        IMPSyncClientManager syncClientManager = IMPSyncClientManager.getInstance();
        syncClientManager.syncMyPlayLists();
        this.addRenderableWidget(new PlayListFixedButtonsList(x + 1, y + 21, 29, 100, 5, new TextComponent("Play List"), this.playList, n -> new TextComponent(n.getName()), (n) -> {
            getParentScreen().selectPlayList = n.item();
            syncClientManager.syncMusics(n.item().getUUID());
        }, n -> n.equals(getParentScreen().selectPlayList)));

        this.addRenderableWidget(new MusicFixedButtonsList(x + 30, y + 21, 169, 100, 5, new TextComponent("Musics"), this.musics, n -> new TextComponent(n.getName()), (n) -> {

        }));

        this.allButton = this.addCreateSmartButton(new TextComponent("All"), x + 1, y + 1, 20, 19, n -> {
            getParentScreen().selectPlayList = MusicPlayList.ALL.getSimple();
        });

        this.addRenderableWidget(new ImageButton(x + 22, y + 1, 7, 19, 38, 20, 19, MSD_WIDGETS, 256, 256, n -> insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.ADD_PLAYLIST)));

        this.createMusicButton = this.addRenderableWidget(new ImageButton(x + 191, y + 1, 7, 19, 38, 20, 19, MSD_WIDGETS, 256, 256, n -> insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.CREATE_MUSIC)));

        this.detailsButton = addRenderableWidget(new ImageButton(x + 176, y + 1, 14, 10, 54, 30, MSD_WIDGETS, n -> {
            insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.PLAYLIST_DETAILS);
        }));

    }

    @Override
    public void tick() {
        super.tick();
        IMPSyncClientManager syncClientManager = IMPSyncClientManager.getInstance();

        this.playList.clear();
        this.playList.addAll(syncClientManager.getMyPlayLists());
        if (!playList.contains(getParentScreen().selectPlayList)) {
            getParentScreen().selectPlayList = MusicPlayList.ALL.getSimple();
        }
        this.allButton.active = !getParentScreen().selectPlayList.equals(MusicPlayList.ALL.getSimple());

        this.musics.clear();
        if (!getParentScreen().selectPlayList.equals(MusicPlayList.ALL.getSimple()))
            this.musics.addAll(syncClientManager.getMusics(getParentScreen().selectPlayList.getUUID()));
        else
            this.musics.addAll(syncClientManager.getAllMusics());

        this.createMusicButton.visible = this.createMusicButton.active = !getParentScreen().selectPlayList.equals(MusicPlayList.ALL.getSimple());
        this.detailsButton.visible = this.detailsButton.active = !getParentScreen().selectPlayList.equals(MusicPlayList.ALL.getSimple());
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        fillXGrayLine(poseStack, x + 1, y + 20, 28);
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

        drawPrettyString(poseStack, new TextComponent(getParentScreen().selectPlayList.getName()), x + 31, y + 2, 0);
        if (!getParentScreen().selectPlayList.equals(MusicPlayList.ALL.getSimple())) {

            boolean mflg = getParentScreen().selectPlayList.getPlayerCont() > 1;

            MutableComponent ccomp = new TextComponent("+" + getParentScreen().selectPlayList.getPlayerCont()).withStyle(IMPFonts.FLOPDE_SIGN_FONT);
            IKSGRenderUtil.drawPlayerFase(poseStack, getParentScreen().selectPlayList.getOwner(), x + 165 - (mflg ? getFont().width(ccomp) : 0), y + 1, 10);
            if (mflg) {
                drawPrettyString(poseStack, ccomp, x + 176 - getFont().width(ccomp), y + 2, 0);
            }
        }

    }

    @Override
    public void renderBg(PoseStack poseStack, int mousX, int mousY, float parTick) {
        super.renderBg(poseStack, mousX, mousY, parTick);
        fillLightGray(poseStack, x + 30, y + 1, 160, 19);
    }
}
