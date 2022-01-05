package hexagonnico.buzzydrones.content.entity.ai;

import hexagonnico.buzzydrones.content.blockentity.SourceStationBlockEntity;
import hexagonnico.buzzydrones.content.entity.DroneEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EnterSourceGoal extends Goal {

	private final DroneEntity droneEntity;

	public EnterSourceGoal(DroneEntity droneEntity) {
		this.droneEntity = droneEntity;
	}

	@Override
	public boolean canUse() {
		return this.droneEntity.getNavigation().isInProgress() && !this.droneEntity.isCarryingItems();
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		BlockPos pos = this.droneEntity.getNavigation().getTargetPos();
		BlockEntity blockEntity = this.droneEntity.level.getBlockEntity(pos);
		if(blockEntity instanceof SourceStationBlockEntity sourceStationBlockEntity) {
			if(this.sourceIsWithinDistance(pos)) {
				this.droneEntity.getNavigation().stop();
				sourceStationBlockEntity.droneEnter(this.droneEntity);
			}
		}
	}

	private boolean sourceIsWithinDistance(BlockPos pos) {
		return pos.closerThan(this.droneEntity.position(), 1.5);
	}
}
