package dev.felnull.imp.client.renderer.hand;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.otyacraftengine.client.util.OERenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class BoomboxHandRenderer {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void render(PoseStack poseStack, MultiBufferSource multiBufferSource, InteractionHand hand, int packedLight, float partialTicks, float interpolatedPitch, float swingProgress, float equipProgress, ItemStack stack) {
        swingProgress = 0;
        boolean off = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = off ? mc.player.getMainArm() : mc.player.getMainArm().getOpposite();
        float uns = arm == HumanoidArm.RIGHT ? 1f : -1f;
        float transPr = BoomboxItem.getTransferProgress(stack, partialTicks);


        poseStack.pushPose();
        OERenderUtil.posePlayerArm(poseStack, arm, swingProgress, equipProgress);

        //    poseStack.translate(0 * uns * transPr, 0.75f * transPr, 0.5f * transPr);
        //    OERenderUtil.poseRotateAll(poseStack, -45 * transPr, -20 * uns * transPr, 40 * uns * transPr);

        /*float x = 0, y = 0, z = 0;
        try {
            String name = stack.getHoverName().getString();
            String[] poss = name.split(",");
            x = Float.parseFloat(poss[0]);
            y = Float.parseFloat(poss[1]);
            z = Float.parseFloat(poss[2]);
        } catch (Exception ex) {
        }*/
        // System.out.println(x + "," + y + "," + z);
        // poseStack.translate(-0.85f, -0.1f, 0.32f);
        //OERenderUtil.poseRotateAll(poseStack, -50f, 0f, -35f);
        // poseStack.translate(-0.85, -0.3, 0.6);
        //  OERenderUtil.poseRotateAll(poseStack, -50f, 0f, -35f);
        // poseStack.translate(-0.85f, -0.1f, 0.32f);
        // OERenderUtil.poseRotateAll(poseStack, x, y, z);

        poseStack.translate(lerpTriple(transPr, -0.85f, -1.5f, 0f) * uns, lerpTriple(transPr, -0.1f, -0.3f, 0.75f), lerpTriple(transPr, 0.32f, 0.6f, 0.5f));
        OERenderUtil.poseRotateAll(poseStack, lerpTriple(transPr, -50f, -50f, -45), lerpTriple(transPr, 0f, 0f, -20) * uns, lerpTriple(transPr, -35f, -35f, 40) * uns);

        OERenderUtil.renderPlayerArm(poseStack, multiBufferSource, arm, packedLight);
        poseStack.popPose();

        poseStack.pushPose();
        OERenderUtil.poseHandItem(poseStack, arm, swingProgress, equipProgress);

        //   OERenderUtil.poseTrans16(poseStack, -0.75 * uns * transPr, 12.25 * transPr, 7.5 * transPr);
        //   OERenderUtil.poseRotateAll(poseStack, 0 * transPr, 75 * uns * transPr, 0 * transPr);
        //   OERenderUtil.poseTrans16(poseStack, 0.75 * uns, 3.5, 0.25);
        //  OERenderUtil.poseRotateAll(poseStack, 8.42f, 36.24f * uns, 2.72f * uns);

        OERenderUtil.poseTrans16(poseStack, Mth.lerp(transPr, 0.75f, -0.75) * uns, Mth.lerp(transPr, 0.75f, 12.25), Mth.lerp(transPr, 0.75f, 7.5));
        OERenderUtil.poseRotateAll(poseStack, Mth.lerp(transPr, 8.42f, 0), Mth.lerp(transPr, 36.24f, 75) * uns, Mth.lerp(transPr, 2.72f, 0) * uns);

        OERenderUtil.renderHandItem(poseStack, multiBufferSource, arm, stack, packedLight);
        poseStack.popPose();
    }

    private static float lerpTriple(float par, float v1, float v2, float v3) {
        return par > 0.5f ? Mth.lerp(par, v2, v3) : Mth.lerp(par, v1, v2);
    }
}
