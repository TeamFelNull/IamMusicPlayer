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
import red.felnull.imp.client.renderer.blockentity.MusicSharingDeviceRenderer;
import red.felnull.otyacraftengine.client.renderer.item.ICustomBEWLRenderer;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class MusicSharingDeviceItemRenderer implements ICustomBEWLRenderer {
    private static final ResourceLocation MSD_MODEL = new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/base");

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        BakedModel model = IKSGRenderUtil.getBakedModel(MSD_MODEL);
        VertexConsumer ivb = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, model, i, i1);
        MusicSharingDeviceRenderer.renderMSD(poseStack, multiBufferSource, i, i1, false, 0f, 0f);
    }
}
