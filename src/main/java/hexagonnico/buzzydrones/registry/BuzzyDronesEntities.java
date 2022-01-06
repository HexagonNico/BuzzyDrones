package hexagonnico.buzzydrones.registry;

import hexagonnico.buzzydrones.BuzzyDrones;
import hexagonnico.buzzydrones.common.entity.DroneEntity;
import hexagonnico.buzzydrones.common.renderer.DroneRenderer;

import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;

public class BuzzyDronesEntities {

	public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, BuzzyDrones.ID);

	public static final RegistryObject<EntityType<DroneEntity>> DRONE = REGISTER.register("drone", () -> EntityType.Builder.<DroneEntity>of(DroneEntity::new, EntityClassification.MISC).sized(0.7f, 0.6f).clientTrackingRange(8).build("drone"));

	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(DRONE.get(), DroneRenderer::new);
	}

	public static void registerAttributes() {
		DeferredWorkQueue.runLater(() -> GlobalEntityTypeAttributes.put(DRONE.get(), DroneEntity.setAttributes().build()));
	}
}
