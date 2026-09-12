package ch.dok.overgeared_jei_compat.compat.jei;

import ch.dok.overgeared_jei_compat.compat.jei.category.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Blocks;
import net.stirdrem.overgeared.block.ModBlocks;
import net.stirdrem.overgeared.item.ModItems;
import net.stirdrem.overgeared.recipe.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class OGJeiPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID =
            ResourceLocation.fromNamespaceAndPath("overgeared_jei_compat", "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper h = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new AlloySmeltingCategory(h),
                new NetherAlloySmeltingCategory(h),
                new CastingCategory(h),
                new CoolingCategory(h),
                new ForgingCategory(h),
                new GrindingCategory(h),
                new KnappingCategory(h),
                new FletchingCategory(h)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Minecraft.getInstance().level.getRecipeManager();

        List<IAlloyRecipe> alloyRecipes = new ArrayList<>();
        rm.getAllRecipesFor(ModRecipeTypes.ALLOY_SMELTING.get()).stream()
                .map(RecipeHolder::value).forEach(alloyRecipes::add);
        rm.getAllRecipesFor(ModRecipeTypes.SHAPED_ALLOY_SMELTING.get()).stream()
                .map(RecipeHolder::value).forEach(alloyRecipes::add);
        registration.addRecipes(AlloySmeltingCategory.RECIPE_TYPE, alloyRecipes);

        List<INetherAlloyRecipe> netherRecipes = new ArrayList<>();
        rm.getAllRecipesFor(ModRecipeTypes.NETHER_ALLOY_SMELTING.get()).stream()
                .map(RecipeHolder::value).forEach(netherRecipes::add);
        rm.getAllRecipesFor(ModRecipeTypes.SHAPED_NETHER_ALLOY_SMELTING.get()).stream()
                .map(RecipeHolder::value).forEach(netherRecipes::add);
        registration.addRecipes(NetherAlloySmeltingCategory.RECIPE_TYPE, netherRecipes);

        registration.addRecipes(CastingCategory.RECIPE_TYPE,
                rm.getAllRecipesFor(ModRecipeTypes.CASTING.get()).stream()
                        .map(RecipeHolder::value).collect(Collectors.toList()));

        registration.addRecipes(CoolingCategory.RECIPE_TYPE,
                rm.getAllRecipesFor(ModRecipeTypes.COOLING_RECIPE.get()).stream()
                        .map(RecipeHolder::value).collect(Collectors.toList()));

        registration.addRecipes(ForgingCategory.RECIPE_TYPE,
                rm.getAllRecipesFor(ModRecipeTypes.FORGING.get()).stream()
                        .map(RecipeHolder::value).collect(Collectors.toList()));

        registration.addRecipes(GrindingCategory.RECIPE_TYPE,
                rm.getAllRecipesFor(ModRecipeTypes.GRINDING_RECIPE.get()).stream()
                        .map(RecipeHolder::value).collect(Collectors.toList()));

        registration.addRecipes(KnappingCategory.RECIPE_TYPE,
                rm.getAllRecipesFor(ModRecipeTypes.KNAPPING.get()).stream()
                        .map(RecipeHolder::value).collect(Collectors.toList()));

        registration.addRecipes(FletchingCategory.RECIPE_TYPE, buildFletchingRecipes(rm));
    }

    private List<FletchingJeiRecipe> buildFletchingRecipes(RecipeManager rm) {
        List<FletchingJeiRecipe> jeiRecipes = new ArrayList<>();

        for (RecipeHolder<FletchingRecipe> holder : rm.getAllRecipesFor(ModRecipeTypes.FLETCHING.get())) {
            FletchingRecipe recipe = holder.value();

            // Plain (no-potion) variant always first
            jeiRecipes.add(new FletchingJeiRecipe(
                    recipe.getTip(), recipe.getShaft(), recipe.getFeather(),
                    recipe.getDefaultResult().copy()
            ));

            // One entry per potion for the tipped variant (regular potion → tipped arrow)
            if (recipe.hasTippedResult()) {
                ItemStack tippedBase = recipe.getTippedResult();
                BuiltInRegistries.POTION.holders()
                        .filter(h -> !h.value().getEffects().isEmpty())
                        .forEach(potionHolder -> {
                            ItemStack potionStack = new ItemStack(Items.POTION);
                            potionStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potionHolder));
                            ItemStack outputStack = new ItemStack(tippedBase.getItem(), tippedBase.getCount());
                            outputStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potionHolder));
                            jeiRecipes.add(new FletchingJeiRecipe(
                                    recipe.getTip(), recipe.getShaft(), recipe.getFeather(),
                                    potionStack, outputStack
                            ));
                        });
            }

            // One entry per potion for the lingering variant (lingering potion → lingering arrow)
            if (recipe.hasLingeringResult()) {
                ItemStack lingeringBase = recipe.getLingeringResult();
                BuiltInRegistries.POTION.holders()
                        .filter(h -> !h.value().getEffects().isEmpty())
                        .forEach(potionHolder -> {
                            ItemStack potionStack = new ItemStack(Items.LINGERING_POTION);
                            potionStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potionHolder));
                            ItemStack outputStack = new ItemStack(lingeringBase.getItem(), lingeringBase.getCount());
                            outputStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potionHolder));
                            jeiRecipes.add(new FletchingJeiRecipe(
                                    recipe.getTip(), recipe.getShaft(), recipe.getFeather(),
                                    potionStack, outputStack
                            ));
                        });
            }
        }

        return jeiRecipes;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ALLOY_FURNACE.get()), AlloySmeltingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.NETHER_ALLOY_FURNACE.get()), NetherAlloySmeltingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CASTING_FURNACE.get()), CastingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SMITHING_ANVIL.get()), ForgingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.TIER_A_SMITHING_ANVIL.get()), ForgingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.TIER_B_SMITHING_ANVIL.get()), ForgingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.STONE_SMITHING_ANVIL.get()), ForgingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.ROCK.get()), KnappingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(Blocks.FLETCHING_TABLE), FletchingCategory.RECIPE_TYPE);
    }
}
