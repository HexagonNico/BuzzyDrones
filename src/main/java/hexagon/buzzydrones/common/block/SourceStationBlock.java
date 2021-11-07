package hexagon.buzzydrones.common.block;

import hexagon.buzzydrones.common.blockentity.SourceStationTileEntity;
import hexagon.buzzydrones.core.registry.BuzzyDronesTileEntities;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SourceStationBlock extends AbstractStationBlock {
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SourceStationTileEntity(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, BuzzyDronesTileEntities.SOURCE_STATION.get(), SourceStationTileEntity::serverTick);
    }
}
