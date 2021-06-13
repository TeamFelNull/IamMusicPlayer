package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.data.IMPServerSyncManager;
import red.felnull.imp.client.gui.components.MSDSmartCheckbox;
import red.felnull.imp.client.gui.components.PlayersFixedButtonsList;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.data.resource.ImageInfo;

import java.util.ArrayList;
import java.util.List;

public class CreatePlaylistMonitor extends MSDCreateBaseMonitor {
    private final List<PlayerInfo> playerInfos = new ArrayList<>();
    private MSDSmartCheckbox publicedCheckBox;
    private PlayersFixedButtonsList playersFixedButtonsList;


    public CreatePlaylistMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdMonitor.createPlaylist"), msdScreen, parentScreen, x, y, width, height);
    }

    @Override
    public void init() {
        super.init();
        nameTextBox.setValue(getMinecraft().player.getGameProfile().getName() + "'s Play List");

        publicedCheckBox = this.addCreateCheckBox(new TranslatableComponent("imp.msdCheckBox.publiced"), x + 101, y + 51);
        publicedCheckBox.setChecked(true);

        playersFixedButtonsList = this.addRenderableWidget(new PlayersFixedButtonsList(x + 4, y + 74, 84, 44, 4, new TextComponent("Players"), playerInfos, n -> {

        }));

    }

    @Override
    public void tick() {
        super.tick();
        playerInfos.clear();
        playerInfos.addAll(IMPServerSyncManager.getInstance().getOnlinePlayers());
    }

    @Override
    protected void created() {
        boolean pub = publicedCheckBox.isChecked();
        String name = nameTextBox.getValue();
        ImageInfo image = imageInfo;

        insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.PLAYLIST);
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
    }

    @Override
    public void renderBg(PoseStack poseStack, int mousX, int mousY, float parTick) {
        super.renderBg(poseStack, mousX, mousY, parTick);
        drawDarkBox(poseStack, x + 3, y + 73, 86, 46);
        drawDarkBox(poseStack, x + 101, y + 72, 95, 29);
    }
}
