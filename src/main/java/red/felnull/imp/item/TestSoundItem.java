package red.felnull.imp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import red.felnull.imp.client.music.IMusicPlayer;
import red.felnull.imp.client.music.LocalFileMusicPlayer;
import red.felnull.imp.client.music.URLNotStreamMusicPlayer;

import java.net.URL;
import java.nio.file.Paths;

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
                    player = new LocalFileMusicPlayer(Paths.get("C:\\Users\\MORI\\Music\\playlist\\銀の龍の背に乗って  中島みゆき (Cover) [高音質] フル.mp3").toFile());
                    //  player = new URLNotStreamMusicPlayer(new URL("https://www.dropbox.com/s/l3bwr7yc53x70kl/nyan.mp3?dl=1"));
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
