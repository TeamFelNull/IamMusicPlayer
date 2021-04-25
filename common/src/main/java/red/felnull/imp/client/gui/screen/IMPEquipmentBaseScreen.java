package red.felnull.imp.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.IMPEquipmentBaseBlock;
import red.felnull.otyacraftengine.client.gui.screen.IkisugiContainerScreen;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;
import red.felnull.otyacraftengine.inventory.IkisugiContainerMenu;

public abstract class IMPEquipmentBaseScreen<T extends IkisugiContainerMenu> extends IkisugiContainerScreen<T> {
    public static final ResourceLocation EQUIPMENT_WIDGETS_TEXTURES = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/equipment_widgets.png");

    public IMPEquipmentBaseScreen(T abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.addButton(new PowerButton(leftPos + imageWidth - 34, topPos + imageHeight - 40, n -> insPower(!isPowerOn())));
    }

    protected void insPower(boolean on) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("On", on);
        this.instruction("Power", tag);
    }

    public boolean isPowerOn() {
        return getBlockEntity().getBlockState().getValue(IMPEquipmentBaseBlock.ON);
    }

    private class PowerButton extends Button {
        public PowerButton(int x, int y, OnPress onPress) {
            super(x, y, 20, 20, new TranslatableComponent("imp.msd.power.narrator"), onPress);
        }

        @Override
        public void renderButton(PoseStack poseStack, int i, int j, float f) {
            IKSGRenderUtil.drawBindTextuer(EQUIPMENT_WIDGETS_TEXTURES, poseStack, this.x, this.y, isPowerOn() ? 20 : 0, this.isHovered() ? 20 : 0, 20, 20, 256, 256);
        }
    }
}
