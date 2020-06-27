package red.felnull.imp.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import red.felnull.imp.IamMusicPlayer;

public class CommonConfig {
	public static ConfigValue<Integer> SEND_BYTE;
	public static ConfigValue<Integer> MAX_LIMIT;

	public static void init() {
		Pair<ConfigLoder, ForgeConfigSpec> common_config = new ForgeConfigSpec.Builder().configure(ConfigLoder::new);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, common_config.getRight());
	}

	static class ConfigLoder {
		public ConfigLoder(ForgeConfigSpec.Builder builder) {
			IamMusicPlayer.LOGGER.info("Loading Common Config");
			builder.push("Sound File");
			SEND_BYTE = builder.define("sendbyte(byte)", 1024 * 8);
			MAX_LIMIT = builder.define("sizelimit(mb)", 50);
			builder.pop();
		}
	}
}
