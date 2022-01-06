package hexagonnico.buzzydrones.registry;

import hexagonnico.buzzydrones.BuzzyDrones;
import hexagonnico.buzzydrones.common.tileentity.IdleStationTileEntity;
import hexagonnico.buzzydrones.common.tileentity.SourceStationTileEntity;
import hexagonnico.buzzydrones.common.tileentity.TargetStationTileEntity;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.tileentity.TileEntityType;

public class BuzzyDronesTileEntities {

	public static final DeferredRegister<TileEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BuzzyDrones.ID);

	public static final RegistryObject<TileEntityType<SourceStationTileEntity>> SOURCE_STATION = REGISTER.register("source_station", () -> TileEntityType.Builder.of(SourceStationTileEntity::new, BuzzyDronesBlocks.SOURCE_STATION.get()).build(null));
	public static final RegistryObject<TileEntityType<TargetStationTileEntity>> TARGET_STATION = REGISTER.register("target_station", () -> TileEntityType.Builder.of(TargetStationTileEntity::new, BuzzyDronesBlocks.TARGET_STATION.get()).build(null));
	public static final RegistryObject<TileEntityType<IdleStationTileEntity>> IDLE_STATION = REGISTER.register("idle_station", () -> TileEntityType.Builder.of(IdleStationTileEntity::new, BuzzyDronesBlocks.IDLE_STATION.get()).build(null));
}
