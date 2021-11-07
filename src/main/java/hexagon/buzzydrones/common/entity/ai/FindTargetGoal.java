package hexagon.buzzydrones.common.entity.ai;

import hexagon.buzzydrones.common.blockentity.TargetStationTileEntity;
import hexagon.buzzydrones.common.entity.DroneEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class FindTargetGoal extends Goal {

    private final DroneEntity droneEntity;

    public FindTargetGoal(DroneEntity droneEntity) {
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
        List<TargetStationTileEntity> list = this.getNearbyTargets();
        if(!list.isEmpty()) {
            for(TargetStationTileEntity tileEntity : list) {
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

    private List<TargetStationTileEntity> getNearbyTargets() {
        BlockPos dronePos = this.droneEntity.blockPosition();
        return BlockPos.betweenClosedStream(dronePos.offset(-15, -15, -15), dronePos.offset(15, 15, 15))
                .map(pos -> this.droneEntity.level.getBlockEntity(pos))
                .filter(tileEntity -> tileEntity instanceof TargetStationTileEntity)
                .map(tileEntity -> (TargetStationTileEntity) tileEntity)
                .sorted(Comparator.comparingDouble(tileEntity -> tileEntity.getDistance(this.droneEntity.blockPosition())))
                .collect(Collectors.toList());
    }

    private boolean targetIsValid(TargetStationTileEntity tileEntity) {
        return tileEntity.isFree() && !tileEntity.hasFilter() && tileEntity.hasRoomFor(this.droneEntity.getItemCarried());
    }

    private void goTo(BlockPos pos) {
        PathNavigation navigation = this.droneEntity.getNavigation();
        navigation.moveTo(navigation.createPath(pos, 1), 1.0);
        this.droneEntity.setStatus(DroneEntity.Status.WORKING);
    }
}
