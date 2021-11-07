package hexagon.buzzydrones.common.entity.ai;

import hexagon.buzzydrones.common.blockentity.TargetStationBlockEntity;
import hexagon.buzzydrones.common.entity.DroneEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class FindTargetWithFilterGoal extends Goal {

    private final DroneEntity droneEntity;

    public FindTargetWithFilterGoal(DroneEntity droneEntity) {
        this.droneEntity = droneEntity;
    }

    @Override
    public boolean canUse() {
        return this.droneEntity.getNavigation().isDone() && this.droneEntity.isCarryingItems();
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        List<TargetStationBlockEntity> list = this.getNearbyTargets();
        if(!list.isEmpty()) {
            for(TargetStationBlockEntity tileEntity : list) {
                if(this.targetIsValid(tileEntity)) {
                    this.goTo(tileEntity.getBlockPos());
                    return;
                }
            }
            this.droneEntity.setStatus(DroneEntity.Status.WARNING);
        } else {
            this.droneEntity.setStatus(DroneEntity.Status.ERROR);
        }
    }

    private List<TargetStationBlockEntity> getNearbyTargets() {
        BlockPos dronePos = this.droneEntity.blockPosition();
        return BlockPos.betweenClosedStream(dronePos.offset(-15, -15, -15), dronePos.offset(15, 15, 15))
                .map(pos -> this.droneEntity.level.getBlockEntity(pos))
                .filter(tileEntity -> tileEntity instanceof TargetStationBlockEntity)
                .map(tileEntity -> (TargetStationBlockEntity) tileEntity)
                .filter(this::filterAllows)
                .sorted(Comparator.comparingDouble(tileEntity -> tileEntity.getDistance(this.droneEntity.blockPosition())))
                .collect(Collectors.toList());
    }

    private boolean filterAllows(TargetStationBlockEntity tileEntity) {
        return this.droneEntity.getItemCarried().equals(tileEntity.getFilter());
    }

    private boolean targetIsValid(TargetStationBlockEntity tileEntity) {
        return tileEntity.isFree() && tileEntity.hasRoomFor(this.droneEntity.getItemCarried());
    }

    private void goTo(BlockPos pos) {
        PathNavigation navigation = this.droneEntity.getNavigation();
        navigation.moveTo(navigation.createPath(pos, 1), 1.0);
        this.droneEntity.setStatus(DroneEntity.Status.WORKING);
    }
}
