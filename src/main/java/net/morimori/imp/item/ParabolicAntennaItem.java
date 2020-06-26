package net.morimori.imp.item;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.Item;
import net.morimori.imp.client.renderer.item.ParabolicAntennaRenderer;
import net.morimori.imp.client.renderer.model.ParabolicAntennaBakedModel;

public class ParabolicAntennaItem extends Item {
	public ParabolicAntennaItem(Properties properties) {
		super(properties.setISTER(() -> ParabolicAntennaRenderer::new));
	}

	public ParabolicAntennaBakedModel getModel(IBakedModel moto, boolean kamed) {

		return new ParabolicAntennaBakedModel(moto, kamed);
	}
}
