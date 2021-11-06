package hexagon.buzzydrones.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import hexagon.buzzydrones.BuzzyDrones;
import hexagon.buzzydrones.client.renderer.DroneRenderer;

@Mod.EventBusSubscriber(modid = BuzzyDrones.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final EntityType<DroneEntity> DRONE = EntityType.Builder.<DroneEntity>of(
            DroneEntity::new, EntityClassification.MISC
    ).sized(0.7f, 0.6f).setTrackingRange(8).build("drone");

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> register) {
        register.getRegistry().registerAll(
                DRONE.setRegistryName("drone")
        );
    }

    public static void registerRenders() {
        RenderingRegistry.registerEntityRenderingHandler(DRONE, DroneRenderer::new);
    }

    public static void registerAttributes() {
        DeferredWorkQueue.runLater(() -> GlobalEntityTypeAttributes.put(DRONE, DroneEntity.setAttributes().build()));
    }
}
