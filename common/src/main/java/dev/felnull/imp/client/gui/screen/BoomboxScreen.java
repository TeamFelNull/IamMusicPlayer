package dev.felnull.imp.client.gui.screen;

import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.blockentity.BoomboxBlockEntity;
import dev.felnull.imp.client.gui.components.BoomboxButton;
import dev.felnull.imp.inventory.BoomboxMenu;
import dev.felnull.otyacraftengine.client.gui.screen.OEItemBEContainerBaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BoomboxScreen extends OEItemBEContainerBaseScreen<BoomboxMenu> {
    private static final Minecraft mc = Minecraft.getInstance();
    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(IamMusicPlayer.MODID, "textures/gui/container/boombox/boombox_base.png");

    public BoomboxScreen(BoomboxMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 214;
        this.imageHeight = 175;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new BoomboxButton(leftPos + 5, topPos + 17, BoomboxBlockEntity.ButtonType.POWER, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.POWER);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19, topPos + 17, BoomboxBlockEntity.ButtonType.RADIO, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.RADIO);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 2, topPos + 17, BoomboxBlockEntity.ButtonType.START, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.START);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 3, topPos + 17, BoomboxBlockEntity.ButtonType.PAUSE, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.PAUSE);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 4, topPos + 17, BoomboxBlockEntity.ButtonType.STOP, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.STOP);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 5, topPos + 17, BoomboxBlockEntity.ButtonType.LOOP, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.LOOP);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 6 + 14, topPos + 17, BoomboxBlockEntity.ButtonType.VOL_DOWN, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.VOL_DOWN);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 7 + 14, topPos + 17, BoomboxBlockEntity.ButtonType.VOL_UP, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.VOL_UP);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 8 + 14, topPos + 17, BoomboxBlockEntity.ButtonType.VOL_MUTE, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.VOL_MUTE);
        }, this::getButtons));

        this.addRenderableWidget(new BoomboxButton(leftPos + 5 + 19 * 9 + 14, topPos + 17, BoomboxBlockEntity.ButtonType.VOL_MAX, n -> {
            insPressButton(BoomboxBlockEntity.ButtonType.VOL_MAX);
        }, this::getButtons));
    }

    @Override
    protected ResourceLocation getBackGrandTexture() {
        return BG_TEXTURE;
    }

    private BoomboxBlockEntity.Buttons getButtons() {
        if (isBlock()) {
            if (getBlockEntity() instanceof BoomboxBlockEntity boomboxBlockEntity)
                return boomboxBlockEntity.getButtons();
        }
        return BoomboxBlockEntity.Buttons.EMPTY;
    }

    private void insPressButton(BoomboxBlockEntity.ButtonType type) {
        var tag = new CompoundTag();
        tag.putString("Type", type.getName());
        instruction("ButtonsPress", 0, tag);
    }
}
