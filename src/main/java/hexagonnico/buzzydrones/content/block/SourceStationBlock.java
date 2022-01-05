package hexagonnico.buzzydrones.content.block;

import hexagonnico.buzzydrones.content.blockentity.SourceStationBlockEntity;
import hexagonnico.buzzydrones.registry.BuzzyDronesBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SourceStationBlock extends AbstractStationBlock {

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SourceStationBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return level.isClientSide ? null : createTickerHelper(blockEntityType, BuzzyDronesBlockEntities.SOURCE_STATION.get(), SourceStationBlockEntity::serverTick);
	}
}
