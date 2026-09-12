package ch.dok.overgeared_jei_compat.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.stirdrem.overgeared.block.ModBlocks;
import net.stirdrem.overgeared.recipe.IAlloyRecipe;

import java.util.List;

public class AlloySmeltingCategory extends AbstractRecipeCategory<IAlloyRecipe> {

    public static final RecipeType<IAlloyRecipe> RECIPE_TYPE =
            RecipeType.create("overgeared_jei_compat", "alloy_smelting", IAlloyRecipe.class);

    // Layout: 3x3 grid (2,2)-(56,56), arrow (59,21), output (90,21), flame (2,58), XP text (20,60)
    private static final int WIDTH = 118;
    private static final int HEIGHT = 76;

    private final IDrawable arrow;
    private final IDrawableAnimated flame;

    public AlloySmeltingCategory(IGuiHelper helper) {
        super(RECIPE_TYPE,
                Component.translatable("jei.overgeared_jei_compat.alloy_smelting"),
                helper.createDrawableItemLike(ModBlocks.ALLOY_FURNACE.get()),
                WIDTH, HEIGHT);
        this.arrow = helper.getRecipeArrow();
        this.flame = helper.createAnimatedRecipeFlame(200);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IAlloyRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> inputs = recipe.getIngredientsList();
        for (int i = 0; i < 9; i++) {
            int col = i % 3;
            int row = i / 3;
            IRecipeSlotBuilder slot = builder.addInputSlot(2 + col * 18, 2 + row * 18)
                    .setStandardSlotBackground();
            if (i < inputs.size() && !inputs.get(i).isEmpty()) {
                slot.addIngredients(inputs.get(i));
            }
        }
        builder.addOutputSlot(90, 21)
                .setOutputSlotBackground()
                .addItemStack(recipe.getResultItem(
                        Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public void draw(IAlloyRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, 59, 21);
        flame.draw(graphics, 2, 58);
        String xpText = recipe.getExperience() + " XP";
        graphics.drawString(Minecraft.getInstance().font, xpText, 20, 60, 0xFFAAAAAA, false);
    }
}
