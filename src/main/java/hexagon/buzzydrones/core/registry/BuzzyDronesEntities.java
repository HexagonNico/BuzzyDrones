package hexagon.buzzydrones.core.registry;

import hexagon.buzzydrones.client.renderer.DroneRenderer;
import hexagon.buzzydrones.common.entity.DroneEntity;
import hexagon.buzzydrones.core.BuzzyDrones;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BuzzyDronesEntities {
    
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, BuzzyDrones.ID);
    
    public static final RegistryObject<EntityType<DroneEntity>> DRONE = REGISTER.register("drone", () -> EntityType.Builder.<DroneEntity>of(DroneEntity::new, EntityClassification.MISC).build("drone"));
    
    public static void registerRenders() {
        RenderingRegistry.registerEntityRenderingHandler(DRONE.get(), DroneRenderer::new);
    }
    
    public static void registerAttributes() {
        DeferredWorkQueue.runLater(() -> GlobalEntityTypeAttributes.put(DRONE.get(), DroneEntity.setAttributes().build()));
    }
}
