package dev.felnull.imp.advancements;

import com.google.gson.JsonObject;
import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class WriteCassetteTapeTrigger extends SimpleCriterionTrigger<WriteCassetteTapeTrigger.TriggerInstance> {
    private static final ResourceLocation ID = new ResourceLocation(IamMusicPlayer.MODID, "write_cassette_tape");

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite composite, DeserializationContext deserializationContext) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
        return new TriggerInstance(composite, itemPredicate);
    }

    public void trigger(ServerPlayer serverPlayer, ItemStack itemStack) {
        this.trigger(serverPlayer, (triggerInstance) -> triggerInstance.matches(itemStack));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;

        public TriggerInstance(EntityPredicate.Composite composite, ItemPredicate itemPredicat) {
            super(ID, composite);
            this.item = itemPredicat;
        }

        public boolean matches(ItemStack itemStack) {
            return item.matches(itemStack);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonObject = super.serializeToJson(serializationContext);
            jsonObject.add("item", this.item.serializeToJson());
            return jsonObject;
        }
    }
}
