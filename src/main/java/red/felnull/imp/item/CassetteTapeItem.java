package red.felnull.imp.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.music.resource.PlayMusic;
import red.felnull.imp.util.ItemHelper;

public class CassetteTapeItem extends Item {
    public CassetteTapeItem(Properties properties) {
        super(properties);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        PlayMusic music = ItemHelper.getPlayMusicByItem(stack);
        if (music != null && !music.getName().isEmpty()) {
            return new TranslationTextComponent("item.iammusicplayer.cassette_tape.write", new StringTextComponent(music.getName()))
                    ;
        }
        return super.getDisplayName(stack);
    }
}
