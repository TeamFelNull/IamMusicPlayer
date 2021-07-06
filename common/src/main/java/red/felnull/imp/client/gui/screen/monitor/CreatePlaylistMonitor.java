package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.data.IMPSyncClientManager;
import red.felnull.imp.client.data.SimplePlayerData;
import red.felnull.imp.client.gui.components.AdminInfoData;
import red.felnull.imp.client.gui.components.AdminPlayersFixedButtonsList;
import red.felnull.imp.client.gui.components.MSDSmartCheckbox;
import red.felnull.imp.client.gui.components.PlayersFixedButtonsList;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.data.resource.AdministratorInformation;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.imp.packet.PlayListCreateMessage;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.util.IKSGPacketUtil;

import java.util.*;

public class CreatePlaylistMonitor extends CreateBaseMonitor {
    private final List<SimplePlayerData> playerInfos = new ArrayList<>();
    private final List<AdminInfoData> selectedAdminPlayer = new ArrayList<>();
    private MSDSmartCheckbox publicedCheckBox;
    private PlayersFixedButtonsList playersFixedButtonsList;
    private AdminPlayersFixedButtonsList adminPlayersFixedButtonsList;


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
            selectedAdminPlayer.add(new AdminInfoData(n.item(), AdministratorInformation.AuthorityType.ADMINISTRATOR));
        }));

        adminPlayersFixedButtonsList = this.addRenderableWidget(new AdminPlayersFixedButtonsList(x + 102, y + 73, 93, 27, 3, new TextComponent("Admin Players"), selectedAdminPlayer, n -> new TextComponent(n.playerInfo().name()), n -> {
            if (n.item().type() == AdministratorInformation.AuthorityType.ADMINISTRATOR) {
                selectedAdminPlayer.set(n.itemNum(), new AdminInfoData(n.item().playerInfo(), AdministratorInformation.AuthorityType.BAN));
            } else {
                selectedAdminPlayer.remove(n.itemNum());
            }
        }));
    }

    @Override
    public void tick() {
        super.tick();
        playerInfos.clear();
        playerInfos.addAll(IMPSyncClientManager.getInstance().getOnlinePlayers().stream().filter(n -> !n.id().equals(getMinecraft().player.getGameProfile().getId())).filter(n -> selectedAdminPlayer.stream().noneMatch(m -> m.playerInfo().id().equals(n.id()))).toList());

        adminPlayersFixedButtonsList.visible = adminPlayersFixedButtonsList.active = playersFixedButtonsList.visible = playersFixedButtonsList.active = canAuthoritySetting();

    }

    @Override
    protected void created() {
        boolean pub = publicedCheckBox.isChecked();
        String name = nameTextBox.getValue();
        ImageInfo image = imageInfo;
        Map<UUID, AdministratorInformation.AuthorityType> adminData = new HashMap<>();
        selectedAdminPlayer.forEach(n -> adminData.put(n.playerInfo().id(), n.type()));

        IKSGPacketUtil.sendToServerPacket(new PlayListCreateMessage(name, pub, image, adminData));
        insMonitorScreen(MusicSharingDeviceBlockEntity.Screen.PLAYLIST);
    }

    private boolean canAuthoritySetting() {
        return !playerInfos.isEmpty() || !selectedAdminPlayer.isEmpty();
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        if (canAuthoritySetting()) {
            IKSGRenderUtil.drawBindTextuer(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 90, y + 83, 7, 62, 10, 7);
            drawPrettyString(poseStack, new TranslatableComponent("imp.msdText.playerAuthority"), x + 4, y + 64, 0);
        }
    }

    @Override
    public void renderBg(PoseStack poseStack, int mousX, int mousY, float parTick) {
        super.renderBg(poseStack, mousX, mousY, parTick);
        if (canAuthoritySetting()) {
            drawDarkBox(poseStack, x + 3, y + 73, 86, 46);
            drawDarkBox(poseStack, x + 101, y + 72, 95, 29);
        }
    }

}
