package hexagon.buzzydrones.core.registry;

import hexagon.buzzydrones.common.blockentity.IdleStationBlockEntity;
import hexagon.buzzydrones.common.blockentity.SourceStationBlockEntity;
import hexagon.buzzydrones.common.blockentity.TargetStationBlockEntity;
import hexagon.buzzydrones.core.BuzzyDrones;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BuzzyDronesTileEntities {
    
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BuzzyDrones.ID);
    
    public static final RegistryObject<BlockEntityType<SourceStationBlockEntity>> SOURCE_STATION = REGISTER.register("source_station", () -> BlockEntityType.Builder.of(SourceStationBlockEntity::new, BuzzyDronesBlocks.SOURCE_STATION.get()).build(null));
    public static final RegistryObject<BlockEntityType<TargetStationBlockEntity>> TARGET_STATION = REGISTER.register("target_station", () -> BlockEntityType.Builder.of(TargetStationBlockEntity::new, BuzzyDronesBlocks.TARGET_STATION.get()).build(null));
    public static final RegistryObject<BlockEntityType<IdleStationBlockEntity>> IDLE_STATION = REGISTER.register("idle_station", () -> BlockEntityType.Builder.of(IdleStationBlockEntity::new, BuzzyDronesBlocks.IDLE_STATION.get()).build(null));
}
