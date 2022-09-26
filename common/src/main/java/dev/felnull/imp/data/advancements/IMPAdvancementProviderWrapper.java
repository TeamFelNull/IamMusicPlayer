package dev.felnull.imp.data.advancements;

import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.AdvancementProviderWrapper;
import net.minecraft.advancements.Advancement;

import java.util.function.Consumer;

public class IMPAdvancementProviderWrapper extends AdvancementProviderWrapper {
    private final Consumer<Consumer<Advancement>> advancements = new IMPAdvancements();

    public IMPAdvancementProviderWrapper(CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(crossDataGeneratorAccess);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        advancements.accept(consumer);
    }
}
