package dev.felnull.imp.client.handler;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.felnull.imp.IMPConfig;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.client.gui.components.MusicVolumeSlider;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.otyacraftengine.api.event.client.ClientEvent;
import dev.felnull.otyacraftengine.api.event.client.FabricOBJLoaderEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

public class ClientHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void init() {
        FabricOBJLoaderEvent.LOAD.register(ClientHandler::objLoad);
        ClientLifecycleEvent.CLIENT_LEVEL_LOAD.register(ClientHandler::onClientLevelLoad);
        ClientEvent.CHANGE_HAND_HEIGHT.register(ClientHandler::changeHandHeight);
        ClientGuiEvent.INIT_POST.register(ClientHandler::onScreenInit);
        AutoConfig.getConfigHolder(IMPConfig.class).registerSaveListener(ClientHandler::onConfigSave);
    }

    private static InteractionResult onConfigSave(ConfigHolder<IMPConfig> configHolder, IMPConfig impConfig) {
        MusicEngine.getInstance().reload();
        return InteractionResult.SUCCESS;
    }

    private static EventResult objLoad(ResourceLocation resourceLocation) {
        if (IamMusicPlayer.MODID.equals(resourceLocation.getNamespace()))
            return EventResult.interruptTrue();
        return EventResult.pass();
    }

    private static void onClientLevelLoad(ClientLevel clientLevel) {
        MusicSyncManager.getInstance().reset();
    }

    private static EventResult changeHandHeight(InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        if (oldStack.getItem() instanceof BoomboxItem && newStack.getItem() instanceof BoomboxItem && BoomboxItem.matches(oldStack, newStack))
            return EventResult.interruptFalse();
        return EventResult.pass();
    }

    private static void onScreenInit(Screen screen, ScreenAccess screenAccess) {
        if (screen instanceof SoundOptionsScreen) {
            int i = 11;
            int x = screen.width / 2 - 155 + i % 2 * 160;
            int y = screen.height / 6 - 12 + 24 * (i >> 1);
            screenAccess.addRenderableWidget(new MusicVolumeSlider(x, y, 150));
            screenAccess.addRenderableWidget(new ImageButton(x + 150 + 4, y, 20, 20, 48, 105, 20, MusicManagerMonitor.WIDGETS_TEXTURE, 256, 256, n -> mc.setScreen(AutoConfig.getConfigScreen(IMPConfig.class, screen).get()), new TranslatableComponent("imp.button.config")));
        }
    }
}
