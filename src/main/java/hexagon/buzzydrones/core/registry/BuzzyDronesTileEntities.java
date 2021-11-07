package hexagon.buzzydrones.core.registry;

import hexagon.buzzydrones.common.blockentity.IdleStationTileEntity;
import hexagon.buzzydrones.common.blockentity.SourceStationTileEntity;
import hexagon.buzzydrones.common.blockentity.TargetStationTileEntity;
import hexagon.buzzydrones.core.BuzzyDrones;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BuzzyDronesTileEntities {
    
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, BuzzyDrones.ID);
    
    public static final RegistryObject<BlockEntityType<SourceStationTileEntity>> SOURCE_STATION = REGISTER.register("source_station", () -> BlockEntityType.Builder.of(SourceStationTileEntity::new, BuzzyDronesBlocks.SOURCE_STATION.get()).build(null));
    public static final RegistryObject<BlockEntityType<TargetStationTileEntity>> TARGET_STATION = REGISTER.register("target_station", () -> BlockEntityType.Builder.of(TargetStationTileEntity::new, BuzzyDronesBlocks.TARGET_STATION.get()).build(null));
    public static final RegistryObject<BlockEntityType<IdleStationTileEntity>> IDLE_STATION = REGISTER.register("idle_station", () -> BlockEntityType.Builder.of(IdleStationTileEntity::new, BuzzyDronesBlocks.IDLE_STATION.get()).build(null));
}
