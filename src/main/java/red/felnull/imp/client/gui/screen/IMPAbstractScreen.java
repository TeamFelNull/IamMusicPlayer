package red.felnull.imp.client.gui.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import red.felnull.otyacraftengine.client.gui.screen.AbstractIkisugiContainerScreen;

public abstract class IMPAbstractScreen<T extends Container> extends AbstractIkisugiContainerScreen<T> {
    public IMPAbstractScreen(T screenContainer, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(screenContainer, playerInventory, titleIn);
    }
}
