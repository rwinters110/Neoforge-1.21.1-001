package net.rwinters110.lotrmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class ModCakeBlock extends CakeBlock {
    private final int nutrition;
    private final float saturationModifier;

    // El constructor recibe las propiedades de bloque estándar y los valores nutricionales únicos
    public ModCakeBlock(Properties properties, int nutrition, float saturationModifier) {
        super(properties);
        this.nutrition = nutrition;
        this.saturationModifier = saturationModifier;
    }

    // Interceptamos el clic derecho cuando el jugador NO tiene un ítem especial interactivo en la mano
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            if (customEat(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }
        }
        return customEat(level, pos, state, player);
    }

    // Lógica personalizada para consumir una porción de la tarta
    protected InteractionResult customEat(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        // Si el jugador no puede comer (está lleno), pasamos de largo
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            // Añade la estadística de haber comido tarta en las estadísticas del jugador
            player.awardStat(Stats.EAT_CAKE_SLICE);

            // Aplicamos los valores de comida configurados para ESTA tarta concreta
            player.getFoodData().eat(this.nutrition, this.saturationModifier);

            // Obtenemos cuántos mordiscos ("bites") lleva la tarta actual (va de 0 a 6)
            int bites = state.getValue(BITES);

            // Disparamos el evento de sonido y partículas de comer
            level.gameEvent(player, GameEvent.EAT, pos);

            if (bites < 6) {
                // Si quedan porciones, actualizamos el estado del bloque sumando un mordisco
                level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            } else {
                // Si era la última porción (mordisco 6), destruimos el bloque de la tarta
                level.removeBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }

            return InteractionResult.SUCCESS;
        }
    }
}

