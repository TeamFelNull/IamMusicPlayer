package dev.felnull.imp.advancements;

import com.google.gson.JsonObject;
import dev.felnull.imp.IamMusicPlayer;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ListenToMusicTrigger extends SimpleCriterionTrigger<ListenToMusicTrigger.TriggerInstance> {
    private static final ResourceLocation ID = new ResourceLocation(IamMusicPlayer.MODID, "listen_to_music");

    /*@Override
    protected TriggerInstance createInstance(JsonObject jo, EntityPredicate.Composite composite, DeserializationContext deserializationContext) {
        boolean radio = jo.has("radio") && jo.get("radio").getAsBoolean();
        boolean remote = jo.has("remote") && jo.get("remote").getAsBoolean();
        boolean kamesuta = jo.has("kamesuta") && jo.get("kamesuta").getAsBoolean();
        return new TriggerInstance(composite, radio, remote, kamesuta);
    }*/

    @Override
    protected TriggerInstance createInstance(JsonObject jo, ContextAwarePredicate contextAwarePredicate, DeserializationContext deserializationContext) {
        boolean radio = jo.has("radio") && jo.get("radio").getAsBoolean();
        boolean remote = jo.has("remote") && jo.get("remote").getAsBoolean();
        boolean kamesuta = jo.has("kamesuta") && jo.get("kamesuta").getAsBoolean();
        return new TriggerInstance(contextAwarePredicate, radio, remote, kamesuta);
    }

    public void trigger(ServerPlayer serverPlayer, boolean radio, boolean remote, boolean kamesuta) {
        this.trigger(serverPlayer, (triggerInstance) -> triggerInstance.matches(radio, remote, kamesuta));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final boolean radio;
        private final boolean remote;
        private final boolean kamesuta;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate, boolean radio, boolean remote, boolean kamesuta) {
            super(ID, contextAwarePredicate);
            this.radio = radio;
            this.remote = remote;
            this.kamesuta = kamesuta;
        }

        /*public TriggerInstance(EntityPredicate.Composite composite, boolean radio, boolean remote, boolean kamesuta) {
            super(ID, composite);
            this.radio = radio;
            this.remote = remote;
            this.kamesuta = kamesuta;
        }*/

        public boolean matches(boolean radio, boolean remote, boolean kamesuta) {
            if (this.radio && !radio)
                return false;
            if (this.remote && !remote)
                return false;
            return !this.kamesuta || kamesuta;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jo = super.serializeToJson(serializationContext);
            jo.addProperty("radio", this.radio);
            jo.addProperty("remote", this.remote);
            jo.addProperty("kamesuta", this.kamesuta);
            return jo;
        }

        public static TriggerInstance listen(boolean radio, boolean remote, boolean kamesuta) {
            return new TriggerInstance(ContextAwarePredicate.ANY, radio, remote, kamesuta);
        }
    }
}
