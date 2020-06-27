package red.felnull.imp.client.renderer.model;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IModelData;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.item.CassetteTapeItem;

@SuppressWarnings("deprecation")
public class CassetteBakedModel implements IBakedModel {

    private IBakedModel tmodel;
    private CassetteTapeItem cassette;


    public CassetteBakedModel(IBakedModel motomodel, CassetteTapeItem item) {
        this.tmodel = motomodel;
        this.cassette = item;
    }

    @Override
    public boolean isAmbientOcclusion() {

        return tmodel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {

        return tmodel.isGui3d();
    }

    @Override
    public boolean func_230044_c_() {

        return tmodel.func_230044_c_();
    }

    @Override
    public boolean isBuiltInRenderer() {

        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {

        return tmodel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {

        return tmodel.getOverrides();
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {

        return tmodel.getQuads(state, side, rand);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand,
                                    @Nonnull IModelData extraData) {

        return tmodel.getQuads(state, side, rand, extraData);
    }

    public ResourceLocation getLocation() {

        return new ResourceLocation(IamMusicPlayer.MODID, "item/" + cassette.getRegistryName().getPath());
    }

}
