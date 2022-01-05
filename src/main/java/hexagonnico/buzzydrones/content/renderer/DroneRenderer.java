package hexagonnico.buzzydrones.content.renderer;

import hexagonnico.buzzydrones.BuzzyDrones;
import hexagonnico.buzzydrones.content.entity.DroneEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@OnlyIn(Dist.CLIENT)
public class DroneRenderer extends MobRenderer<DroneEntity, DroneModel<DroneEntity>> {

	private static final ResourceLocation droneBlueTexture = new ResourceLocation(BuzzyDrones.ID, "textures/entity/drone_blue.png");
	private static final ResourceLocation droneGreenTexture = new ResourceLocation(BuzzyDrones.ID, "textures/entity/drone_green.png");
	private static final ResourceLocation droneOrangeTexture = new ResourceLocation(BuzzyDrones.ID, "textures/entity/drone_orange.png");
	private static final ResourceLocation droneRedTexture = new ResourceLocation(BuzzyDrones.ID, "textures/entity/drone_red.png");

	public DroneRenderer(EntityRendererProvider.Context renderProvider) {
		super(renderProvider, new DroneModel<>(), 0.4f);
	}

	@Override
	public ResourceLocation getTextureLocation(DroneEntity entity) {
		return switch (entity.getStatus()) {
			case WORKING -> droneBlueTexture;
			case IDLE -> droneGreenTexture;
			case WARNING -> droneOrangeTexture;
			default -> droneRedTexture;
		};
	}
}
