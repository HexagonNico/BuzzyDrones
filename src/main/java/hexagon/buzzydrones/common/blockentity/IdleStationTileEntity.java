package hexagon.buzzydrones.common.blockentity;

import hexagon.buzzydrones.core.registry.BuzzyDronesTileEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IdleStationTileEntity extends BlockEntity {

    public IdleStationTileEntity(BlockPos pos, BlockState state) {
        super(BuzzyDronesTileEntities.IDLE_STATION.get(), pos, state);
    }
}
