package dev.felnull.imp.data;

import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.RecipeProviderWrapper;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class IMPRecipeProviderWrapper extends RecipeProviderWrapper {
    public IMPRecipeProviderWrapper(CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(crossDataGeneratorAccess);
    }

    @Override
    public void generateRecipe(Consumer<FinishedRecipe> exporter, RecipeProviderAccess providerAccess) {

    }
}
