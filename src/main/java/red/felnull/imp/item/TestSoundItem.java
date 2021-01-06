package red.felnull.imp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import red.felnull.imp.client.music.player.IMusicPlayer;
import red.felnull.imp.client.music.player.LocalFileMusicPlayer;

import java.io.File;


public class TestSoundItem extends Item {
    public TestSoundItem(Properties properties) {
        super(properties);
    }

    public static IMusicPlayer imp;

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) {
            long frist = System.currentTimeMillis();
            try {
                if (imp == null) {
                    imp = new LocalFileMusicPlayer(new File("C:\\Users\\MORI\\Music\\playlist\\銀の龍の背に乗って.mp3"));
                    playerIn.sendStatusMessage(new StringTextComponent("Create Instance"), false);
                } else {

                    if (!playerIn.isSneaking()) {
                        imp.play();
                        playerIn.sendStatusMessage(new StringTextComponent("Play"), false);
                    }else {
                        imp.ready(0);
                        playerIn.sendStatusMessage(new StringTextComponent("Ready"), false);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            playerIn.sendStatusMessage(new StringTextComponent("Time : " + (System.currentTimeMillis() - frist)), false);
        }
        return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
    }
}
