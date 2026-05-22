package net.rwinters110.lotrmod.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.rwinters110.lotrmod.block.entity.KegBlockEntity;
import net.rwinters110.lotrmod.block.ModBlocks;
import org.jetbrains.annotations.NotNull;

public class KegMenu extends AbstractContainerMenu {
    public final KegBlockEntity blockEntity;
    private final ContainerData data;

    // Constructor Cliente
    public KegMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public boolean isBrewing() { return this.data.get(3) == 1; }

    // Constructor Servidor
    public KegMenu(int containerId, Inventory playerInventory, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.KEG_MENU.get(), containerId);
        // Nota rápida: Si tienes 10 slots en total en el bloque (6 ingred + 3 agua + 1 salida),
        // quizá más adelante debas subir este 9 a un 10 si te da algún error en consola.
        checkContainerSize(playerInventory, 10);
        this.blockEntity = (KegBlockEntity) entity;
        this.data = data;

        // 6 huecos de INGREDIENTES
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), col + row * 3, 14 + col * 18, 34 + row * 18));
            }
        }

        // 3 huecos de AGUA
        for (int col = 0; col < 3; col++) {
            this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 6 + col, 14 + col * 18, 70));
        }

        // El hueco de RESULTADO (donde sale el licor)
        this.addSlot(new SlotItemHandler(this.blockEntity.getItemHandler(), 9, 108, 52));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        this.addDataSlots(data);
    }

    // Le decimos al IDE "ya sé que no los uso todavía, no me avises"
    @SuppressWarnings("unused")
    public int getBrewingTime() { return this.data.get(0); }
    @SuppressWarnings("unused")
    public int getBarrelState() { return this.data.get(1); }
    @SuppressWarnings("unused")
    public int getTotalIngredients() { return this.data.get(2); }
    @SuppressWarnings("unused")
    public String getBrewName() { return this.blockEntity.getQualityText(); }

    @Override
    public boolean stillValid(@NotNull Player player) {
        // Guardamos el nivel en una variable y comprobamos que no sea nulo antes de usarlo
        Level level = this.blockEntity.getLevel();
        if (level == null) return false;

        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.KEG.get());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        // Se quitó "slot != null" porque la lista de slots nunca devuelve nulos
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 9) {
                if (!this.moveItemStackTo(itemstack1, 9, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(itemstack1, 0, 9, false)) return ItemStack.EMPTY;

            if (itemstack1.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        }
        return itemstack;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        int invX = 24;
        int invY = 138;

        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, invX + l * 18, invY + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        int hotbarX = 24;
        int hotbarY = 196;

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, hotbarX + i * 18, hotbarY));
        }
    }

    // Este es el correo interno de Minecraft. Si recibe un clic con la ID 0, avisa al bloque.
    @Override
    public boolean clickMenuButton(@NotNull Player player, int id) {
        if (id == 0) {
            this.blockEntity.handleButtonClick();
            return true;
        }
        return false;
    }
}