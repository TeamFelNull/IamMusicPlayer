package dev.felnull.imp.data;

import dev.felnull.imp.block.IMPBlocks;
import dev.felnull.imp.item.IMPItemTags;
import dev.felnull.imp.item.IMPItems;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.RecipeProviderWrapper;
import dev.felnull.otyacraftengine.tag.PlatformItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class IMPRecipeProviderWrapper extends RecipeProviderWrapper {

    public IMPRecipeProviderWrapper(PackOutput packOutput, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, crossDataGeneratorAccess);
    }

    @Override
    public void generateRecipe(Consumer<FinishedRecipe> exporter, RecipeProviderAccess providerAccess) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IMPItems.MANUAL.get())
                .requires(Items.BOOK)
                .requires(IMPItemTags.CASSETTE_TAPE)
                .unlockedBy(providerAccess.getHasName(Items.BOOK), providerAccess.has(Items.BOOK))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IMPItems.CASSETTE_TAPE.get())
                .requires(PlatformItemTags.ironNuggets().getKey())
                .requires(PlatformItemTags.stone().getKey())
                .requires(PlatformItemTags.redstoneDusts())
                .requires(Items.DRIED_KELP)
                .unlockedBy(providerAccess.getHasName(Items.DRIED_KELP), providerAccess.has(Items.DRIED_KELP))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IMPItems.CASSETTE_TAPE_GLASS.get())
                .requires(PlatformItemTags.ironNuggets().getKey())
                .requires(PlatformItemTags.glassBlocks())
                .requires(PlatformItemTags.redstoneDusts())
                .requires(Items.DRIED_KELP)
                .unlockedBy(providerAccess.getHasName(Items.DRIED_KELP), providerAccess.has(Items.DRIED_KELP))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMPItems.RADIO_ANTENNA.get())
                .define('E', PlatformItemTags.enderPearls().getKey())
                .define('I', PlatformItemTags.ironIngots())
                .pattern("E")
                .pattern("I")
                .pattern("I")
                .group("antenna")
                .unlockedBy(providerAccess.getHasName(Items.ENDER_PEARL), providerAccess.has(PlatformItemTags.enderPearls().getKey()))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMPBlocks.BOOMBOX.get())
                .define('T', PlatformItemTags.ironNuggets().getKey())
                .define('I', PlatformItemTags.ironIngots())
                .define('N', Items.NOTE_BLOCK)
                .define('J', Items.JUKEBOX)
                .define('B', ItemTags.BUTTONS)
                .pattern("TBT")
                .pattern("NJN")
                .pattern("III")
                .unlockedBy(providerAccess.getHasName(Items.JUKEBOX), providerAccess.has(Items.JUKEBOX))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMPBlocks.CASSETTE_DECK.get())
                .define('R', PlatformItemTags.redstoneDusts())
                .define('I', PlatformItemTags.ironIngots())
                .define('N', Items.NOTE_BLOCK)
                .define('J', Items.JUKEBOX)
                .pattern("IRI")
                .pattern("NJN")
                .pattern("III")
                .unlockedBy(providerAccess.getHasName(Items.JUKEBOX), providerAccess.has(Items.JUKEBOX))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMPBlocks.MUSIC_MANAGER.get())
                .define('D', PlatformItemTags.diamonds())
                .define('I', PlatformItemTags.ironIngots())
                .define('G', PlatformItemTags.glassPanes())
                .define('R', PlatformItemTags.redstoneBlocks().getKey())
                .define('B', PlatformItemTags.diamonds())
                .pattern("III")
                .pattern("DGR")
                .pattern("BII")
                .unlockedBy(providerAccess.getHasName(Items.DIAMOND), providerAccess.has(Items.DIAMOND))
                .save(exporter);
    }
}
