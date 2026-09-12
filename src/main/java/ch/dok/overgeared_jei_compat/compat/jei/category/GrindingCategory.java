package ch.dok.overgeared_jei_compat.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.stirdrem.overgeared.recipe.GrindingRecipe;

public class GrindingCategory extends AbstractRecipeCategory<GrindingRecipe> {

    public static final RecipeType<GrindingRecipe> RECIPE_TYPE =
            RecipeType.create("overgeared_jei_compat", "grinding", GrindingRecipe.class);

    private static final int WIDTH = 82;
    private static final int HEIGHT = 26;

    private final IDrawable arrow;

    public GrindingCategory(IGuiHelper helper) {
        super(RECIPE_TYPE,
                Component.translatable("jei.overgeared_jei_compat.grinding"),
                helper.createDrawableItemLike(Items.GRINDSTONE),
                WIDTH, HEIGHT);
        this.arrow = helper.getRecipeArrow();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GrindingRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(1, 4).addIngredients(recipe.getInput());
        builder.addOutputSlot(63, 4).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(GrindingRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, 26, 5);
    }
}
