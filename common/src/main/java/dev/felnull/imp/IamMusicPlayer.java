package dev.felnull.imp;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import dev.architectury.platform.Platform;
import dev.felnull.imp.advancements.IMPCriteriaTriggers;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.blockentity.IMPBlockEntities;
import dev.felnull.imp.entity.village.IMPPoiType;
import dev.felnull.imp.entity.village.IMPVillagerProfessions;
import dev.felnull.imp.handler.CommonHandler;
import dev.felnull.imp.inventory.IMPMenus;
import dev.felnull.imp.item.IMPItems;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.imp.server.handler.ServerHandler;
import dev.felnull.imp.server.handler.ServerMusicHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class IamMusicPlayer {
    public static final IMPConfig CONFIG = AutoConfig.register(IMPConfig.class, Toml4jConfigSerializer::new).getConfig();
    public static final String MODID = "iammusicplayer";
    private static final Supplier<String> MODNAME = Suppliers.memoize(() -> Platform.getMod(MODID).getName());
    public static final OptionInstance<Double> IMP_VOLUME_OPTION = new OptionInstance<>("soundCategory." + IamMusicPlayer.MODID, OptionInstance.noTooltip(), (component, val) -> {
        return val == 0.0 ? Options.genericValueLabel(component, CommonComponents.OPTION_OFF) : Component.translatable("options.percent_value", component, (int) (val * 100.0));
    }, OptionInstance.UnitDouble.INSTANCE, CONFIG.volume, (val) -> CONFIG.volume = val);

    public static void init() {
        IMPPackets.init();
        IMPItems.init();
        IMPBlocks.init();
        IMPBlockEntities.init();
        IMPMenus.init();
        IMPPoiType.init();
        IMPVillagerProfessions.init();
        IMPCriteriaTriggers.init();
        ServerMusicHandler.init();
        ServerHandler.init();
        CommonHandler.init();
    }

    public static void setup() {
        IMPVillagerProfessions.setup();
    }

    public static String getModName() {
        return MODNAME.get();
    }
}
