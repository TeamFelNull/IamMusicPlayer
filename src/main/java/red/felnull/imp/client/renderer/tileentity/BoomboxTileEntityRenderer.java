package red.felnull.imp.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.CampfireTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.block.BoomboxBlock;
import red.felnull.imp.tileentity.BoomboxTileEntity;
import red.felnull.imp.util.RenderHelper;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class BoomboxTileEntityRenderer extends TileEntityRenderer<BoomboxTileEntity> {

    public static RenderMaterial material = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE,
            new ResourceLocation(IamMusicPlayer.MODID, "block/boombox"));

    private ModelRenderer lidWest;
    private ModelRenderer lidEast;
    private ModelRenderer lidSouth;
    private ModelRenderer lidNorth;

    public BoomboxTileEntityRenderer(TileEntityRendererDispatcher terd) {

        super(terd);
        this.lidWest = new ModelRenderer(64, 64, 10, 53);
        this.lidWest.addBox(6, 0, 0, 4, 3, 0.3f, 0.0F);
        this.lidWest.setRotationPoint(0, 2, 5);

        this.lidEast = new ModelRenderer(64, 64, 10, 53);
        this.lidEast.addBox(6, 0, 0, 4, 3, 0.3f, 0.0F);
        this.lidEast.setRotationPoint(0, 2, 10.7f);

        this.lidSouth = new ModelRenderer(64, 64, 0, 53);
        this.lidSouth.addBox(0, 0, 6, 0.3f, 3, 4, 0.0F);
        this.lidSouth.setRotationPoint(5, 2, 0);

        this.lidNorth = new ModelRenderer(64, 64, 0, 53);
        this.lidNorth.addBox(0, 0, 6, 0.3f, 3, 4, 0.0F);
        this.lidNorth.setRotationPoint(10.7f, 2, 0);

    }

    @Override
    public void render(BoomboxTileEntity tile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buff, int light, int overlay) {

        float opend = (float) tile.openProgress / 30f;

        IVertexBuilder IVB = material.getBuffer(buff, RenderType::getEntitySolid);

        Direction direction = tile.getBlockState().get(BoomboxBlock.FACING);

        RenderHelper.matrixPush(matrix);

        if (tile.getBlockState().get(BoomboxBlock.WALL)) {
            if (direction == Direction.NORTH) {
                RenderHelper.matrixTranslatef(matrix, -0.314f, 0.185f, 0);
                this.lidNorth.rotateAngleZ = -opend;
                this.lidNorth.render(matrix, IVB, light, overlay);
            } else if (direction == Direction.SOUTH) {
                RenderHelper.matrixTranslatef(matrix, 0.314f, 0.185f, 0);
                this.lidSouth.rotateAngleZ = opend;
                this.lidSouth.render(matrix, IVB, light, overlay);
            } else if (direction == Direction.EAST) {
                RenderHelper.matrixTranslatef(matrix, 0, 0.185f, -0.314f);
                this.lidEast.rotateAngleX = opend;
                this.lidEast.render(matrix, IVB, light, overlay);
            } else if (direction == Direction.WEST) {
                RenderHelper.matrixTranslatef(matrix, 0, 0.185f, 0.314f);
                this.lidWest.rotateAngleX = -opend;
                this.lidWest.render(matrix, IVB, light, overlay);
            }
        } else {
            if (direction == Direction.NORTH) {
                this.lidNorth.rotateAngleZ = -opend;
                this.lidNorth.render(matrix, IVB, light, overlay);
            } else if (direction == Direction.SOUTH) {
                this.lidSouth.rotateAngleZ = opend;
                this.lidSouth.render(matrix, IVB, light, overlay);
            } else if (direction == Direction.EAST) {
                this.lidEast.rotateAngleX = opend;
                this.lidEast.render(matrix, IVB, light, overlay);
            } else if (direction == Direction.WEST) {
                this.lidWest.rotateAngleX = -opend;
                this.lidWest.render(matrix, IVB, light, overlay);
            }
        }

        RenderHelper.matrixPop(matrix);

        ItemStack cassette = tile.getCassette();
        if (!cassette.isEmpty()) {
            RenderHelper.matrixPush(matrix);
            float scale = 0.65f;
            RenderHelper.matrixScalf(matrix, scale, scale, scale);
            if (tile.getBlockState().get(BoomboxBlock.WALL)) {
                if (direction == Direction.NORTH) {
                    RenderHelper.matrixTranslatef(matrix, -0.314f / scale, 0.185f / scale, 0);
                    RenderHelper.matrixTranslatef(matrix, 1.0f, 0.3f, 0.77f);
                    RenderHelper.matrixRotateDegreefY(matrix, 270);
                } else if (direction == Direction.SOUTH) {
                    RenderHelper.matrixTranslatef(matrix, 0.314f / scale, 0.185f / scale, 0);
                    RenderHelper.matrixTranslatef(matrix, 0.55f, 0.3f, 0.77f);
                    RenderHelper.matrixRotateDegreefY(matrix, 90);
                } else if (direction == Direction.EAST) {
                    RenderHelper.matrixTranslatef(matrix, 0, 0.185f / scale, -0.314f / scale);
                    RenderHelper.matrixTranslatef(matrix, 0.77f, 0.3f, 1.0f);
                    RenderHelper.matrixRotateDegreefY(matrix, 180);
                } else if (direction == Direction.WEST) {
                    RenderHelper.matrixTranslatef(matrix, 0, 0.185f / scale, 0.314f / scale);
                    RenderHelper.matrixTranslatef(matrix, 0.77f, 0.3f, 0.55f);
                }
            } else {
                if (direction == Direction.NORTH) {
                    RenderHelper.matrixTranslatef(matrix, 1.0f, 0.3f, 0.77f);
                    RenderHelper.matrixRotateDegreefY(matrix, 270);
                } else if (direction == Direction.SOUTH) {
                    RenderHelper.matrixTranslatef(matrix, 0.55f, 0.3f, 0.77f);
                    RenderHelper.matrixRotateDegreefY(matrix, 90);

                } else if (direction == Direction.EAST) {
                    RenderHelper.matrixTranslatef(matrix, 0.77f, 0.3f, 1.0f);
                    RenderHelper.matrixRotateDegreefY(matrix, 180);
                } else if (direction == Direction.WEST) {
                    RenderHelper.matrixTranslatef(matrix, 0.77f, 0.3f, 0.55f);
                }
            }
            Minecraft.getInstance().getItemRenderer().renderItem(cassette,
                    TransformType.FIXED, light, overlay, matrix, buff);

            RenderHelper.matrixPop(matrix);
        }
    }

}
