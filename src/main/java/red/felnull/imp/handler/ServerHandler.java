package red.felnull.imp.handler;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import red.felnull.imp.IamMusicPlayer;
import red.felnull.imp.data.IMPWorldData;
import red.felnull.imp.data.PlayListGuildManeger;
import red.felnull.imp.item.IMPItems;
import red.felnull.imp.music.ServerWorldMusicManager;
import red.felnull.imp.util.PathUtils;
import red.felnull.otyacraftengine.api.ResponseSender;
import red.felnull.otyacraftengine.api.event.common.ReceiverEvent;
import red.felnull.otyacraftengine.api.event.common.ResponseEvent;
import red.felnull.otyacraftengine.data.SendReceiveLogger;
import red.felnull.otyacraftengine.util.IKSGDataUtil;
import red.felnull.otyacraftengine.util.IKSGFileLoadUtil;

import java.io.File;

public class ServerHandler {
    @SubscribeEvent
    public static void onClientResponse(ResponseEvent.Client e) {
        if (e.getLocation().equals(IMPWorldData.PLAYLIST_REQUEST)) {
            if (e.getId() == 0) {
                PlayListGuildManeger.instance().joinPlayList(e.getPlayer(), e.getMessage());
            }
        }
    }

    @SubscribeEvent
    public static void onReceiverPos(ReceiverEvent.Server.Pos e) {
        if (e.getLocation().equals(IMPWorldData.SERVER_MUSIC_DATA)) {
            sendMusicUploadResultState(e.getPlayer(), e.getName(), e.getReceiveResult(), "unziped");
            try {
                byte[] compdata = IKSGFileLoadUtil.fileBytesReader(PathUtils.getWorldTmpFolder().resolve(e.getName()));
                IKSGFileLoadUtil.fileBytesWriter(IKSGDataUtil.gzUnZipping(compdata), PathUtils.getWorldTmpFolder().resolve(e.getName() + "-tmp"));
                IKSGFileLoadUtil.deleteFile(PathUtils.getWorldTmpFolder().resolve(e.getName()));
                File file = PathUtils.getWorldTmpFolder().resolve(e.getName() + "-tmp").toFile();
                IKSGFileLoadUtil.createFolder(PathUtils.getWorldMusicFolder());
                file.renameTo(PathUtils.getWorldMusicFolder().resolve(e.getName()).toFile());
            } catch (Exception ex) {
            }
            sendMusicUploadResultState(e.getPlayer(), e.getName(), e.getReceiveResult(), "complete");
        }
    }

    private static void sendMusicUploadResultState(ServerPlayerEntity player, String uuid, SendReceiveLogger.SRResult result, String state) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("result", result.name());
        tag.putString("state", state);
        ResponseSender.sendToClient(player, IMPWorldData.SEND_MUSIC_RESPONSE, 0, uuid, tag);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent e) {
        ServerWorldMusicManager.instance().tick();
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent e) {
        ServerWorldMusicManager.instance().clear();
    }


    @SubscribeEvent
    public static void onServerStopping(FMLServerStoppingEvent e) {
        ServerWorldMusicManager.instance().clear();
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent e) {
        if (e.getName().toString().equals("minecraft:chests/simple_dungeon")) {
            LootPool pool = LootPool.builder().rolls(new RandomValueRange(1))
                    .acceptCondition(RandomChance.builder((0.0810f)))
                    .addEntry(ItemLootEntry.builder(IMPItems.IKISUGI_ANTENNA).weight(1))
                    .name(new ResourceLocation(IamMusicPlayer.MODID, "antenna").toString())
                    .build();
            e.getTable().addPool(pool);
        }
    }
/*
    @SubscribeEvent
    public static void onLogIn(PlayerEvent.PlayerLoggedInEvent e) {
        e.getPlayer().sendStatusMessage(IKSGStyles.withStyle(new TranslationTextComponent("message.login.impalpha"), IKSGStyles.withColor(TextFormatting.RED)), false);
    }*/
}
