package red.felnull.imp.client.gui.components.monitor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.imp.client.gui.screen.MusicSharingDeviceScreen;
import red.felnull.imp.item.IMPItems;

public class NoAntennaMonitor extends MSDBaseMonitor {
    private ItemStack antenna;
    protected ItemRenderer itemRenderer;

    public NoAntennaMonitor(MusicSharingDeviceBlockEntity.Screen msdScreen, MusicSharingDeviceScreen parentScreen, int x, int y, int width, int height) {
        super(new TranslatableComponent("imp.msdscreen.noantenna"), msdScreen, parentScreen, x, y, width, height);
        this.renderHeader = false;
    }

    @Override
    public void init() {
        super.init();
        antenna = new ItemStack(IMPItems.PARABOLIC_ANTENNA);
        itemRenderer = getMinecraft().getItemRenderer();
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        this.itemRenderer.blitOffset = 100.0F;
        this.itemRenderer.renderAndDecorateItem(antenna, x + (width / 2) - 16, y + (height / 2) - 16);
        this.itemRenderer.blitOffset = 0.0F;
    }
}
