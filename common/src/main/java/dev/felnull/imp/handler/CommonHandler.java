package dev.felnull.imp.handler;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.TickEvent;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.otyacraftengine.event.MoreEntityEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommonHandler {
    public static List<UUID> itemBoomboxes = new ArrayList<>();

    public static void init() {
        MoreEntityEvent.ENTITY_TICK.register(CommonHandler::onEntityTick);
        TickEvent.SERVER_POST.register(CommonHandler::onTickEnd);
    }

    private static void onTickEnd(MinecraftServer minecraftServer) {
        itemBoomboxes.clear();
    }

    private static EventResult onEntityTick(Entity entity) {
        if (IamMusicPlayer.CONFIG.dropItemRing && entity instanceof ItemEntity itemEntity && itemEntity.getItem().getItem() instanceof BoomboxItem) {
            BoomboxItem.tick(entity.level, entity, itemEntity.getItem(), true);
        }
        return EventResult.pass();
    }
}
