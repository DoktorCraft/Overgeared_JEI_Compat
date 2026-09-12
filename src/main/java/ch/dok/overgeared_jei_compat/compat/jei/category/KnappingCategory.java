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
import net.stirdrem.overgeared.item.ModItems;
import net.stirdrem.overgeared.recipe.RockKnappingRecipe;

public class KnappingCategory extends AbstractRecipeCategory<RockKnappingRecipe> {

    public static final RecipeType<RockKnappingRecipe> RECIPE_TYPE =
            RecipeType.create("overgeared_jei_compat", "knapping", RockKnappingRecipe.class);

    // 3x3 grid of 12px cells with 1px gap → 38x38px at PATTERN_X/Y
    // Rock input (2,13), pattern (22,3), arrow (64,11), output (89,13)
    private static final int WIDTH  = 110;
    private static final int HEIGHT = 44;
    private static final int PATTERN_X = 22;
    private static final int PATTERN_Y = 3;
    private static final int CELL = 12;
    private static final int STEP = 13; // cell + 1px gap

    private static final int FILLED_BORDER  = 0xFF5A5A5A;
    private static final int FILLED_INNER   = 0xFFAAAAAA;
    private static final int FILLED_HILIGHT = 0xFFCCCCCC;
    private static final int EMPTY_BORDER   = 0xFF222222;
    private static final int EMPTY_INNER    = 0xFF333333;

    private final IDrawable arrow;

    public KnappingCategory(IGuiHelper helper) {
        super(RECIPE_TYPE,
                Component.translatable("jei.overgeared_jei_compat.knapping"),
                helper.createDrawableItemLike(ModItems.ROCK.get()),
                WIDTH, HEIGHT);
        this.arrow = helper.getRecipeArrow();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RockKnappingRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(2, 13).setStandardSlotBackground().addIngredients(recipe.getIngredient());
        builder.addOutputSlot(89, 13).setOutputSlotBackground().addItemStack(recipe.getResult());
    }

    @Override
    public void draw(RockKnappingRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        boolean[][] pat = recipe.getPattern().getPattern();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                int x = PATTERN_X + c * STEP;
                int y = PATTERN_Y + r * STEP;
                boolean filled = r < pat.length && c < pat[r].length && pat[r][c];
                if (filled) {
                    graphics.fill(x, y, x + CELL, y + CELL, FILLED_BORDER);
                    graphics.fill(x + 1, y + 1, x + CELL - 1, y + CELL - 1, FILLED_INNER);
                    graphics.fill(x + 1, y + 1, x + CELL - 1, y + 2, FILLED_HILIGHT);
                    graphics.fill(x + 1, y + 1, x + 2, y + CELL - 1, FILLED_HILIGHT);
                } else {
                    graphics.fill(x, y, x + CELL, y + CELL, EMPTY_BORDER);
                    graphics.fill(x + 1, y + 1, x + CELL - 1, y + CELL - 1, EMPTY_INNER);
                }
            }
        }
        arrow.draw(graphics, 64, 11);
    }
}
