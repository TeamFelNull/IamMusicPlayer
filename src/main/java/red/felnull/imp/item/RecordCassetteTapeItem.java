package red.felnull.imp.item;

import net.minecraft.item.ItemStack;

public class RecordCassetteTapeItem extends CassetteTapeItem {

    public RecordCassetteTapeItem(Properties properties) {
        super(properties);

    }

    @Override
    public boolean canWrite(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canOverwrite() {
        return false;
    }

}
