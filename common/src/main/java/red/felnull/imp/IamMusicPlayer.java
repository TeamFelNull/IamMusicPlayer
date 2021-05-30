package red.felnull.imp;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import red.felnull.imp.api.IMPRegistry;
import red.felnull.imp.block.IMPBlocks;
import red.felnull.imp.blockentity.IMPBlockEntitys;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.inventory.IMPMenus;
import red.felnull.imp.item.IMPItems;
import red.felnull.imp.packet.IMPPackets;
import red.felnull.imp.recipe.IMPRecipes;

public class IamMusicPlayer {
    public static final IMPConfig CONFIG = AutoConfig.register(IMPConfig.class, Toml4jConfigSerializer::new).getConfig();

    public static final String MODID = "iammusicplayer";

    public static void init() {
        IMPPackets.init();
        IMPItems.init();
        IMPBlocks.init();
        IMPRecipes.init();
        IMPBlockEntitys.init();
        IMPRegistry.init();
        IMPWorldData.init();
        IMPMenus.init();
    }
}


