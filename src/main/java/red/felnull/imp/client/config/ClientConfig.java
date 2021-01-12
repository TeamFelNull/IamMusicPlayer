package red.felnull.imp.client.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientConfig {
    private static final Logger LOGGER = LogManager.getLogger(ClientConfig.class);

    public static ConfigValue<Double> MusicVolume;
    public static ConfigValue<Boolean> StereoEnabled;

    public static void init() {
        Pair<ConfigLoder, ForgeConfigSpec> client_config = new ForgeConfigSpec.Builder().configure(ConfigLoder::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, client_config.getRight());
    }

    public static class ConfigLoder {
        public ConfigLoder(ForgeConfigSpec.Builder builder) {
            LOGGER.info("Loading Client Config");
            builder.push("Music Play");
            MusicVolume = builder.define("Music Volume", 1d);
            StereoEnabled = builder.define("Stereo Enabled", true);
            builder.pop();
        }
    }
}
