package red.felnull.imp.item;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import red.felnull.imp.client.renderer.item.CassetteItemRenderer;
import red.felnull.imp.client.renderer.model.CassetteBakedModel;

public class CassetteTapeItem extends Item {

    public CassetteTapeItem(Properties properties) {
        super(properties.setISTER(() -> CassetteItemRenderer::new));

    }

    public boolean canWrite(ItemStack stack) {
        return true;
    }

    public boolean canOverwrite() {

        return true;
    }

    public ItemStack afterWriting(ItemStack befre) {
        return befre;
    }

    /*
        @OnlyIn(Dist.CLIENT)
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                ITooltipFlag flagIn) {
            SoundData.addSoundDataTooltip(stack, tooltip);
        }
        public ITextComponent getDisplayName(ItemStack stack) {
        return SoundHelper.isWritedSound(stack)
                ? new TranslationTextComponent("item.iammusicplayer.cassette_tape.written",
                        SoundHelper.getSoundName(
                                stack))
                : super.getDisplayName(stack);
    }
*/
    public CassetteBakedModel getModel(IBakedModel moto) {

        return new CassetteBakedModel(moto, this);
    }

}
