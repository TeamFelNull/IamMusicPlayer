package red.felnull.imp.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.IMPEquipmentBaseBlock;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.otyacraftengine.client.renderer.blockentity.IkisugiBlockEntityRenderer;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class MusicSharingDeviceRenderer extends IkisugiBlockEntityRenderer<MusicSharingDeviceBlockEntity> {
    private static final ResourceLocation MSD_PWBTN_ON = new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/power_button_on");
    private static final ResourceLocation MSD_PWBTN_OFF = new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/power_button_off");
    private static final ResourceLocation MSD_PWMS_ON = new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/power_miniscreen_on");
    private static final ResourceLocation MSD_PWMS_OFF = new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/power_miniscreen_off");
    private static final ResourceLocation MSD_MONITOR = new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/monitor");
    private static final ResourceLocation MSD_MOUSEPAD = new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/mousepad");
    private static final ResourceLocation MSD_MOUSE = new ResourceLocation(IamMusicPlayer.MODID, "block/music_sharing_device/mouse");

    public MusicSharingDeviceRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MusicSharingDeviceBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        BlockState state = blockEntity.getBlockState();
        poseStack.pushPose();
        IKSGRenderUtil.matrixRotateDirection(poseStack, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise());
        renderMSD(poseStack, multiBufferSource, i, j, state.getValue(IMPEquipmentBaseBlock.ON));
        poseStack.popPose();
    }


    public static void renderMSD(PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay, boolean power) {
        VertexConsumer ivb = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        BakedModel modelPwBtn = IKSGRenderUtil.getBakedModel(power ? MSD_PWBTN_ON : MSD_PWBTN_OFF);
        BakedModel modelPwMS = IKSGRenderUtil.getBakedModel(power ? MSD_PWMS_ON : MSD_PWMS_OFF);
        BakedModel modelMonitor = IKSGRenderUtil.getBakedModel(MSD_MONITOR);
        BakedModel modelMousepad = IKSGRenderUtil.getBakedModel(MSD_MOUSEPAD);
        BakedModel modelMouse = IKSGRenderUtil.getBakedModel(MSD_MOUSE);

        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, modelPwBtn, combinedLight, combinedOverlay);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, modelPwMS, combinedLight, combinedOverlay);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, modelMonitor, combinedLight, combinedOverlay);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, modelMousepad, combinedLight, combinedOverlay);

        poseStack.pushPose();
        IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, 2f, 0.1f, 2f);
        IKSGRenderUtil.renderBakedModel(poseStack, ivb, null, modelMouse, combinedLight, combinedOverlay);
        poseStack.popPose();
    }
}
