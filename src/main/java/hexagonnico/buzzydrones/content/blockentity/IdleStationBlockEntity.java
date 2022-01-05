package hexagonnico.buzzydrones.content.blockentity;

import hexagonnico.buzzydrones.registry.BuzzyDronesBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IdleStationBlockEntity extends BlockEntity {

	public IdleStationBlockEntity(BlockPos pos, BlockState state) {
		super(BuzzyDronesBlockEntities.IDLE_STATION.get(), pos, state);
	}
}
