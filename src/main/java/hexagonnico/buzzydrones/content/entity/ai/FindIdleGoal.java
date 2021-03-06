package hexagonnico.buzzydrones.content.entity.ai;

import hexagonnico.buzzydrones.content.entity.DroneEntity;
import hexagonnico.buzzydrones.content.tileentity.IdleStationTileEntity;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;

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
		List<IdleStationTileEntity> list = this.getNearbyStations();
		if(!list.isEmpty()) {
			this.wanderAround(list.get(0).getBlockPos());
		}
	}

	private List<IdleStationTileEntity> getNearbyStations() {
		BlockPos dronePos = this.droneEntity.blockPosition();
		return BlockPos.betweenClosedStream(dronePos.offset(-15, -15, -15), dronePos.offset(15, 15, 15))
				.map(pos -> this.droneEntity.level.getBlockEntity(pos))
				.filter(tileEntity -> tileEntity instanceof IdleStationTileEntity)
				.map(tileEntity -> (IdleStationTileEntity) tileEntity)
				.collect(Collectors.toList());
	}

	private void wanderAround(BlockPos pos) {
		PathNavigator navigator = this.droneEntity.getNavigation();
		float xOffset = this.random.nextInt((3 - -3) + 1) + -3;
		float yOffset = this.random.nextInt(3) + 1;
		float zOffset = this.random.nextInt((3 - -3) + 1) + -3;
		navigator.moveTo(navigator.createPath(pos.offset(xOffset, yOffset, zOffset), 1), 1.0);
	}
}
