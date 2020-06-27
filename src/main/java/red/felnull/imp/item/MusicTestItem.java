package red.felnull.imp.item;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import red.felnull.imp.sound.ClientSoundPlayer;
import red.felnull.imp.sound.SoundRinger;
import red.felnull.imp.util.FileHelper;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class MusicTestItem extends Item {

    public MusicTestItem(Properties properties) {
        super(properties);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack item = playerIn.getHeldItem(hand);

        if (worldIn.isRemote) {

            try {
                Player ad = new Player(new BufferedInputStream(new FileInputStream("D:\\minecraft\\versionservertest\\1.16.1\\client\\iammusicplayer\\musics\\Brain Power.mp3")));
                ad.play();
            } catch (Exception e) {

            }

        }

        return ActionResult.func_233538_a_(item, worldIn.isRemote);
    }
}
