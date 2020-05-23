package net.morimori.imp.client.renderer.item;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.morimori.imp.client.renderer.model.CassetteBakedModel;
import net.morimori.imp.item.CassetteTapeItem;
import net.morimori.imp.util.RenderHelper;

@SuppressWarnings("deprecation")
public class CassetteItemRenderer extends ItemStackTileEntityRenderer {
	private static Minecraft mc = Minecraft.getInstance();

	public static Map<CassetteTapeItem, IBakedModel> casettomodels = new HashMap<CassetteTapeItem, IBakedModel>();

	TransformType transtyepe;

	@Override
	public void func_228364_a_(ItemStack stack, MatrixStack matx, IRenderTypeBuffer bff,
			int p_228364_4_, int p_228364_5_) {

		ItemRenderer ir = mc.getItemRenderer();
		CassetteBakedModel md = (CassetteBakedModel) ir.getItemModelWithOverrides(stack, null, null);
		transtyepe = md.cameraTransformType;
		RenderHelper.matrixPush(matx);

		RenderHelper.matrixTranslatef(matx, 0.5f, 0.5f, 0.5f);

		if (casettomodels.containsKey(stack.getItem())) {
			ir.func_229111_a_(stack, transtyepe,
					isTransTyape(TransformType.FIRST_PERSON_LEFT_HAND)
							|| isTransTyape(TransformType.THIRD_PERSON_LEFT_HAND),
					matx, bff, p_228364_4_, p_228364_5_,
					casettomodels.get(stack.getItem()));

		}



//		FontRenderer fr = mc.fontRenderer;
//		RenderHelper.matrixRotateDegreefY(matx, 180);
//		RenderHelper.matrixRotateDegreefZ(matx, 180);
//		float nazo = (float) (-fr.getStringWidth("test") / 2);
//		fr.func_228079_a_("test", nazo, 0, 0, false, matx.func_227866_c_().func_227870_a_(), bff, false, 0,
//				p_228364_4_);
//
//		ResourceLocation test = new ResourceLocation(IkisugiMusicPlayer.MODID,
//				"textures/gui/container/soundfile_uploader_2.png");
//
//		mc.getTextureManager().bindTexture(test);
//		AbstractGui.fill(matx.func_227866_c_().func_227870_a_(), 12, 149, 12, 12, 256);

		RenderHelper.matrixPop(matx);
	}

	private boolean isTransTyape(TransformType type) {
		return transtyepe == type;
	}
}
