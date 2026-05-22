
package net.rwinters110.lotrmod.block.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.rwinters110.lotrmod.inventory.KegMenu;
import net.rwinters110.lotrmod.recipe.KegRecipe;
import net.rwinters110.lotrmod.recipe.KegRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class KegBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private boolean isBrewing = false;

    private int brewingTime = 0;
    private int barrelState = 0; // 0=Empty, 1=Weak, 2=Light, 3=Moderate, 4=Strong, 5=Potent
    private int totalIngredients = 0;
    private String currentBrewName = "None";

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> KegBlockEntity.this.brewingTime;
                case 1 -> KegBlockEntity.this.barrelState;
                case 2 -> KegBlockEntity.this.totalIngredients;
                case 3 -> KegBlockEntity.this.isBrewing ? 1 : 0;
                default -> 0;
            };
        }
        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> KegBlockEntity.this.brewingTime = value;
                case 1 -> KegBlockEntity.this.barrelState = value;
                case 2 -> KegBlockEntity.this.totalIngredients = value;
                case 3 -> KegBlockEntity.this.isBrewing = (value == 1);
            }
        }
        @Override public int getCount() { return 4; }
    };

    public KegBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KEG.get(), pos, state);
    }

    // Silenciamos los parámetros pos y state que NeoForge exige pero que no usamos en la lógica interna
    public void tick(Level level, @SuppressWarnings("unused") BlockPos pos, @SuppressWarnings("unused") BlockState state) {
        if (level.isClientSide) return;

        if (this.isBrewing && this.barrelState > 0 && this.barrelState < 5) {
            this.brewingTime++;

            if (this.brewingTime == 12000) { this.barrelState = 2; setChanged(); }
            else if (this.brewingTime == 24000) { this.barrelState = 3; setChanged(); }
            else if (this.brewingTime == 36000) { this.barrelState = 4; setChanged(); }
            else if (this.brewingTime == 48000) { this.barrelState = 5; this.isBrewing = false; setChanged(); }
        }
    }

    private void clearCraftingGrid() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = this.itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.is(Items.WATER_BUCKET)) {
                    this.itemHandler.setStackInSlot(i, new ItemStack(Items.BUCKET));
                } else {
                    this.itemHandler.extractItem(i, 1, false);
                }
            }
        }
    }

    public void handleButtonClick() {
        if (this.level == null || this.level.isClientSide) return;

        if (this.barrelState == 0) {
            KegRecipeInput input = new KegRecipeInput(this.itemHandler);
            boolean hasWater = this.itemHandler.getStackInSlot(6).is(Items.WATER_BUCKET) &&
                    this.itemHandler.getStackInSlot(7).is(Items.WATER_BUCKET) &&
                    this.itemHandler.getStackInSlot(8).is(Items.WATER_BUCKET);

            if (hasWater) {
                Optional<RecipeHolder<KegRecipe>> recipe = level.getRecipeManager().getRecipeFor(KegRecipe.Type.INSTANCE, input, level);
                if (recipe.isPresent()) {
                    this.barrelState = 1;
                    this.brewingTime = 1;
                    this.totalIngredients = 16;
                    this.isBrewing = true;
                    this.currentBrewName = recipe.get().value().getResultName();
                    clearCraftingGrid();
                    setChanged();
                }
            }
        }
        else if (this.barrelState >= 1 && this.isBrewing) {
            this.isBrewing = false;
            setChanged();
        }
    }

    @SuppressWarnings("unused")
    public int getBarrelState() { return this.barrelState; }

    public String getQualityText() {
        if (this.barrelState == 0) return Component.translatable("quality.lotrmod.empty").getString();

        return switch (this.barrelState) {
            case 1 -> Component.translatable("quality.lotrmod.weak").getString();
            case 2 -> Component.translatable("quality.lotrmod.light").getString();
            case 3 -> Component.translatable("quality.lotrmod.moderate").getString();
            case 4 -> Component.translatable("quality.lotrmod.strong").getString();
            case 5 -> Component.translatable("quality.lotrmod.potent").getString();
            default -> Component.translatable("quality.lotrmod.unknown").getString();
        } + " " + this.currentBrewName;
    }

    @SuppressWarnings("unused")
    public void consumeServing() {
        if (this.totalIngredients > 0) {
            this.totalIngredients--;
            if (this.totalIngredients <= 0) {
                this.barrelState = 0;
                this.brewingTime = 0;
                this.currentBrewName = "None";
            }
            setChanged();
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("BrewingTime", this.brewingTime);
        tag.putInt("BarrelState", this.barrelState);
        tag.putInt("TotalIngredients", this.totalIngredients);
        tag.putString("BrewName", this.currentBrewName);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("inventory")) {
            itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        }
        this.brewingTime = tag.getInt("BrewingTime");
        this.barrelState = tag.getInt("BarrelState");
        this.totalIngredients = tag.getInt("TotalIngredients");
        this.currentBrewName = tag.getString("BrewName");
    }

    public ItemStackHandler getItemHandler() { return itemHandler; }

    @SuppressWarnings("unused")
    public boolean isBrewing() { return this.isBrewing; }

    @SuppressWarnings("unused")
    public int getTotalIngredients() {
        return this.totalIngredients;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.lotrmod.keg");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new KegMenu(id, inventory, this, this.data);
    }
}