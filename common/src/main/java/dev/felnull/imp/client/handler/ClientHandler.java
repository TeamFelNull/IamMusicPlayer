package dev.felnull.imp.client.handler;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.felnull.imp.IMPConfig;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.client.gui.components.MusicVolumeSlider;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.client.renderer.item.hand.BoomboxHandRenderer;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.imp.server.music.ringer.MusicRingManager;
import dev.felnull.otyacraftengine.OtyacraftEngine;
import dev.felnull.otyacraftengine.client.event.ClientEvent;
import dev.felnull.otyacraftengine.client.event.FabricOBJLoaderEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ClientHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static void init() {
        FabricOBJLoaderEvent.LOAD.register(ClientHandler::objLoad);
        ClientLifecycleEvent.CLIENT_LEVEL_LOAD.register(ClientHandler::onClientLevelLoad);
        ClientEvent.CHANGE_HAND_HEIGHT.register(ClientHandler::changeHandHeight);
        ClientGuiEvent.INIT_POST.register(ClientHandler::onScreenInit);
        AutoConfig.getConfigHolder(IMPConfig.class).registerSaveListener(ClientHandler::onConfigSave);
        ClientEvent.POSE_HUMANOID_ARM.register(ClientHandler::onPoseHumanoidArm);
        ClientEvent.INTEGRATED_SERVER_PAUSE.register(ClientHandler::onPauseChange);
    }

    private static void onPauseChange(boolean paused) {
        var rm = MusicRingManager.getInstance();
        var mm = MusicEngine.getInstance();
        if (paused) {
            rm.pause();
            mm.pause();
        } else {
            rm.resume();
            mm.resume();
        }
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
            int y = screen.height / 6 - 12 + 22 * (i >> 1);
            screenAccess.addRenderableWidget(new MusicVolumeSlider(x, y, 150));
            screenAccess.addRenderableWidget(new ImageButton(x + 150 + 4, y, 20, 20, 48, 105, 20, MusicManagerMonitor.WIDGETS_TEXTURE, 256, 256, n -> mc.setScreen(AutoConfig.getConfigScreen(IMPConfig.class, screen).get()), new TranslatableComponent("imp.button.config")));
        }
    }

    private static EventResult onPoseHumanoidArm(HumanoidArm arm, InteractionHand hand, HumanoidModel<? extends LivingEntity> model, LivingEntity livingEntity) {
        var item = livingEntity.getItemInHand(hand);
        if (item.is(IMPBlocks.BOOMBOX.asItem()) && BoomboxItem.getTransferProgress(item) >= 1f) {
            BoomboxHandRenderer.pose(arm, model, item);
            return EventResult.interruptFalse();
        }
        return EventResult.pass();
    }
}
