package net.rwinters110.lotrmod.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class KegRecipeInput implements RecipeInput {
    private final ItemStackHandler itemHandler;

    public KegRecipeInput(ItemStackHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        // Mapea directamente los slots del contenido
        return this.itemHandler.getStackInSlot(index);
    }

    @Override
    public int size() {
        // Evaluamos los 9 slots de entrada (6 ingredientes + 3 aguas)
        return 9;
    }
}