package red.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.client.renderer.item.ICustomBEWLRenderer;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class CassetteTapeItemRenderer implements ICustomBEWLRenderer {
    private static final ResourceLocation BASE_NOMAL_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/base_nomal");
    private static final ResourceLocation BASE_GLASS_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/base_glass");
    private static final ResourceLocation TAPE_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape");
    private static final ResourceLocation TAPE_CONECTER = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_conecter");
    private static final ResourceLocation GLASS_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/glass");
    private static final ResourceLocation TAPE_CORE_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_core");
    private static final ResourceLocation TAPE_CORE_AROUND_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_core_around");
    private static final ResourceLocation TAPE_ROLL_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_roll");

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        long tim = System.currentTimeMillis() % 3000;
        float par = (float) tim / 3000f;

        VertexConsumer ivb = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        renderBase(poseStack, ivb, itemStack, i, i1);

        BakedModel glassModel = IKSGRenderUtil.getBakedModel(GLASS_MODEL);
        poseStack.pushPose();
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, 3, 0, 2.25d);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, glassModel, i, i1);
        poseStack.popPose();

        renderTapeRoll(poseStack, par * 10f, 1 - par, ivb, 7.25d, -0.01f, 2.5d, i, i1);
        renderTapeRoll(poseStack, par * 10f, par, ivb, 1.75d, -0.01f, 2.5d, i, i1);

        BakedModel tapeModel = IKSGRenderUtil.getBakedModel(TAPE_MODEL);
        poseStack.pushPose();
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, 0.975d, 0.25d, 0.275d);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, tapeModel, i, i1);
        poseStack.popPose();

        renderTapeConecter(poseStack, 22 - 46 * par, ivb, 0.975d, 0.25d, 0.8d, i, i1);
        renderTapeConecter(poseStack, 22 - 46 * par, ivb, 9d, 0.25d, 0.8d, i, i1);
    }

    private static void renderBase(PoseStack poseStack, VertexConsumer ivb, ItemStack stack, int i, int i1) {
        BakedModel bakedModel = IKSGRenderUtil.getBakedModel(BASE_GLASS_MODEL);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, bakedModel, i, i1);

    }


    private static void renderTapeConecter(PoseStack poseStack, float angle, VertexConsumer ivb, double x, double y, double z, int i, int i1) {
        BakedModel tapeConecterModel = IKSGRenderUtil.getBakedModel(TAPE_CONECTER);
        poseStack.pushPose();
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, x, y, z);
        float f = 0.025f / 2f;
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, f, f, f);
        IKSGRenderUtil.matrixRotateDegreefY(poseStack, angle);
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, -f, -f, -f);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, tapeConecterModel, i, i1);
        poseStack.popPose();
    }

    private static void renderTapeRoll(PoseStack poseStack, float par, float roll, VertexConsumer ivb, double x, double y, double z, int i, int i1) {
        roll = Mth.clamp(roll, 0, 1);
        BakedModel tapeCoreModel = IKSGRenderUtil.getBakedModel(TAPE_CORE_MODEL);
        BakedModel tapeCoreAroundModel = IKSGRenderUtil.getBakedModel(TAPE_CORE_AROUND_MODEL);
        BakedModel tapeRollModel = IKSGRenderUtil.getBakedModel(TAPE_ROLL_MODEL);

        poseStack.pushPose();
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, x, y, z);
        poseStack.scale(1.25f, 1.05f, 1.25f);
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, 0.4, 0, 0.4);
        IKSGRenderUtil.matrixRotateDegreefY(poseStack, par * 360f);
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, -0.4, 0, -0.4);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, tapeCoreModel, i, i1);
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, 0, 0.25, 0);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, tapeCoreAroundModel, i, i1);
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, 0, 0.125f / 2f, 0);
        float rollPar = roll + 0.5f;
        for (int j = 1; j <= Math.ceil(rollPar); j++) {
            poseStack.pushPose();
            IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, 0.4, 0, 0.4);
            float sc = Math.min(rollPar, j);
            poseStack.scale(sc, 1, sc);
            IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, -0.4, 0, -0.4);
            IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, tapeRollModel, i, i1);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

}




