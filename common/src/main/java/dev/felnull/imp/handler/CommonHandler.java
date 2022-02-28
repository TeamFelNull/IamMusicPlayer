package dev.felnull.imp.handler;

import dev.architectury.event.EventResult;
import dev.felnull.imp.IamMusicPlayer;
import dev.felnull.imp.item.BoomboxItem;
import dev.felnull.otyacraftengine.event.MoreEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

public class CommonHandler {
    public static void init() {
        MoreEntityEvent.ENTITY_TICK.register(CommonHandler::entityTick);
    }

    private static EventResult entityTick(Entity entity) {
        if (IamMusicPlayer.CONFIG.dropItemRing && entity instanceof ItemEntity)
            BoomboxItem.tick(entity.level, entity, ((ItemEntity) entity).getItem());
        return EventResult.pass();
    }
}
