package hexagonnico.buzzydrones.content.gui.screen;

import hexagonnico.buzzydrones.BuzzyDrones;
import hexagonnico.buzzydrones.content.container.TargetStationContainer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TargetGuiScreen extends ContainerScreen<TargetStationContainer> {

	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(BuzzyDrones.ID, "textures/gui/target.png");

	public TargetGuiScreen(TargetStationContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.passEvents = false;
		this.imageHeight = 133;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
		if(this.hoveredSlot != null && this.hoveredSlot.getSlotIndex() == 5 && this.hoveredSlot.getItem().isEmpty() && this.isHovering(0, 0, 176, 45, mouseX, mouseY))
			this.renderTooltip(matrixStack, new TranslationTextComponent("inventory.buzzydrones.filter"), mouseX, mouseY);
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(GUI_TEXTURE);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
}
