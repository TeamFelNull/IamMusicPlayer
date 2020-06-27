package red.felnull.imp.registries;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

public class IMPRegistries {

	public static Map<ResourceLocation, String> DwonloadMusics = new HashMap<ResourceLocation, String>();

	public static void registerDwonloadMusic(ResourceLocation location, String url) {
		DwonloadMusics.put(location, url);
	}
}
