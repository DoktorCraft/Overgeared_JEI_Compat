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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.stirdrem.overgeared.block.ModBlocks;
import net.stirdrem.overgeared.components.CastData;
import net.stirdrem.overgeared.components.ModComponents;
import net.stirdrem.overgeared.item.ModItems;
import net.stirdrem.overgeared.recipe.CastingRecipe;

import java.util.List;
import java.util.Map;

public class CastingCategory extends AbstractRecipeCategory<CastingRecipe> {

    public static final RecipeType<CastingRecipe> RECIPE_TYPE =
            RecipeType.create("overgeared_jei_compat", "casting", CastingRecipe.class);

    // Single row: [material(2,11)] [cast(22,11)] → arrow(46,11) → [output(72,11)]
    // Flame (2,31), XP text (18,35)
    private static final int WIDTH  = 98;
    private static final int HEIGHT = 58;

    private final IDrawable arrow;
    private final IDrawableAnimated flame;

    public CastingCategory(IGuiHelper helper) {
        super(RECIPE_TYPE,
                Component.translatable("jei.overgeared_jei_compat.casting"),
                helper.createDrawableItemLike(ModBlocks.CASTING_FURNACE.get()),
                WIDTH, HEIGHT);
        this.arrow = helper.getRecipeArrow();
        this.flame = helper.createAnimatedRecipeFlame(200);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CastingRecipe recipe, IFocusGroup focuses) {
        String toolType = recipe.getToolType();
        Map<String, Integer> materials = recipe.getRequiredMaterials();

        // Cast/mold slot — left of the pair, cycles clay and nether cast
        CastData castData = new CastData("", toolType, Map.of(), 0, 0, List.of(), ItemStack.EMPTY, false);
        IRecipeSlotBuilder castSlot = builder.addInputSlot(2, 11).setStandardSlotBackground();
        ItemStack clayCast = new ItemStack(ModItems.CLAY_TOOL_CAST.get());
        clayCast.set(ModComponents.CAST_DATA.get(), castData);
        castSlot.addItemStack(clayCast);
        ItemStack netherCast = new ItemStack(ModItems.NETHER_TOOL_CAST.get());
        netherCast.set(ModComponents.CAST_DATA.get(), castData);
        castSlot.addItemStack(netherCast);

        // Material slot — right of cast, same row; ingots (units/9) and nuggets (units) cycle
        if (!materials.isEmpty()) {
            Map.Entry<String, Integer> entry = materials.entrySet().iterator().next();
            String materialId = entry.getKey();
            int units = entry.getValue();
            int ingotCount = Math.max(1, units / 9);

            IRecipeSlotBuilder slot = builder.addInputSlot(22, 11).setStandardSlotBackground();

            TagKey<Item> ingotsTag = TagKey.create(Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath("c", "ingots/" + materialId));
            BuiltInRegistries.ITEM.getTag(ingotsTag).ifPresent(named ->
                    named.forEach(holder ->
                            slot.addItemStack(new ItemStack(holder.value(), ingotCount))));

            TagKey<Item> nuggetsTag = TagKey.create(Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath("c", "nuggets/" + materialId));
            BuiltInRegistries.ITEM.getTag(nuggetsTag).ifPresent(named ->
                    named.forEach(holder ->
                            slot.addItemStack(new ItemStack(holder.value(), units))));
        }

        builder.addOutputSlot(72, 11)
                .setOutputSlotBackground()
                .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public void draw(CastingRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, 46, 11);
        flame.draw(graphics, 2, 31);
        String xpText = recipe.getExperience() + " XP";
        graphics.drawString(Minecraft.getInstance().font, xpText, 18, 35, 0xFFAAAAAA, false);
    }
}
