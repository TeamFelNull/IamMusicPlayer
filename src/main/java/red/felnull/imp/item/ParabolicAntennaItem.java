package red.felnull.imp.item;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.Item;
import red.felnull.imp.client.renderer.item.ParabolicAntennaRenderer;
import red.felnull.imp.client.renderer.model.ParabolicAntennaBakedModel;

public class ParabolicAntennaItem extends Item {
	public ParabolicAntennaItem(Properties properties) {
		super(properties.setISTER(() -> ParabolicAntennaRenderer::new));
	}

	public ParabolicAntennaBakedModel getModel(IBakedModel moto, boolean kamed) {

		return new ParabolicAntennaBakedModel(moto, kamed);
	}
}
