package hexagonnico.buzzydrones.content.entity.ai;

import hexagonnico.buzzydrones.content.blockentity.IdleStationBlockEntity;
import hexagonnico.buzzydrones.content.entity.DroneEntity;

import java.util.Optional;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class FindIdleGoal extends Goal {

	private final DroneEntity droneEntity;
	private final Random random;

	public FindIdleGoal(DroneEntity droneEntity) {
		this.droneEntity = droneEntity;
		this.random = new Random();
	}

	@Override
	public boolean canUse() {
		return this.droneEntity.getStatus() != DroneEntity.Status.WORKING && this.droneEntity.getNavigation().isDone();
	}

	@Override
	public boolean canContinueToUse() {
		return this.droneEntity.getNavigation().isInProgress();
	}

	@Override
	public void start() {
		this.getClosestStation().ifPresent(station -> this.wanderAround(station.getBlockPos()));
	}

	private Optional<IdleStationBlockEntity> getClosestStation() {
		BlockPos dronePos = this.droneEntity.blockPosition();
		return BlockPos.betweenClosedStream(dronePos.offset(-15, -15, -15), dronePos.offset(15, 15, 15))
				.map(this.droneEntity.level::getBlockEntity)
				.filter(IdleStationBlockEntity.class::isInstance)
				.map(IdleStationBlockEntity.class::cast)
				.findFirst();
	}

	private void wanderAround(BlockPos pos) {
		PathNavigation navigation = this.droneEntity.getNavigation();
		float xOffset = this.random.nextInt((3 - -3) + 1) + -3;
		float yOffset = this.random.nextInt(3) + 1;
		float zOffset = this.random.nextInt((3 - -3) + 1) + -3;
		navigation.moveTo(navigation.createPath(pos.offset(xOffset, yOffset, zOffset), 1), 1.0);
	}
}
