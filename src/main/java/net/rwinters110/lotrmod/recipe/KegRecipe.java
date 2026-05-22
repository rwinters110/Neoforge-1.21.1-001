package net.rwinters110.lotrmod.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ClassCanBeRecord")
public class KegRecipe implements Recipe<KegRecipeInput> {

    private final ShapedRecipePattern pattern;
    private final ItemStack result;

    public KegRecipe(ShapedRecipePattern pattern, ItemStack result) {
        this.pattern = pattern;
        this.result = result;
    }

    // Leemos el nombre directamente del ítem resultante
    public String getResultName() {
        return this.result.getHoverName().getString();
    }

    @Override
    public boolean matches(@NotNull KegRecipeInput input, @NotNull Level level) {
        if (level.isClientSide) return false;

        // Las recetas del tonel DEBEN ocupar toda la cuadrícula (3x3 = 9 slots)
        if (this.pattern.width() != 3 || this.pattern.height() != 3) {
            return false;
        }

        NonNullList<Ingredient> ingredients = this.pattern.ingredients();

        // Comparamos los 9 slots (0 al 5 son ingredientes, 6 al 8 son los cubos de agua)
        for (int i = 0; i < 9; i++) {
            Ingredient ingredient = ingredients.get(i);
            ItemStack stackInSlot = input.getItem(i);

            if (!ingredient.test(stackInSlot)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull KegRecipeInput input, @NotNull HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.pattern.ingredients();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.KEG_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<KegRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "keg_brewing";
        private Type() {}
    }

    // =========================================================================
    // CODECS Y SERIALIZADORES (Usando el estándar de patrones de Mojang 1.21.1)
    // =========================================================================
    public static class Serializer implements RecipeSerializer<KegRecipe> {

        private static final MapCodec<KegRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
        ).apply(inst, KegRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, KegRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    ShapedRecipePattern.STREAM_CODEC.encode(buf, recipe.pattern);
                    ItemStack.STREAM_CODEC.encode(buf, recipe.result);
                },
                buf -> {
                    ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buf);
                    ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                    return new KegRecipe(pattern, result);
                }
        );

        @Override
        public @NotNull MapCodec<KegRecipe> codec() { return CODEC; }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, KegRecipe> streamCodec() { return STREAM_CODEC; }
    }
}