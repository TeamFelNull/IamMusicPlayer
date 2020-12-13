package red.felnull.imp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import red.felnull.imp.client.music.IMusicPlayer;
import red.felnull.imp.client.music.WorldFileMusicPlayer;


public class TestSoundItem extends Item {
    public TestSoundItem(Properties properties) {
        super(properties);
    }

    public static IMusicPlayer player = null;

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) {
            try {
                if (player == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                player = new WorldFileMusicPlayer("7e23db29-0575-4be5-aa3c-acefec831e77");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                if (!playerIn.isSneaking()) {

                    long time = 0;

                    try {
                        time = Long.valueOf(itemstack.getDisplayName().getString());
                    } catch (Exception ex) {

                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (player != null)
                                    player.play(20 * 1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } else {
                    player.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
    }
}
