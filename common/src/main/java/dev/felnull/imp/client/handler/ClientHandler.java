package dev.felnull.imp.client.handler;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.architectury.networking.NetworkManager;
import dev.felnull.imp.IMPConfig;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.client.gui.screen.monitor.music_manager.MusicManagerMonitor;
import dev.felnull.imp.client.music.MusicEngine;
import dev.felnull.imp.client.music.MusicSyncManager;
import dev.felnull.imp.client.neteasecloudmusic.NetEaseCloudMusicManager;
import dev.felnull.imp.client.renderer.item.IMPItemRenderers;
import dev.felnull.imp.client.renderer.item.hand.BoomboxHandRenderer;
import dev.felnull.imp.entity.IRingerPartyParrot;
import dev.felnull.imp.integration.PatchouliIntegration;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.imp.networking.IMPPackets;
import dev.felnull.imp.server.music.ringer.MusicRingManager;
import dev.felnull.otyacraftengine.client.event.ClientEvent;
import dev.felnull.otyacraftengine.client.gui.TextureSpecify;
import dev.felnull.otyacraftengine.client.gui.components.IconButton;
import dev.felnull.otyacraftengine.item.location.HandItemLocation;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ClientHandler {
    private static final Minecraft mc = Minecraft.getInstance();
    private static double LAST_MUSIC_VOLUME = IamMusicPlayer.CONFIG.volume;

    public static void init() {
        ClientLifecycleEvent.CLIENT_LEVEL_LOAD.register(ClientHandler::onClientLevelLoad);
        ClientEvent.CHANGE_HAND_HEIGHT.register(ClientHandler::changeHandHeight);
        AutoConfig.getConfigHolder(IMPConfig.class).registerSaveListener(ClientHandler::onConfigSave);
        ClientEvent.POSE_HUMANOID_ARM.register(ClientHandler::onPoseHumanoidArm);
        ClientEvent.INTEGRATED_SERVER_PAUSE.register(ClientHandler::onPauseChange);
        //  MoreEntityEvent.ENTITY_TICK.register(ClientHandler::onEntityTick);
        ClientTickEvent.CLIENT_POST.register(ClientHandler::ontClientTick);
        ClientEvent.HAND_ATTACK.register(ClientHandler::onHandAttack);
        ClientGuiEvent.SET_SCREEN.register(ClientHandler::onModifyScreen);
        ClientGuiEvent.INIT_POST.register(ClientHandler::onScreenInit);
    }

    private static void onScreenInit(Screen screen, ScreenAccess screenAccess) {
        if (screen instanceof SoundOptionsScreen) {
            LAST_MUSIC_VOLUME = IamMusicPlayer.CONFIG.volume;

            screenAccess.addRenderableWidget(new IconButton(screen.width - 27, screen.height - 27, 20, 20, Component.translatable("imp.button.config"), TextureSpecify.createRelative(MusicManagerMonitor.WIDGETS_TEXTURE, 36, 58, 14, 5), n -> mc.setScreen(AutoConfig.getConfigScreen(IMPConfig.class, screen).get())));
        }
    }

    private static CompoundEventResult<Screen> onModifyScreen(Screen screen) {
        if (mc.screen instanceof SoundOptionsScreen && LAST_MUSIC_VOLUME != IamMusicPlayer.CONFIG.volume)
            AutoConfig.getConfigHolder(IMPConfig.class).save();
        return CompoundEventResult.pass();
    }

    private static EventResult onHandAttack(@NotNull ItemStack itemStack) {
        if (itemStack.getItem() instanceof BoomboxItem && BoomboxItem.isPowered(itemStack)) {
            if (mc.player.isCrouching()) {
                var bu = BoomboxItem.getRingerUUID(itemStack);
                if (bu != null)
                    NetworkManager.sendToServer(IMPPackets.HAND_LID_CYCLE, new IMPPackets.LidCycleMessage(bu, new HandItemLocation(InteractionHand.MAIN_HAND)).toFBB());
            }
            return EventResult.interruptFalse();
        }
        return EventResult.pass();
    }

    private static EventResult onEntityTick(Entity entity) {
        if (!entity.level.isClientSide()) return EventResult.pass();
        var mm = MusicEngine.getInstance();
        if (entity instanceof IRingerPartyParrot ringerPartyParrot) {
            var id = ringerPartyParrot.getRingerUUID();
            if (id == null || !mm.isPlaying(id))
                ringerPartyParrot.setRingerUUID(null);
        }
        return EventResult.pass();
    }

    private static void onPauseChange(boolean paused) {
        var rm = MusicRingManager.getInstance();
        var nmm = MusicEngine.getInstance();
        if (paused) {
            rm.pause();
            nmm.pause();
        } else {
            rm.resume();
            nmm.resume();
        }
    }

    private static InteractionResult onConfigSave(ConfigHolder<IMPConfig> configHolder, IMPConfig impConfig) {
        MusicEngine.getInstance().destroy();
        NetEaseCloudMusicManager.getInstance().reload();
        return InteractionResult.PASS;
    }


    private static void onClientLevelLoad(ClientLevel clientLevel) {
        MusicSyncManager.getInstance().reset();
    }

    private static EventResult changeHandHeight(InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        if (oldStack.getItem() instanceof BoomboxItem && newStack.getItem() instanceof BoomboxItem && BoomboxItem.matches(oldStack, newStack))
            return EventResult.interruptFalse();
        return EventResult.pass();
    }

    private static EventResult onPoseHumanoidArm(HumanoidArm arm, InteractionHand hand, HumanoidModel<? extends LivingEntity> model, LivingEntity livingEntity) {
        var item = livingEntity.getItemInHand(hand);
        if (item.is(IMPBlocks.BOOMBOX.get().asItem()) && BoomboxItem.getTransferProgress(item) >= 1f) {
            BoomboxHandRenderer.pose(arm, model, item);
            return EventResult.interruptFalse();
        }
        return EventResult.pass();
    }

    private static void ontClientTick(Minecraft instance) {
        if (PatchouliIntegration.INSTANCE.isEnable())
            IMPItemRenderers.manualItemRenderer.tick();
    }
}
