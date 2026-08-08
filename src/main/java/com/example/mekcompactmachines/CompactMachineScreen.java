package com.example.mekcompactmachines;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import static com.example.mekcompactmachines.MyMekAddon.MODID;

public class CompactMachineScreen extends AbstractContainerScreen<CompactMachineMenu> {
	private static final ResourceLocation TEXTURE= new ResourceLocation(MODID, "textures/gui/craftingtable_gui.png");

	public CompactMachineScreen(CompactMachineMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	protected void init() {
		super.init();
	}
	private static final ResourceLocation GHOST_ICON =
			new ResourceLocation(MODID, "textures/gui/ghost_icon.png"); // ※画像パスは適宜変えてください
	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
		int progressWidth = menu.getScaledProgress();
		graphics.blit(TEXTURE, x + 86, y + 36, 178, 6, progressWidth, 15);
		if (!this.menu.slots.get(0).hasItem()) {
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.35F);
			RenderSystem.setShaderTexture(0, GHOST_ICON);
			graphics.blit(GHOST_ICON, x + 30, y + 17, 0, 0, 16, 16, 16, 16);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.disableBlend();
			RenderSystem.setShaderTexture(0, TEXTURE);
		}
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
		renderTooltip(graphics, mouseX, mouseY);
	}

}