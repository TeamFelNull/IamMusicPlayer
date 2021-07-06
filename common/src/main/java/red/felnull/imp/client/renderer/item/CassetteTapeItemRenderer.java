package red.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.client.renderer.item.ICustomBEWLRenderer;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class CassetteTapeItemRenderer implements ICustomBEWLRenderer {
    private static final ResourceLocation CT_BASE_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/base");
    private static final ResourceLocation CT_GLASS_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/glass");

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        BakedModel bakedModel = IKSGRenderUtil.getBakedModel(CT_BASE_MODEL);
        BakedModel glassModel = IKSGRenderUtil.getBakedModel(CT_GLASS_MODEL);
        VertexConsumer ivb = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, bakedModel, i, i1);

        poseStack.pushPose();
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, 3, 0, 2.25d);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, glassModel, i, i1);
        poseStack.popPose();
    }
}
