package net.rwinters110.lotrmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rwinters110.lotrmod.block.entity.KegBlockEntity;
import net.rwinters110.lotrmod.block.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class KegBlock extends Block implements EntityBlock {
    // El Codec requerido por las nuevas versiones de Minecraft
    public static final MapCodec<KegBlock> CODEC = simpleCodec(KegBlock::new);

    // La caja de colisiones exacta (2 píxeles hacia dentro, idéntica a la 1.16.5)
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D);

    public KegBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    // Aplicar la colisión estrecha al bloque
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // Crear el "cerebro" (Block Entity) del tonel
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KegBlockEntity(pos, state);
    }

    // El Ticker: El reloj interno que hace avanzar el tiempo de destilación en el servidor
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return createTickerHelper(type, ModBlockEntities.KEG.get(),
                (lvl, pos, st, blockEntity) -> blockEntity.tick(lvl, pos, st));
    }

    // Método auxiliar para el Ticker
    @SuppressWarnings("unchecked")
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
    }

    // El detector de Clic Derecho: Ejecuta el sonido de barril y abre la interfaz gráfica
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof KegBlockEntity kegEntity) {
                level.playSound(null, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.5f, level.random.nextFloat() * 0.1f + 0.9f);
                player.openMenu(kegEntity, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}