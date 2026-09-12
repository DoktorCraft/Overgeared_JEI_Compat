package ch.dok.overgeared_jei_compat.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stirdrem.overgeared.block.ModBlocks;
import net.stirdrem.overgeared.components.BlueprintData;
import net.stirdrem.overgeared.components.ModComponents;
import net.stirdrem.overgeared.item.ModItems;
import net.stirdrem.overgeared.recipe.ForgingRecipe;

import java.util.Set;

public class ForgingCategory extends AbstractRecipeCategory<ForgingRecipe> {

    public static final RecipeType<ForgingRecipe> RECIPE_TYPE =
            RecipeType.create("overgeared_jei_compat", "forging", ForgingRecipe.class);

    // Blueprint slot (2,20), 3x3 grid (22,2), arrow (79,22), output (107,22)
    // Footer row below grid: Hits (22,59) and Tier (90,59)
    private static final int WIDTH = 178;
    private static final int HEIGHT = 70;

    private final IDrawable arrow;

    public ForgingCategory(IGuiHelper helper) {
        super(RECIPE_TYPE,
                Component.translatable("jei.overgeared_jei_compat.forging"),
                helper.createDrawableItemLike(ModBlocks.SMITHING_ANVIL.get()),
                WIDTH, HEIGHT);
        this.arrow = helper.getRecipeArrow();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ForgingRecipe recipe, IFocusGroup focuses) {
        // Blueprint slot — always visible; fill with typed blueprint stacks when available
        IRecipeSlotBuilder bpSlot = builder.addInputSlot(2, 20).setStandardSlotBackground();
        Set<String> bpTypes = recipe.getBlueprintTypes();
        if (!bpTypes.isEmpty()) {
            for (String type : bpTypes) {
                ItemStack bp = new ItemStack(ModItems.BLUEPRINT.get());
                bp.set(ModComponents.BLUEPRINT_DATA.get(), BlueprintData.createDefault().withToolType(type));
                bpSlot.addItemStack(bp);
            }
        } else if (recipe.requiresBlueprint()) {
            bpSlot.addItemStack(new ItemStack(ModItems.BLUEPRINT.get()));
        }

        // 3x3 ingredient grid — all 9 slots always rendered with backgrounds
        int rw = recipe.width;
        int rh = recipe.height;
        NonNullList<ForgingRecipe.ForgingIngredient> fi = recipe.getForgingIngredients();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                IRecipeSlotBuilder slot = builder.addInputSlot(22 + col * 18, 2 + row * 18)
                        .setStandardSlotBackground();
                if (row < rh && col < rw) {
                    int idx = row * rw + col;
                    if (idx < fi.size() && !fi.get(idx).ingredient().isEmpty()) {
                        slot.addIngredients(fi.get(idx).ingredient());
                    }
                }
            }
        }

        builder.addOutputSlot(107, 22)
                .setOutputSlotBackground()
                .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public void draw(ForgingRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, 79, 22);

        var font = Minecraft.getInstance().font;
        graphics.drawString(font, "Hits: " + recipe.getHammeringRequired(), 22, 59, 0xFF808080, false);
        String tier = recipe.getAnvilTier();
        if (!tier.isBlank()) {
            String tierDisplay = tier.substring(0, 1).toUpperCase() + tier.substring(1);
            graphics.drawString(font, "Tier: " + tierDisplay, 90, 59, 0xFF808080, false);
        }
    }
}
