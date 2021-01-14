package red.felnull.imp.client.gui.screen;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import red.felnull.otyacraftengine.client.gui.widget.StringImageButton;
import red.felnull.otyacraftengine.client.util.IKSGScreenUtil;
import red.felnull.otyacraftengine.util.IKSGStyles;

public abstract class IMPAbstractMonitorEquipmentScreen<T extends Container> extends IMPAbstractEquipmentScreen<T> implements IMonitorScreen {
    public IMPAbstractMonitorEquipmentScreen(T screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
    }

    protected StringImageButton addSmartStringButton(IFormattableTextComponent text, int x, int y, Button.IPressable onPressIn) {
        return addStringImageButton(text, x, y, 48, 15, 40, 0, onPressIn);
    }

    protected StringImageButton addStringImageButton(IFormattableTextComponent text, int x, int y, int w, int h, int tsx, int tsy, Button.IPressable onPressIn) {
        return addStringImageButton(text, EQUIPMENT_WIDGETS_TEXTURES, x, y, w, h, tsx, tsy, onPressIn);
    }

    protected StringImageButton addStringImageButton(IFormattableTextComponent text, ResourceLocation location, int x, int y, int w, int h, int tsx, int tsy, Button.IPressable onPressIn) {
        StringImageButton sib = this.addWidgetByIKSG(new StringImageButton(getMonitorStartX() + x, getMonitorStartY() + y, w, h, tsx, tsy, h, location, onPressIn, IKSGStyles.withStyle(text, smart_fontStyle)));
        sib.setSizeAdjustment(true);
        sib.setShadwString(false);
        sib.setStringColor(0);
        IKSGScreenUtil.setVisible(sib, false);
        return sib;
    }
}
