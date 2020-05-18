package net.morimori.imp.client.renderer.model;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

public class TestModel implements IBakedModel{

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {

		return null;
	}

	@Override
	public boolean isAmbientOcclusion() {

		return false;
	}

	@Override
	public boolean isGui3d() {

		return false;
	}

	@Override
	public boolean func_230044_c_() {

		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {

		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {

		return null;
	}

	@Override
	public ItemOverrideList getOverrides() {

		return null;
	}

}
