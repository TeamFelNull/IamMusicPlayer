package red.felnull.imp.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CassetteTapeItem extends Item {
    public CassetteTapeItem(Properties properties) {
        super(properties);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null && !tag.getCompound("music").getString("name").isEmpty()) {
            return new TranslationTextComponent("item.iammusicplayer.cassette_tape.write", tag.getCompound("music").getString("name"));
        }
        return super.getDisplayName(stack);
    }
}
