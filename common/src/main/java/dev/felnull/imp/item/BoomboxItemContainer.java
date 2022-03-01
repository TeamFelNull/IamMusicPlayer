package dev.felnull.imp.item;

import dev.felnull.imp.inventory.BoomboxMenu;
import dev.felnull.otyacraftengine.item.ItemContainer;
import dev.felnull.otyacraftengine.item.location.HandItemLocation;
import dev.felnull.otyacraftengine.item.location.IPlayerItemLocation;
import dev.felnull.otyacraftengine.util.OEMenuUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class BoomboxItemContainer extends ItemContainer {

    public BoomboxItemContainer(ItemStack itemStack, IPlayerItemLocation location, int size, String tagName, Function<Player, Boolean> valid) {
        super(itemStack, location, size, tagName, valid);
    }

    @Override
    public void setItem(int i, ItemStack stack) {
        if (i == 0) {
            var data = BoomboxItem.getData(getItemStack());
            data.onCassetteTapeChange(stack, data.getCassetteTape());
        }
        super.setItem(i, stack);
    }

    public static void openContainer(ServerPlayer player, InteractionHand hand, ItemStack stack) {
        var loc = new HandItemLocation(hand);
        OEMenuUtil.openItemMenu(player, createBoomboxMenuProvider(stack, loc, 2, "BoomboxItems", BoomboxMenu::new), loc, stack, 2);
    }

    private static MenuProvider createBoomboxMenuProvider(ItemStack stack, IPlayerItemLocation location, int size, String tagName, MenuFactory factory) {
        var con = new BoomboxItemContainer(stack, location, size, tagName, player -> {
            if (location.getItem(player).isEmpty() || stack.isEmpty())
                return false;
            return location.getItem(player) == stack;
        });
        return new SimpleMenuProvider((i, inventory, player1) -> factory.createMenu(i, inventory, con, BlockPos.ZERO, stack, location), stack.getHoverName());
    }
}
