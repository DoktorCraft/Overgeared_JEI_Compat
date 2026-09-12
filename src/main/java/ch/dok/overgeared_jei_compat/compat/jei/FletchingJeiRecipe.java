package ch.dok.overgeared_jei_compat.compat.jei;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public final class FletchingJeiRecipe {
    public final Ingredient tip;
    public final Ingredient shaft;
    public final Ingredient feather;
    @Nullable public final ItemStack potionInput;
    public final ItemStack output;

    public FletchingJeiRecipe(Ingredient tip, Ingredient shaft, Ingredient feather, ItemStack output) {
        this.tip = tip;
        this.shaft = shaft;
        this.feather = feather;
        this.potionInput = null;
        this.output = output;
    }

    public FletchingJeiRecipe(Ingredient tip, Ingredient shaft, Ingredient feather,
                              ItemStack potionInput, ItemStack output) {
        this.tip = tip;
        this.shaft = shaft;
        this.feather = feather;
        this.potionInput = potionInput;
        this.output = output;
    }
}
