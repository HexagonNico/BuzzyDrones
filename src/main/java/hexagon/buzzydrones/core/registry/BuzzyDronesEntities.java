package hexagon.buzzydrones.core.registry;

import hexagon.buzzydrones.common.entity.DroneEntity;
import hexagon.buzzydrones.core.BuzzyDrones;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BuzzyDronesEntities {
    
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, BuzzyDrones.ID);
    
    public static final RegistryObject<EntityType<DroneEntity>> DRONE = REGISTER.register("drone", () -> EntityType.Builder.<DroneEntity>of(DroneEntity::new, MobCategory.MISC).sized(0.7f, 0.6f).clientTrackingRange(8).build("drone"));
}
