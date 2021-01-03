package red.felnull.imp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;


public class TestSoundItem extends Item {
    public TestSoundItem(Properties properties) {
        super(properties);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) {
          /*  try {
                //       WorldSoundRinger.instance().addMusicPlayer(UUID.randomUUID(), new LocalFileMusicPlayer(new File("C:\\Users\\MORI\\Music\\playlist\\Brain Power.mp3")));
                // ClientWorldSoundManager.instance().addMusicPlayer(UUID.randomUUID(), new YoutubeMusicPlayer("l5NPZN2cUlQ"));
                ClientWorldMusicManager.instance().addMusicPlayer(UUID.randomUUID(), new MusicRinger(new PlayMusic("66537216-f3cc-41e3-9ee4-dcac88295a44", new CompoundNBT()), playerIn.getPositionVec()));

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } else {
            playerIn.sendStatusMessage(new StringTextComponent(worldIn.getDimensionKey().getLocation().toString()), false);
        }
        return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
    }
}
