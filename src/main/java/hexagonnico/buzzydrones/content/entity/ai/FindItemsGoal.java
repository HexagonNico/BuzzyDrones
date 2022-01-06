package hexagonnico.buzzydrones.content.entity.ai;

import hexagonnico.buzzydrones.content.entity.DroneEntity;

import java.util.Comparator;
import java.util.Optional;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

public class FindItemsGoal extends Goal {

	private final DroneEntity droneEntity;

	public FindItemsGoal(DroneEntity droneEntity) {
		this.droneEntity = droneEntity;
	}

	@Override
	public boolean canUse() {
		return !this.droneEntity.isCarryingItems() && this.droneEntity.getNavigation().isDone();
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		this.lookForItems();
		this.pickUpItems();
	}

	private void pickUpItems() {
		this.getClosestItem(1.5).ifPresent(item -> this.droneEntity.pickUpAllItems(item.getItem()));
	}

	private void lookForItems() {
		this.getClosestItem(15.0).ifPresentOrElse(item -> {
			this.droneEntity.getNavigation().moveTo(item, 1.0);
			this.droneEntity.setStatus(DroneEntity.Status.WORKING);
		}, () -> {
			this.droneEntity.setStatus(DroneEntity.Status.IDLE);
		});
	}

	private Optional<ItemEntity> getClosestItem(double range) {
		AABB box = this.droneEntity.getBoundingBox().inflate(range);
		return this.droneEntity.level.getEntitiesOfClass(ItemEntity.class, box).stream()
				.sorted(Comparator.comparingDouble(item -> item.blockPosition().distSqr(this.droneEntity.blockPosition())))
				.findFirst();
	}
}
