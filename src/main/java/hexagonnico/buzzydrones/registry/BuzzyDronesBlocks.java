package hexagonnico.buzzydrones.registry;

import hexagonnico.buzzydrones.BuzzyDrones;
import hexagonnico.buzzydrones.content.block.IdleStationBlock;
import hexagonnico.buzzydrones.content.block.SourceStationBlock;
import hexagonnico.buzzydrones.content.block.TargetStationBlock;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.block.Block;

public class BuzzyDronesBlocks {

	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, BuzzyDrones.ID);

	public static final RegistryObject<SourceStationBlock> SOURCE_STATION = REGISTER.register("source_station", SourceStationBlock::new);
	public static final RegistryObject<TargetStationBlock> TARGET_STATION = REGISTER.register("target_station", TargetStationBlock::new);
	public static final RegistryObject<IdleStationBlock> IDLE_STATION = REGISTER.register("idle_station", IdleStationBlock::new);
}
