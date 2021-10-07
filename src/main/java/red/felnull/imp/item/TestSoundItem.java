package red.felnull.imp.item;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import red.felnull.imp.client.music.player.IMusicPlayer;


public class TestSoundItem extends Item {
    public TestSoundItem(Properties properties) {
        super(properties);
    }


    public static IMusicPlayer imp;

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        ItemStack offitemstack = playerIn.getHeldItem(handIn == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);
        if (worldIn.isRemote) {

        }
        return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
    }
}
