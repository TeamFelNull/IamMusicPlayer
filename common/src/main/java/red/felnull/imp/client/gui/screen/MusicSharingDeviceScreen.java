package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.inventory.MusicSharingDeviceMenu;
import red.felnull.otyacraftengine.client.gui.screen.IkisugiContainerScreen;

public class MusicSharingDeviceScreen extends IkisugiContainerScreen<MusicSharingDeviceMenu> {
    private static final ResourceLocation MSD_LOCATION = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/music_sharing_device.png");

    public MusicSharingDeviceScreen(MusicSharingDeviceMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 215;
        this.imageHeight = 242;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MSD_LOCATION);
        this.blit(poseStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }


}
