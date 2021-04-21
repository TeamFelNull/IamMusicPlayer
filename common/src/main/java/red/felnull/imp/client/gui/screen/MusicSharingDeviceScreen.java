package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import red.felnull.imp.inventory.MusicSharingDeviceMenu;
import red.felnull.otyacraftengine.client.gui.screen.IkisugiContainerScreen;

public class MusicSharingDeviceScreen extends IkisugiContainerScreen<MusicSharingDeviceMenu> {
    public MusicSharingDeviceScreen(MusicSharingDeviceMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {

    }
}
