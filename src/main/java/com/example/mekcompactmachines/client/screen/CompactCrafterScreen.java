package com.example.mekcompactmachines.client.screen;

import com.example.mekcompactmachines.ModConstants;
import com.example.mekcompactmachines.block.CompactCraftor.CompactCrafterMenu;
import com.example.mekcompactmachines.block.CompactCraftor.CompactCrafterSlot;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CompactCrafterScreen extends AbstractContainerScreen<CompactCrafterMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/compact_crafter_gui.png");
    private static final ResourceLocation GHOST_ICON = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/gui/ghost_icon.png");

    public CompactCrafterScreen(CompactCrafterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); // 最初にベースを白に戻しておく
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // GUIの背景テクスチャを描画
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // スロット0（INPUT_1：ガラス）が空の場合、ゴーストアイコンをぴったり描画する
        if (!this.menu.slots.get(CompactCrafterSlot.INPUT_1.index()).hasItem()) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.35F); // 半透明にする
            RenderSystem.setShaderTexture(0, GHOST_ICON);

            int slotX = x + CompactCrafterSlot.INPUT_1.xPos();
            int slotY = y + CompactCrafterSlot.INPUT_1.yPos();
            graphics.blit(GHOST_ICON, slotX, slotY, 0, 0, 16, 16, 16, 16);

            // 描画が終わったらすぐに色とブレンドを元に戻す
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        }

        // 最後にベースのテクスチャに戻しておく
        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}