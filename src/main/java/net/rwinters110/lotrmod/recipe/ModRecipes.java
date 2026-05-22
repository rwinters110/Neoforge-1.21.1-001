package net.rwinters110.lotrmod.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rwinters110.lotrmod.lotrmod;

import java.util.function.Supplier;

public class ModRecipes {
    // Registros específicos de NeoForge para Recetas
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, lotrmod.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, lotrmod.MOD_ID);

    // Registramos nuestro serializador del Tonel. ¡Esto es lo que buscaba el error rojo!
    public static final Supplier<RecipeSerializer<KegRecipe>> KEG_SERIALIZER =
            SERIALIZERS.register(KegRecipe.Type.ID, KegRecipe.Serializer::new);

    public static final Supplier<RecipeType<KegRecipe>> KEG_TYPE =
            TYPES.register(KegRecipe.Type.ID, () -> KegRecipe.Type.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}