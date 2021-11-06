package hexagon.buzzydrones.core.registry;

import hexagon.buzzydrones.common.block.IdleStationBlock;
import hexagon.buzzydrones.common.block.SourceStationBlock;
import hexagon.buzzydrones.common.block.TargetStationBlock;
import hexagon.buzzydrones.core.BuzzyDrones;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BuzzyDronesBlocks {
    
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, BuzzyDrones.ID);
    
    public static final RegistryObject<SourceStationBlock> SOURCE_STATION = REGISTER.register("source_station", SourceStationBlock::new);
    public static final RegistryObject<TargetStationBlock> TARGET_STATION = REGISTER.register("target_station", TargetStationBlock::new);
    public static final RegistryObject<IdleStationBlock> IDLE_STATION = REGISTER.register("idle_station", IdleStationBlock::new);
}
