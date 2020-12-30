package red.felnull.imp.client.renderer.handanimation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.otyacraftengine.client.renderer.handanimation.IHandAnimation;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class BoomboxHandAnimation implements IHandAnimation {

    @Override
    public boolean isRender(Hand hand, ItemStack itemStack, float equipProgress, float interpolatedPitch, float swingProgress) {
        return false;
    }

    @Override
    public void renderLeftHand(ItemStack itemStack, MatrixStack matrixStack, float partialTicks, IRenderTypeBuffer bufferIn, int light, float equipProgress, float interpolatedPitch, float swingProgress) {

    }

    @Override
    public void renderRightHand(ItemStack itemStack, MatrixStack matrixStack, float partialTicks, IRenderTypeBuffer bufferIn, int light, float equipProgress, float interpolatedPitch, float swingProgress) {
        IKSGRenderUtil.matrixPush(matrixStack);
        IKSGRenderUtil.matrixArmFirstPerson(matrixStack, equipProgress, swingProgress, HandSide.RIGHT);
        AbstractClientPlayerEntity abstractclientplayerentity = IamMusicPlayer.proxy.getMinecraft().player;
        PlayerRenderer playerrenderer = (PlayerRenderer) IamMusicPlayer.proxy.getMinecraft().getRenderManager().<AbstractClientPlayerEntity>getRenderer(abstractclientplayerentity);
        playerrenderer.renderRightArm(matrixStack, bufferIn, light, abstractclientplayerentity);
        FirstPersonRenderer fpr = Minecraft.getInstance().getFirstPersonRenderer();
        IKSGRenderUtil.matrixPush(matrixStack);
        IKSGRenderUtil.matrixTranslatef(matrixStack, 0, 2f, 0);
        fpr.renderItemSide(IamMusicPlayer.proxy.getMinecraft().player, itemStack, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, true, matrixStack, bufferIn, light);
        IKSGRenderUtil.matrixPop(matrixStack);
        IKSGRenderUtil.matrixPop(matrixStack);
    }

    @Override
    public void renderLeftHandOnry(ItemStack itemStack, MatrixStack matrixStack, float partialTicks, IRenderTypeBuffer bufferIn, int light, float equipProgress, float interpolatedPitch, float swingProgress) {

    }

    @Override
    public void renderRightHandOnry(ItemStack itemStack, MatrixStack matrixStack, float partialTicks, IRenderTypeBuffer bufferIn, int light, float equipProgress, float interpolatedPitch, float swingProgress) {

    }
}
/*        PlayerRenderer playerrenderer = (PlayerRenderer) IamMusicPlayer.proxy.getMinecraft().getRenderManager().<AbstractClientPlayerEntity>getRenderer(abstractclientplayerentity);
        if (flag) {
            playerrenderer.renderRightArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity);
            FirstPersonRenderer fpr = Minecraft.getInstance().getFirstPersonRenderer();
            IKSGRenderUtil.matrixPush(matrixStackIn);
            IKSGRenderUtil.matrixTranslatef(matrixStackIn, 0, 2f, 0);
            fpr.renderItemSide(IamMusicPlayer.proxy.getMinecraft().player, new ItemStack(Items.APPLE), ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, true, matrixStackIn, bufferIn, combinedLightIn);
            IKSGRenderUtil.matrixPop(matrixStackIn);
        } else {
            playerrenderer.renderLeftArm(matrixStackIn, bufferIn, combinedLightIn, abstractclientplayerentity);
        }*/
