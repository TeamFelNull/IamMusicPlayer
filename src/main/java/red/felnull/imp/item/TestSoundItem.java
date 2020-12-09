package red.felnull.imp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import red.felnull.imp.client.music.LocalFileMusicPlayer;

import java.nio.file.Paths;

public class TestSoundItem extends Item {
    public TestSoundItem(Properties properties) {
        super(properties);
    }

    public static LocalFileMusicPlayer player = null;

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) {
            try {
                if (player == null) {
                    player = new LocalFileMusicPlayer(Paths.get("C:\\Users\\MORI\\Music\\playlist\\銀の龍の背に乗って  中島みゆき (Cover) [高音質] フル.mp3").toFile());
                }

                if (!playerIn.isSneaking()) {

                    long time = 0;

                    try {
                        time = Long.valueOf(itemstack.getDisplayName().getString());
                    } catch (Exception ex) {

                    }

                    player.play(time);
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
