package red.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.renderer.PlayImageRenderer;
import red.felnull.imp.data.resource.ImageInfo;
import red.felnull.imp.item.CassetteTapeItem;
import red.felnull.otyacraftengine.client.renderer.item.ICustomBEWLRenderer;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class CassetteTapeItemRenderer implements ICustomBEWLRenderer {
    private static final ResourceLocation BASE_NORMAL_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/base_nomal");
    private static final ResourceLocation BASE_GLASS_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/base_glass");
    private static final ResourceLocation BASE_GLASS_COLOR_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/base_glass_color");
    private static final ResourceLocation TAPE_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape");
    private static final ResourceLocation TAPE_CONECTER = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_conecter");
    private static final ResourceLocation GLASS_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/glass");
    private static final ResourceLocation TAPE_CORE_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_core");
    private static final ResourceLocation TAPE_CORE_AROUND_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_core_around");
    private static final ResourceLocation TAPE_ROLL_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/tape_roll");
    private static final ResourceLocation LABEL_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "item/cassette_tape/label");

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        long tim = System.currentTimeMillis() % 3000;
        float par = (float) tim / 3000f;

        VertexConsumer ivb = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        renderBase(poseStack, ivb, multiBufferSource, itemStack, i, i1);

        BakedModel glassModel = IKSGRenderUtil.getBakedModel(GLASS_MODEL);
        poseStack.pushPose();
        IKSGRenderUtil.poseTrans16(poseStack, 3, 0, 2.25d);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, glassModel, i, i1);
        poseStack.popPose();

        renderTapeRoll(poseStack, par * 10f, 1 - par, ivb, 7.25d, -0.01f, 2.5d, i, i1);
        renderTapeRoll(poseStack, par * 10f, par, ivb, 1.75d, -0.01f, 2.5d, i, i1);

        BakedModel tapeModel = IKSGRenderUtil.getBakedModel(TAPE_MODEL);
        poseStack.pushPose();
        IKSGRenderUtil.poseTrans16(poseStack, 0.975d, 0.25d, 0.275d);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, tapeModel, i, i1);
        poseStack.popPose();

        renderTapeConecter(poseStack, 22 - 46 * par, ivb, 0.975d, 0.25d, 0.8d, i, i1);
        renderTapeConecter(poseStack, 22 - 46 * par, ivb, 9d, 0.25d, 0.8d, i, i1);

        renderMusicInfo(poseStack, ivb, multiBufferSource, itemStack, i, i1);
    }

    private static void renderMusicInfo(PoseStack poseStack, VertexConsumer ivb, MultiBufferSource multiBufferSource, ItemStack stack, int i, int i1) {
        BakedModel labelModel = IKSGRenderUtil.getBakedModel(LABEL_MODEL);
        poseStack.pushPose();
        IKSGRenderUtil.poseTrans16(poseStack, 3d, 1d, 4d);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, labelModel, i, i1);
        poseStack.translate(0, (1f / 16f) * 0.025f + Mth.EPSILON, 0);

        PlayImageRenderer renderer = PlayImageRenderer.getInstance();
        ImageInfo image1 = new ImageInfo(ImageInfo.ImageType.STRING, "1");
        ImageInfo image2 = new ImageInfo(ImageInfo.ImageType.STRING, "2");
        ImageInfo image3 = new ImageInfo(ImageInfo.ImageType.STRING, "3");

        {
            float size = (1f / 16f) * 1.025f;
            float x = 2.8f;
            float y = 0.55f;
            renderer.renderSprite(image1, poseStack, multiBufferSource, -(size + (1f / 16f) * x), (1f / 16f) * y, 0, -90, 0, 180, size, i, i1);
        }
        {
            float size = (1f / 16f) * 0.4f;
            float x = 2.175f;
            float y = 1.175f;
            renderer.renderSprite(image2, poseStack, multiBufferSource, -(size + (1f / 16f) * x), (1f / 16f) * y, 0, -90, 0, 180, size, i, i1);
        }
        {
            float size = (1f / 16f) * 0.4f;
            float x = 2.175f;
            float y = 0.55f;
            renderer.renderSprite(image3, poseStack, multiBufferSource, -(size + (1f / 16f) * x), (1f / 16f) * y, 0, -90, 0, 180, size, i, i1);
        }
        poseStack.pushPose();
        IKSGRenderUtil.poseRotateX(poseStack, 90f);
        IKSGRenderUtil.poseRotateY(poseStack, 180);
        IKSGRenderUtil.renderTextSprite(poseStack, multiBufferSource, new TextComponent("TEST1"), -(1f / 16f) * 3.85f, (1f / 16f) * 0.18f, 0, 0.15f, 0, 0);
        IKSGRenderUtil.renderTextSprite(poseStack, multiBufferSource, new TextComponent("TEST2"), -(1f / 16f) * 2.05f, (1f / 16f) * 1.2f, 0, 0.18f, 0, 0);
        IKSGRenderUtil.renderTextSprite(poseStack, multiBufferSource, new TextComponent("TEST3"), -(1f / 16f) * 2.05f, (1f / 16f) * 0.575f, 0, 0.18f, 0, 0);
        IKSGRenderUtil.renderTextSprite(poseStack, multiBufferSource, new TextComponent("+19..."), -(1f / 16f) * 0.95f, (1f / 16f) * 0.2f, 0, 0.15f, 0, 0);
        poseStack.popPose();

        poseStack.popPose();
    }

    private static void renderBase(PoseStack poseStack, VertexConsumer ivb, MultiBufferSource multiBufferSource, ItemStack stack, int i, int i1) {
        if (stack.getItem() instanceof CassetteTapeItem) {
            CassetteTapeItem.BaseType type = ((CassetteTapeItem) stack.getItem()).getType();
            ResourceLocation location = BASE_NORMAL_MODEL;
            int color = ((CassetteTapeItem) stack.getItem()).hasCustomColor(stack) ? ((CassetteTapeItem) stack.getItem()).getColor(stack) : 0x1a1a1a;
            if (type == CassetteTapeItem.BaseType.GLASS) {
                location = ((CassetteTapeItem) stack.getItem()).hasCustomColor(stack) ? BASE_GLASS_COLOR_MODEL : BASE_GLASS_MODEL;
                ivb = multiBufferSource.getBuffer(Sheets.translucentCullBlockSheet());
            }
            BakedModel bakedModel = IKSGRenderUtil.getBakedModel(location);
            IKSGRenderUtil.renderColorBakedModel(poseStack, ivb, null, bakedModel, i, i1, color);
        }
    }


    private static void renderTapeConecter(PoseStack poseStack, float angle, VertexConsumer ivb, double x, double y, double z, int i, int i1) {
        BakedModel tapeConecterModel = IKSGRenderUtil.getBakedModel(TAPE_CONECTER);
        poseStack.pushPose();
        IKSGRenderUtil.poseTrans16(poseStack, x, y, z);
        float f = 0.025f / 2f;
        IKSGRenderUtil.poseTrans16(poseStack, f, f, f);
        IKSGRenderUtil.poseRotateY(poseStack, angle);
        IKSGRenderUtil.poseTrans16(poseStack, -f, -f, -f);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, tapeConecterModel, i, i1);
        poseStack.popPose();
    }

    private static void renderTapeRoll(PoseStack poseStack, float par, float roll, VertexConsumer ivb, double x, double y, double z, int i, int i1) {
        roll = Mth.clamp(roll, 0, 1);
        BakedModel tapeCoreModel = IKSGRenderUtil.getBakedModel(TAPE_CORE_MODEL);
        BakedModel tapeCoreAroundModel = IKSGRenderUtil.getBakedModel(TAPE_CORE_AROUND_MODEL);
        BakedModel tapeRollModel = IKSGRenderUtil.getBakedModel(TAPE_ROLL_MODEL);

        poseStack.pushPose();
        IKSGRenderUtil.poseTrans16(poseStack, x, y, z);
        poseStack.scale(1.25f, 1.05f, 1.25f);
        IKSGRenderUtil.poseTrans16(poseStack, 0.4, 0, 0.4);
        IKSGRenderUtil.poseRotateY(poseStack, par * 360f);
        IKSGRenderUtil.poseTrans16(poseStack, -0.4, 0, -0.4);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, tapeCoreModel, i, i1);
        IKSGRenderUtil.poseTrans16(poseStack, 0, 0.25, 0);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, tapeCoreAroundModel, i, i1);
        IKSGRenderUtil.poseTrans16(poseStack, 0, 0.125f / 2f, 0);
        float rollPar = roll + 0.5f;
        for (int j = 1; j <= Math.ceil(rollPar); j++) {
            poseStack.pushPose();
            IKSGRenderUtil.poseTrans16(poseStack, 0.4, 0, 0.4);
            float sc = Math.min(rollPar, j);
            poseStack.scale(sc, 1, sc);
            IKSGRenderUtil.poseTrans16(poseStack, -0.4, 0, -0.4);
            IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, tapeRollModel, i, i1);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

}




