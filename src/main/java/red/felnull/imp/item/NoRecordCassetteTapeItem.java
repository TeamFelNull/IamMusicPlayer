package red.felnull.imp.item;

import net.minecraft.item.ItemStack;

public class NoRecordCassetteTapeItem extends CassetteTapeItem {

	public NoRecordCassetteTapeItem(Properties properties) {
		super(properties);

	}

	@Override
	public boolean canOverwrite() {

		return false;
	}

	@Override
	public ItemStack afterWriting(ItemStack befre) {
		ItemStack outstack = new ItemStack(IMPItems.RECORD_CASSETTE_TAPE, befre.getCount());
		outstack.setAnimationsToGo(befre.getAnimationsToGo());
		if (befre.getTag() != null) {
			outstack.setTag(befre.getTag().copy());
		}
		return outstack;
	}

}
