package dev.felnull.imp.data;

import dev.felnull.imp.item.IMPItemTags;
import dev.felnull.imp.item.IMPItems;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.RecipeProviderWrapper;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class IMPRecipeProviderWrapper extends RecipeProviderWrapper {
    public IMPRecipeProviderWrapper(CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(crossDataGeneratorAccess);
    }

    @Override
    public void generateRecipe(Consumer<FinishedRecipe> exporter, RecipeProviderAccess providerAccess) {
        ShapelessRecipeBuilder.shapeless(IMPItems.MANUAL.get())
                .requires(Items.BOOK)
                .requires(IMPItemTags.CASSETTE_TAPE)
                .unlockedBy("has_book", providerAccess.has(Items.BOOK))
                .save(exporter);
    }
}
