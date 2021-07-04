package red.felnull.imp.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.IMPEquipmentBaseBlock;
import red.felnull.imp.blockentity.MusicSharingDeviceBlockEntity;
import red.felnull.otyacraftengine.client.renderer.blockentity.IkisugiBlockEntityRenderer;
import red.felnull.otyacraftengine.client.util.IKSGRenderUtil;

public class MusicSharingDeviceRenderer extends IkisugiBlockEntityRenderer<MusicSharingDeviceBlockEntity> {
    private static final Minecraft mc = Minecraft.getInstance();
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
    public void render(MusicSharingDeviceBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        BlockState state = blockEntity.getBlockState();
        poseStack.pushPose();
        IKSGRenderUtil.matrixRotateDirection(poseStack, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise());
        renderMSD(poseStack, multiBufferSource, i, j, partialTicks, state.getValue(IMPEquipmentBaseBlock.ON), blockEntity.getAntenna(), blockEntity.getAntennaProgress());
        poseStack.popPose();
    }


    public static void renderMSD(PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay, float partialTicks, boolean power, ItemStack antenna, long antennaProgress) {
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


        if (!antenna.isEmpty()) {
            if (!power)
                partialTicks = 0;
            float px = 10.5f;
            float py = 4.6f;
            float pz = 13.5f;

            float yaw = (float) antennaProgress * 5f;
            float preYaw = (float) (antennaProgress + 1) * 5f;
            float allYaw = IKSGRenderUtil.partialTicksMisalignment(yaw, preYaw, partialTicks);

            float p1 = (float) (antennaProgress % 40) * 5 - 50;
            float p2 = (float) ((antennaProgress + 1) % 40) * 5 - 50;

            float pitch = p1 > 50 ? 50 - (p1 - 50) : p1;
            float prePitch = p2 > 50 ? 50 - (p2 - 50) : p2;
            float allPitch = IKSGRenderUtil.partialTicksMisalignment(pitch, prePitch, partialTicks);

            poseStack.pushPose();
            IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, px, py, pz);
            IKSGRenderUtil.matrixRotateDegreefY(poseStack, allYaw);
            IKSGRenderUtil.matrixRotateDegreefZ(poseStack, allPitch);
            IKSGRenderUtil.matrixTranslatef16Divisions(poseStack, 0f, -1.8f, 0);
            mc.getItemRenderer().renderStatic(antenna, ItemTransforms.TransformType.GROUND, combinedLight, combinedOverlay, poseStack, multiBufferSource, 0);
            poseStack.popPose();
        }
    }
}
