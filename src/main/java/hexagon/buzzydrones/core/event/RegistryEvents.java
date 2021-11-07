package hexagon.buzzydrones.core.event;

import hexagon.buzzydrones.client.renderer.DroneRenderer;
import hexagon.buzzydrones.common.entity.DroneEntity;
import hexagon.buzzydrones.core.BuzzyDrones;
import hexagon.buzzydrones.core.registry.BuzzyDronesEntities;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BuzzyDrones.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    @SubscribeEvent
    public static void registerAttributesEvent(EntityAttributeCreationEvent event) {
        event.put(BuzzyDronesEntities.DRONE.get(), DroneEntity.setAttributes().build());
    }
    
    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BuzzyDronesEntities.DRONE.get(), DroneRenderer::new);
    }
}
