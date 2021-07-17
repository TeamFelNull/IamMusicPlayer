package red.felnull.imp.client.gui.screen.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.data.IMPSyncClientManager;
import red.felnull.imp.client.data.SimplePlayerData;
import red.felnull.imp.client.gui.components.AdminInfoData;
import red.felnull.imp.client.gui.components.AdminPlayersFixedButtonsList;
import red.felnull.imp.client.gui.components.PlayersFixedButtonsList;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.data.resource.AdministratorInformation;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.imp.music.resource.MusicPlayList;
import red.felnull.imp.music.resource.simple.SimpleMusicPlayList;
import red.felnull.otyacraftengine.client.util.IKSGClientUtil;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.util.IKSGPlayerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaylistDetailsMonitor extends DetailsEditBaseMonitor {
    private final List<SimplePlayerData> playerInfos = new ArrayList<>();
    private final List<AdminInfoData> selectedAdminPlayer = new ArrayList<>();
    private PlayersFixedButtonsList playersFixedButtonsList;
    private PlayersFixedButtonsList selectPlayersFixedButtonsList;
    private AdminPlayersFixedButtonsList adminPlayersFixedButtonsList;


    public PlaylistDetailsMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdMonitor.details"), msdScreen, parentScreen, x, y, width, height);
        UUID pil = getMinecraft().player.getGameProfile().getId();
    }

    @Override
    public void init() {
        super.init();

        getParentScreen().selectPlayList.getPlayers().stream().filter(n -> !n.equals(getMinecraft().player.getGameProfile().getId())).forEach(n -> {
            SimplePlayerData data = new SimplePlayerData(IKSGClientUtil.getPlayerNameByUUID(n).equals(IKSGPlayerUtil.getFakePlayerName()) ? n.toString() : IKSGClientUtil.getPlayerNameByUUID(n), n);
            selectedAdminPlayer.add(new AdminInfoData(data, getParentScreen().selectPlayList.getAdministrators().contains(n) ? AdministratorInformation.AuthorityType.ADMINISTRATOR : AdministratorInformation.AuthorityType.READ_ONLY));
        });
        selectedAdminPlayer.clear();
        SimpleMusicPlayList playList = getParentScreen().selectPlayList;
        playList.getBaned().forEach(n -> {
            SimplePlayerData data = new SimplePlayerData(IKSGClientUtil.getPlayerNameByUUID(n).equals(IKSGPlayerUtil.getFakePlayerName()) ? n.toString() : IKSGClientUtil.getPlayerNameByUUID(n), n);
            selectedAdminPlayer.add(new AdminInfoData(data, AdministratorInformation.AuthorityType.BAN));
        });
        playList.getAdministrators().forEach(n -> {
            SimplePlayerData data = new SimplePlayerData(IKSGClientUtil.getPlayerNameByUUID(n).equals(IKSGPlayerUtil.getFakePlayerName()) ? n.toString() : IKSGClientUtil.getPlayerNameByUUID(n), n);
            selectedAdminPlayer.add(new AdminInfoData(data, playList.getOwner().equals(n) ? AdministratorInformation.AuthorityType.OWNER : AdministratorInformation.AuthorityType.ADMINISTRATOR));
        });
        playList.getPlayers().forEach(n -> {
            if (selectedAdminPlayer.stream().anyMatch(m -> m.playerInfo().id().equals(n)))
                return;
            SimplePlayerData data = new SimplePlayerData(IKSGClientUtil.getPlayerNameByUUID(n).equals(IKSGPlayerUtil.getFakePlayerName()) ? n.toString() : IKSGClientUtil.getPlayerNameByUUID(n), n);
            selectedAdminPlayer.add(new AdminInfoData(data, AdministratorInformation.AuthorityType.READ_ONLY));
        });

        playersFixedButtonsList = this.addRenderableWidget(new PlayersFixedButtonsList(x + 4, y + 74, 191, 44, 4, new TextComponent("Players"), playerInfos, n -> {

        }));

        selectPlayersFixedButtonsList = this.addRenderableWidget(new PlayersFixedButtonsList(x + 4, y + 74, 84, 44, 4, new TextComponent("Players"), playerInfos, n -> {
            selectedAdminPlayer.add(new AdminInfoData(n.item(), AdministratorInformation.AuthorityType.ADMINISTRATOR));
        }));
        adminPlayersFixedButtonsList = this.addRenderableWidget(new AdminPlayersFixedButtonsList(x + 102, y + 73, 93, 27, 3, new TextComponent("Admin Players"), selectedAdminPlayer, n -> new TextComponent(n.playerInfo().name()), n -> {
            if (n.item().type() == AdministratorInformation.AuthorityType.READ_ONLY) {
                selectedAdminPlayer.set(n.itemNum(), new AdminInfoData(n.item().playerInfo(), AdministratorInformation.AuthorityType.ADMINISTRATOR));
            } else if (n.item().type() == AdministratorInformation.AuthorityType.ADMINISTRATOR) {
                selectedAdminPlayer.set(n.itemNum(), new AdminInfoData(n.item().playerInfo(), AdministratorInformation.AuthorityType.BAN));
            } else if (n.item().type() == AdministratorInformation.AuthorityType.BAN) {
                selectedAdminPlayer.remove(n.itemNum());
            }
        }));
    }

    @Override
    public void tick() {
        super.tick();
        playerInfos.clear();

        if (canChangeable()) {
            List<SimplePlayerData> pls = new ArrayList<>();
            pls.addAll(IMPSyncClientManager.getInstance().getOnlinePlayers());
            pls.addAll(getParentScreen().selectPlayList.getPlayers().stream().map(n -> new SimplePlayerData(IKSGClientUtil.getPlayerNameByUUID(n).equals(IKSGPlayerUtil.getFakePlayerName()) ? n.toString() : IKSGClientUtil.getPlayerNameByUUID(n), n)).toList());
            playerInfos.addAll(pls.stream().filter(n -> !n.id().equals(getMinecraft().player.getGameProfile().getId())).filter(n -> selectedAdminPlayer.stream().noneMatch(m -> m.playerInfo().id().equals(n.id()))).toList());
        } else {
            playerInfos.addAll(getParentScreen().selectPlayList.getPlayers().stream().map(n -> new SimplePlayerData(IKSGClientUtil.getPlayerNameByUUID(n).equals(IKSGPlayerUtil.getFakePlayerName()) ? n.toString() : IKSGClientUtil.getPlayerNameByUUID(n), n)).toList());
        }

        playersFixedButtonsList.visible = playersFixedButtonsList.active = !canChangeable();
        adminPlayersFixedButtonsList.visible = adminPlayersFixedButtonsList.active = selectPlayersFixedButtonsList.visible = selectPlayersFixedButtonsList.active = canChangeable();
    }

    @Override
    protected UUID getCurrentDetailsObjectUUID() {
        return getParentScreen().selectPlayList.getUUID();
    }

    @Override
    protected boolean isMaintain() {
        return getCurrentDetailsObjectUUID() != null && !getCurrentDetailsObjectUUID().equals(MusicPlayList.ALL.getUUID());
    }

    @Override
    protected ImageInfo getDefaultImage() {
        return getParentScreen().selectPlayList.getImage();
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        if (!canChangeable()) {
            drawPrettyString(poseStack, new TextComponent(getParentScreen().selectPlayList.getName()), x + 46, y + 24, 0);
        } else {
            IKSGRenderUtil.drawTexture(MSDBaseMonitor.MSD_WIDGETS, poseStack, x + 90, y + 83, 7, 62, 10, 7);
            drawPrettyString(poseStack, new TranslatableComponent("imp.msdText.playerAuthority"), x + 4, y + 64, 0);
        }
    }

    @Override
    public void renderBg(PoseStack poseStack, int mousX, int mousY, float parTick) {
        super.renderBg(poseStack, mousX, mousY, parTick);
        if (!canChangeable()) {
            drawDarkBox(poseStack, x + 3, y + 73, 193, 46);
        } else {
            drawDarkBox(poseStack, x + 3, y + 73, 86, 46);
            drawDarkBox(poseStack, x + 101, y + 72, 95, 29);
        }
    }

    @Override
    protected String getDefaultName() {
        return getParentScreen().selectPlayList.getName();
    }

    @Override
    protected boolean canChangeable() {
        return getParentScreen().selectPlayList.getAdministrators().contains(getMinecraft().player.getGameProfile().getId());
    }
}
