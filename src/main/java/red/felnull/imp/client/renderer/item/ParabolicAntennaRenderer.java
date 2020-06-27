package red.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import red.felnull.imp.client.renderer.model.ParabolicAntennaBakedModel;
import red.felnull.imp.util.RenderHelper;

public class ParabolicAntennaRenderer extends ItemStackTileEntityRenderer {
    public static IBakedModel pmodel;
    public static IBakedModel pmodel_null;

    TransformType transtyepe;
    private static Minecraft mc = Minecraft.getInstance();

    @Override
    public void func_239207_a_(ItemStack stack, TransformType trtype, MatrixStack matx, IRenderTypeBuffer bff, int p_228364_4_, int p_228364_5_) {
        ItemRenderer ir = mc.getItemRenderer();
        ParabolicAntennaBakedModel md = (ParabolicAntennaBakedModel) ir.getItemModelWithOverrides(stack, null, null);
        transtyepe = trtype;

        RenderHelper.matrixPush(matx);
        RenderHelper.matrixTranslatef(matx, 0.5f, 0.5f, 0.5f);

        if (stack.getDisplayName().getString().toLowerCase().equals("null")) {
            ir.renderItem(stack, transtyepe,
                    isTransTyape(TransformType.FIRST_PERSON_LEFT_HAND)
                            || isTransTyape(TransformType.THIRD_PERSON_LEFT_HAND),
                    matx, bff, p_228364_4_, p_228364_5_,
                    pmodel_null);
        } else {
            ir.renderItem(stack, transtyepe,
                    isTransTyape(TransformType.FIRST_PERSON_LEFT_HAND)
                            || isTransTyape(TransformType.THIRD_PERSON_LEFT_HAND),
                    matx, bff, p_228364_4_, p_228364_5_,
                    pmodel);
        }

        RenderHelper.matrixPop(matx);

    }

    private boolean isTransTyape(TransformType type) {
        return transtyepe == type;
    }
}
