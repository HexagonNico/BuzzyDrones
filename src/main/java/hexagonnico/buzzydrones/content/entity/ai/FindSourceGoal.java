package hexagonnico.buzzydrones.content.entity.ai;

import hexagonnico.buzzydrones.content.blockentity.AbstractStationBlockEntity;
import hexagonnico.buzzydrones.content.blockentity.SourceStationBlockEntity;
import hexagonnico.buzzydrones.content.entity.DroneEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class FindSourceGoal extends Goal {

	private final DroneEntity droneEntity;

	public FindSourceGoal(DroneEntity droneEntity) {
		this.droneEntity = droneEntity;
	}

	@Override
	public boolean canUse() {
		return this.droneEntity.getNavigation().isDone() && !this.droneEntity.isCarryingItems();
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		List<SourceStationBlockEntity> list = this.getNearbySources();
		if(!list.isEmpty()) {
			for(SourceStationBlockEntity tileEntity : list) {
				if(this.sourceIsValid(tileEntity)) {
					this.goTo(tileEntity.getBlockPos());
					return;
				}
			}
			this.droneEntity.setStatus(DroneEntity.Status.IDLE);
		} else {
			this.droneEntity.setStatus(DroneEntity.Status.ERROR);
		}
	}

	private List<SourceStationBlockEntity> getNearbySources() {
		BlockPos dronePos = this.droneEntity.blockPosition();
		return BlockPos.betweenClosedStream(dronePos.offset(-15, -15, -15), dronePos.offset(15, 15, 15))
				.map(pos -> this.droneEntity.level.getBlockEntity(pos))
				.filter(tileEntity -> tileEntity instanceof SourceStationBlockEntity)
				.map(tileEntity -> (SourceStationBlockEntity) tileEntity)
				.sorted(Comparator.comparingInt(AbstractStationBlockEntity::getFullness).reversed())
				.collect(Collectors.toList());
	}

	private boolean sourceIsValid(SourceStationBlockEntity tileEntity) {
		return tileEntity.isFree() && !tileEntity.isEmpty();
	}

	private void goTo(BlockPos pos) {
		PathNavigation navigation = this.droneEntity.getNavigation();
		navigation.moveTo(navigation.createPath(pos, 1), 1.0);
		this.droneEntity.setStatus(DroneEntity.Status.WORKING);
	}
}
