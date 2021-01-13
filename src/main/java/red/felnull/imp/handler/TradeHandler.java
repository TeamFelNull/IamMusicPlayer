package red.felnull.imp.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import red.felnull.imp.item.IMPItems;

public class TradeHandler {
    @SubscribeEvent
    public static void onWandererTrade(WandererTradesEvent e) {
        e.getRareTrades().add(new BasicTrade(31, new ItemStack(IMPItems.KAMESUTA_ANTENNA), 1, 9));
    }
}
