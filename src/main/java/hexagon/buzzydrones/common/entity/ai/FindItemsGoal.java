package hexagon.buzzydrones.common.entity.ai;

import hexagon.buzzydrones.common.entity.DroneEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        List<ItemEntity> items = this.getNearbyItems(1.5);
        if(!items.isEmpty()) {
            this.droneEntity.pickUpAllItems(items.get(0).getItem());
        }
    }

    private void lookForItems() {
        List<ItemEntity> items = this.getNearbyItems(15.0);
        if(!items.isEmpty()) {
            this.droneEntity.getNavigation().moveTo(items.get(0), 1.0);
            this.droneEntity.setStatus(DroneEntity.Status.WORKING);
        } else {
            this.droneEntity.setStatus(DroneEntity.Status.IDLE);
        }
    }

    private List<ItemEntity> getNearbyItems(double r) {
        AABB box = this.droneEntity.getBoundingBox().inflate(r);
        return this.droneEntity.level.getEntitiesOfClass(ItemEntity.class, box)
                .stream().sorted(Comparator.comparingDouble((item) -> item.blockPosition().distSqr(this.droneEntity.blockPosition()))).collect(Collectors.toList());
    }
}
