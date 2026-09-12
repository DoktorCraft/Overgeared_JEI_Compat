package ch.dok.overgeared_jei_compat.compat.jei.category;

import ch.dok.overgeared_jei_compat.compat.jei.FletchingJeiRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;

public class FletchingCategory extends AbstractRecipeCategory<FletchingJeiRecipe> {

    public static final RecipeType<FletchingJeiRecipe> RECIPE_TYPE =
            RecipeType.create("overgeared_jei_compat", "fletching", FletchingJeiRecipe.class);

    // Diagonal 2-column layout matching EMI:
    //   col0  col1
    //   [   ] [TIP]   y=1
    //   [SHF] [   ]   y=19
    //   [FTH] [POT]   y=37
    //   → arrow (46,19) → output (78,19)
    private static final int WIDTH = 100;
    private static final int HEIGHT = 58;

    private final IDrawable arrow;

    public FletchingCategory(IGuiHelper helper) {
        super(RECIPE_TYPE,
                Component.translatable("jei.overgeared_jei_compat.fletching"),
                helper.createDrawableItemLike(Blocks.FLETCHING_TABLE),
                WIDTH, HEIGHT);
        this.arrow = helper.getRecipeArrow();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FletchingJeiRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(19, 1).setStandardSlotBackground().addIngredients(recipe.tip);
        builder.addInputSlot(1, 19).setStandardSlotBackground().addIngredients(recipe.shaft);
        builder.addInputSlot(1, 37).setStandardSlotBackground().addIngredients(recipe.feather);

        IRecipeSlotBuilder potionSlot = builder.addInputSlot(19, 37).setStandardSlotBackground();
        if (recipe.potionInput != null) {
            potionSlot.addItemStack(recipe.potionInput);
        }

        builder.addOutputSlot(78, 19).setOutputSlotBackground().addItemStack(recipe.output);
    }

    @Override
    public void draw(FletchingJeiRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, 46, 19);
    }
}
