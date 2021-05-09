package red.felnull.imp.api;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.InteractionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.client.config.IMPClientConfig;
import red.felnull.imp.client.music.MusicEngine;
import red.felnull.otyacraftengine.api.OtyacraftEngineAPI;
import red.felnull.otyacraftengine.util.IKSGModUtil;

import java.util.List;
import java.util.function.Consumer;

public class IamMusicPlayerAPI {
    private static final Logger LOGGER = LogManager.getLogger(IamMusicPlayerAPI.class);
    private static IamMusicPlayerAPI INSTANCE;
    private final List<IIMPIntegration> integrations;

    private IamMusicPlayerAPI(List<IIMPIntegration> integrations) {
        this.integrations = integrations;
    }

    public static IamMusicPlayerAPI getInstance() {
        if (INSTANCE == null) {
            apiInit();
        }

        return INSTANCE;
    }

    private static void apiInit() {
        IamMusicPlayerAPI api = new IamMusicPlayerAPI(IKSGModUtil.getModEntrypoints(IIMPIntegration.class, IamMusicPlayer.MODID, IMPIntegration.class));
        if (INSTANCE == null) {
            LOGGER.info("API Initialize");
            long startTime = System.currentTimeMillis();
            INSTANCE = api;
            LOGGER.info("API Initialize elapsed time: " + (System.currentTimeMillis() - startTime) + "ms");
        }
    }

    public List<IIMPIntegration> getIntegrations() {
        return this.integrations;
    }

    public void integrationConsumer(Consumer<IIMPIntegration> consumer) {
        List<IIMPIntegration> integrations = this.getIntegrations();
        integrations.forEach(consumer);
    }
}
