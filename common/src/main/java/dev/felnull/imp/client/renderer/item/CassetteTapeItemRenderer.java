package dev.felnull.imp.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.felnull.imp.client.gui.components.MyPlayListFixedButtonsList;
import dev.felnull.imp.client.model.IMPModels;
import dev.felnull.imp.client.renderer.PlayImageRenderer;
import dev.felnull.imp.item.CassetteTapeItem;
import dev.felnull.imp.music.resource.Music;
import dev.felnull.otyacraftengine.client.model.SpecialModelLoader;
import dev.felnull.otyacraftengine.client.renderer.item.BEWLItemRenderer;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.Date;

public class CassetteTapeItemRenderer implements BEWLItemRenderer {
    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, float v, int i, int i1) {
        float par = CassetteTapeItem.getTapePercentage(itemStack);
        var spml = SpecialModelLoader.getInstance();

        VertexConsumer ivb = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        renderBase(poseStack, ivb, multiBufferSource, itemStack, i, i1);

        BakedModel glassModel = spml.getModel(IMPModels.CASSETTE_TAPE_GLASS_MODEL);
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 3, 0, 2.25d);
        OERenderUtil.renderModel(poseStack, ivb, glassModel, i, i1);
        poseStack.popPose();

        renderTapeRoll(poseStack, par * 10f, 1 - par, ivb, 7.25d, -0.01f, 2.5d, i, i1);
        renderTapeRoll(poseStack, par * 10f, par, ivb, 1.75d, -0.01f, 2.5d, i, i1);

        BakedModel tapeModel = spml.getModel(IMPModels.CASSETTE_TAPE_MODEL);
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 0.975d, 0.25d, 0.275d);
        OERenderUtil.renderModel(poseStack, ivb, tapeModel, i, i1);
        poseStack.popPose();

        renderTapeConecter(poseStack, 22 - 46 * par, ivb, 0.975d, 0.25d, 0.8d, i, i1);
        renderTapeConecter(poseStack, 22 - 46 * par, ivb, 9d, 0.25d, 0.8d, i, i1);

        renderMusicInfo(poseStack, ivb, multiBufferSource, itemStack, i, i1);
    }

    private static void renderMusicInfo(PoseStack poseStack, VertexConsumer ivb, MultiBufferSource multiBufferSource, ItemStack stack, int i, int i1) {

        Music music = CassetteTapeItem.getMusic(stack);

        if (music == null)
            return;

        var spml = SpecialModelLoader.getInstance();
        BakedModel labelModel = spml.getModel(music.getImage().isEmpty() ? IMPModels.CASSETTE_TAPE_LABEL_NO_IMAGE_MODEL : IMPModels.CASSETTE_TAPE_LABEL_MODEL);
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, 3d, 1d, 4d);
        OERenderUtil.renderModel(poseStack, ivb, labelModel, i, i1);
        poseStack.translate(0, (1f / 16f) * 0.025f + OERenderUtil.MIN_BREADTH, 0);

        if (!music.getImage().isEmpty()) {
            float size = (1f / 16f) * 1.025f;
            float x = 2.8f;
            float y = 0.55f;
            poseStack.pushPose();
            OERenderUtil.poseRotateAll(poseStack, -90, 0, 180);
            PlayImageRenderer.getInstance().renderSprite(music.getImage(), poseStack, multiBufferSource, -(size + (1f / 16f) * x), (1f / 16f) * y, 0, size, i, i1);
            poseStack.popPose();
        }

        poseStack.pushPose();
        OERenderUtil.poseRotateX(poseStack, 90f);
        OERenderUtil.poseRotateY(poseStack, 180);
        float x = music.getImage().isEmpty() ? 3.85f : 2.65f;
        OERenderUtil.renderTextSprite(poseStack, multiBufferSource, new TextComponent(MyPlayListFixedButtonsList.dateFormat.format(new Date(music.getCreateDate()))), -(1f / 16f) * 3.85f, (1f / 16f) * 0.18f, 0, 0.15f, 0, 0, i);
        Component namec = new TextComponent(music.getName());
        int ms = music.getImage().isEmpty() ? 120 : 80;
        String dot = "...";
        if (mc.font.width(namec) >= ms) {
            StringBuilder sb = new StringBuilder();
            for (char c : music.getName().toCharArray()) {
                sb.append(c);
                if (mc.font.width(sb.toString()) >= ms - mc.font.width(dot)) {
                    sb.deleteCharAt(sb.length() - 1);
                    break;
                }
            }
            sb.append(dot);
            namec = new TextComponent(sb.toString());
        }
        OERenderUtil.renderTextSprite(poseStack, multiBufferSource, namec, -(1f / 16f) * x, (1f / 16f) * 1.2f, 0, 0.18f, 0, 0, i);
        Component authorc = new TextComponent(music.getAuthor());
        if (mc.font.width(authorc) >= ms) {
            StringBuilder sb = new StringBuilder();
            for (char c : music.getAuthor().toCharArray()) {
                sb.append(c);
                if (mc.font.width(sb.toString()) >= ms - mc.font.width(dot)) {
                    sb.deleteCharAt(sb.length() - 1);
                    break;
                }
            }
            sb.append(dot);
            authorc = new TextComponent(sb.toString());
        }
        OERenderUtil.renderTextSprite(poseStack, multiBufferSource, authorc, -(1f / 16f) * x, (1f / 16f) * 0.575f, 0, 0.18f, 0, 0, i);
        poseStack.popPose();

        poseStack.popPose();
    }

    private static void renderBase(PoseStack poseStack, VertexConsumer ivb, MultiBufferSource multiBufferSource, ItemStack stack, int i, int i1) {
        if (stack.getItem() instanceof CassetteTapeItem) {
            CassetteTapeItem.BaseType type = ((CassetteTapeItem) stack.getItem()).getType();
            ResourceLocation location = IMPModels.CASSETTE_TAPE_BASE_NORMAL_MODEL;
            int color = ((CassetteTapeItem) stack.getItem()).hasCustomColor(stack) ? ((CassetteTapeItem) stack.getItem()).getColor(stack) : 0x1a1a1a;
            if (type == CassetteTapeItem.BaseType.GLASS) {
                location = ((CassetteTapeItem) stack.getItem()).hasCustomColor(stack) ? IMPModels.CASSETTE_TAPE_BASE_GLASS_COLOR_MODEL : IMPModels.CASSETTE_TAPE_BASE_GLASS_MODEL;
                ivb = multiBufferSource.getBuffer(Sheets.translucentCullBlockSheet());
            }
            BakedModel bakedModel = SpecialModelLoader.getInstance().getModel(location);
            OERenderUtil.renderModel(poseStack, ivb, bakedModel, i, i1, color);
        }
    }

    private static void renderTapeConecter(PoseStack poseStack, float angle, VertexConsumer ivb, double x, double y, double z, int i, int i1) {
        var spml = SpecialModelLoader.getInstance();
        BakedModel tapeConecterModel = spml.getModel(IMPModels.CASSETTE_TAPE_CONECTER);
        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, x, y, z);
        float f = 0.025f / 2f;
        OERenderUtil.poseTrans16(poseStack, f, f, f);
        OERenderUtil.poseRotateY(poseStack, angle);
        OERenderUtil.poseTrans16(poseStack, -f, -f, -f);
        OERenderUtil.renderModel(poseStack, ivb, tapeConecterModel, i, i1);
        poseStack.popPose();
    }

    private static void renderTapeRoll(PoseStack poseStack, float par, float roll, VertexConsumer ivb, double x, double y, double z, int i, int i1) {
        roll = Mth.clamp(roll, 0, 1);
        var spml = SpecialModelLoader.getInstance();
        BakedModel tapeCoreModel = spml.getModel(IMPModels.CASSETTE_TAPE_CORE_MODEL);
        BakedModel tapeCoreAroundModel = spml.getModel(IMPModels.CASSETTE_TAPE_CORE_AROUND_MODEL);
        BakedModel tapeRollModel = spml.getModel(IMPModels.CASSETTE_TAPE_ROLL_MODEL);

        poseStack.pushPose();
        OERenderUtil.poseTrans16(poseStack, x, y, z);
        poseStack.scale(1.25f, 1.05f, 1.25f);
        OERenderUtil.poseTrans16(poseStack, 0.4, 0, 0.4);
        OERenderUtil.poseRotateY(poseStack, par * 360f);
        OERenderUtil.poseTrans16(poseStack, -0.4, 0, -0.4);
        OERenderUtil.renderModel(poseStack, ivb, tapeCoreModel, i, i1);
        OERenderUtil.poseTrans16(poseStack, 0, 0.25, 0);
        OERenderUtil.renderModel(poseStack, ivb, tapeCoreAroundModel, i, i1);
        OERenderUtil.poseTrans16(poseStack, 0, 0.125f / 2f, 0);
        float rollPar = roll + 0.5f;
        for (int j = 1; j <= Math.ceil(rollPar); j++) {
            poseStack.pushPose();
            OERenderUtil.poseTrans16(poseStack, 0.4, 0, 0.4);
            float sc = Math.min(rollPar, j);
            poseStack.scale(sc, 1, sc);
            OERenderUtil.poseTrans16(poseStack, -0.4, 0, -0.4);
            OERenderUtil.renderModel(poseStack, ivb, tapeRollModel, i, i1);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

}
