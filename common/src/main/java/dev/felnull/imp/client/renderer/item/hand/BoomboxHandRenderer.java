package dev.felnull.imp.client.renderer.item.hand;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.otyacraftengine.client.util.OERenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BoomboxHandRenderer {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void render(PoseStack poseStack, MultiBufferSource multiBufferSource, InteractionHand hand, int packedLight, float partialTicks, float interpolatedPitch, float swingProgress, float equipProgress, ItemStack stack) {
        //  if (BoomboxItem.getTransferProgress(stack) > 0)
        swingProgress = 0;
        boolean off = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = off ? mc.player.getMainArm() : mc.player.getMainArm().getOpposite();
        float uns = arm == HumanoidArm.RIGHT ? 1f : -1f;
        float transPr = BoomboxItem.getTransferProgress(stack, partialTicks);

        poseStack.pushPose();
        OERenderUtils.posePlayerArm(poseStack, arm, swingProgress, equipProgress);
        poseStack.translate(lerpTriple(transPr, -0.85f, -1.5f, 0f) * uns, lerpTriple(transPr, -0.1f, -0.3f, 0.75f), lerpTriple(transPr, 0.32f, 0.6f, 0.5f));
        OERenderUtils.poseRotateAll(poseStack, lerpTriple(transPr, -50f, -50f, -45), lerpTriple(transPr, 0f, 0f, -20) * uns, lerpTriple(transPr, -35f, -35f, 40) * uns);

        OERenderUtils.renderPlayerArm(poseStack, multiBufferSource, arm, packedLight);
        poseStack.popPose();

        poseStack.pushPose();
        OERenderUtils.poseHandItem(poseStack, arm, swingProgress, equipProgress);

        OERenderUtils.poseTrans16(poseStack, Mth.lerp(transPr, 0.75f, -0.75) * uns, Mth.lerp(transPr, 0.75f, 12.25), Mth.lerp(transPr, 0.75f, 7.5));
        OERenderUtils.poseRotateAll(poseStack, Mth.lerp(transPr, 8.42f, 0), Mth.lerp(transPr, 36.24f, 75) * uns, Mth.lerp(transPr, 2.72f, 0) * uns);

        OERenderUtils.renderHandItem(poseStack, multiBufferSource, arm, stack, packedLight);
        poseStack.popPose();
    }


    private static float lerpTriple(float par, float v1, float v2, float v3) {
        return par > 0.5f ? Mth.lerp(par, v2, v3) : Mth.lerp(par, v1, v2);
    }

    public static void pose(HumanoidArm arm, HumanoidModel<? extends LivingEntity> model, ItemStack stack) {
        var marm = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        float rv = arm == HumanoidArm.RIGHT ? 1f : -1f;
        marm.xRot = -(float) Math.PI / 2f - 0.1f;
        marm.yRot = 0.5f * rv;
    }

    public static void renderArmWithItem(ItemInHandLayer<? extends LivingEntity, ? extends EntityModel<?>> layer, LivingEntity livingEntity, ItemStack itemStack, ItemTransforms.TransformType transformType, HumanoidArm arm, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        if (!itemStack.isEmpty()) {
            boolean bl = arm == HumanoidArm.LEFT;
            float rv = arm == HumanoidArm.RIGHT ? 1f : -1f;
            poseStack.pushPose();
            layer.getParentModel().translateToHand(arm, poseStack);
            OERenderUtils.poseRotateZ(poseStack, -15f * rv);
            OERenderUtils.poseRotateY(poseStack, 180f);
            poseStack.translate(0, 0.2f, 1.2f);
            poseStack.translate(rv / 16.0f, 0.125f, -0.625f);
            Minecraft.getInstance().gameRenderer.itemInHandRenderer.renderItem(livingEntity, itemStack, transformType, bl, poseStack, multiBufferSource, i);
            poseStack.popPose();
        }
    }
}
