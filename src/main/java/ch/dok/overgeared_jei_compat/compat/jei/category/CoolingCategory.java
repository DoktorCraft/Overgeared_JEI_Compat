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
import net.stirdrem.overgeared.recipe.CoolingRecipe;

public class CoolingCategory extends AbstractRecipeCategory<CoolingRecipe> {

    public static final RecipeType<CoolingRecipe> RECIPE_TYPE =
            RecipeType.create("overgeared_jei_compat", "cooling", CoolingRecipe.class);

    private static final int WIDTH = 82;
    private static final int HEIGHT = 26;

    private final IDrawable arrow;

    public CoolingCategory(IGuiHelper helper) {
        super(RECIPE_TYPE,
                Component.translatable("jei.overgeared_jei_compat.cooling"),
                helper.createDrawableItemLike(Items.WATER_BUCKET),
                WIDTH, HEIGHT);
        this.arrow = helper.getRecipeArrow();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CoolingRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(1, 4).addIngredients(recipe.getIngredient());
        builder.addOutputSlot(63, 4).addItemStack(recipe.getResultItem(
                net.minecraft.client.Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public void draw(CoolingRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, 26, 5);
    }
}
