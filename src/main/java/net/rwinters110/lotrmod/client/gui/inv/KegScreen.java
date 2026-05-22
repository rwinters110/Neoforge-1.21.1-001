package net.rwinters110.lotrmod.client.gui.inv;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.rwinters110.lotrmod.lotrmod;
import net.rwinters110.lotrmod.inventory.KegMenu;

import org.jetbrains.annotations.NotNull;

public class KegScreen extends AbstractContainerScreen<KegMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(lotrmod.MOD_ID, "textures/gui/keg/keg.png");

    public KegScreen(KegMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 209;
        this.imageHeight = 220;

        this.titleLabelX = 40;
        this.titleLabelY = 6;
        this.inventoryLabelX = 24;
        this.inventoryLabelY = 126;
    }

    @Override
    protected void init() {
        super.init();

        // Ajuste fino definitivo: movido a la izquierda y más abajo
        int btnX = this.leftPos + 83; // Ajustado para que no toque el tanque de la derecha
        int btnY = this.topPos + 90; // Bajado para que encaje centrado en la caja oscura

        this.addRenderableWidget(new KegStartButton(btnX, btnY));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY); // CORRECCIÓN: Se quitó partialTick aquí
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    private class KegStartButton extends AbstractButton {
        private static final int WIDTH = 44;
        private static final int HEIGHT = 16;

        private static final int UV_X = 210;
        private static final int UV_Y = 0;

        public KegStartButton(int x, int y) {
            super(x, y, WIDTH, HEIGHT, Component.empty());
        }

        @Override
        public void onPress() {
            // Se añadió una comprobación de seguridad para evitar NullPointerException (Warning mitigado)
            if (KegScreen.this.minecraft != null && KegScreen.this.minecraft.gameMode != null) {
                KegScreen.this.minecraft.gameMode.handleInventoryButtonClick(KegScreen.this.menu.containerId, 0);
            }
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            boolean disponible = KegScreen.this.menu.isBrewing();

            int v = UV_Y; // Reducido para evitar el warning de "variable redundante"

            if (disponible) {
                if (this.isHoveredOrFocused()) {
                    v = UV_Y + (HEIGHT * 2);
                } else {
                    v = UV_Y + HEIGHT;
                }
            }

            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), UV_X, v, WIDTH, HEIGHT);
        }

        @Override
        protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }
    }
}