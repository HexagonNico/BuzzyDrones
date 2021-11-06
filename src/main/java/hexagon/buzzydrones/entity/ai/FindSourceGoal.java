package hexagon.buzzydrones.entity.ai;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import hexagon.buzzydrones.entity.DroneEntity;
import hexagon.buzzydrones.tileentity.DroneSourceTileEntity;
import hexagon.buzzydrones.tileentity.DroneStationTileEntity;

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
        List<DroneSourceTileEntity> list = this.getNearbySources();
        if(!list.isEmpty()) {
            for(DroneSourceTileEntity tileEntity : list) {
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

    private List<DroneSourceTileEntity> getNearbySources() {
        BlockPos dronePos = this.droneEntity.blockPosition();
        return BlockPos.betweenClosedStream(dronePos.offset(-15, -15, -15), dronePos.offset(15, 15, 15))
                .map(pos -> this.droneEntity.level.getBlockEntity(pos))
                .filter(tileEntity -> tileEntity instanceof DroneSourceTileEntity)
                .map(tileEntity -> (DroneSourceTileEntity) tileEntity)
                .sorted(Comparator.comparingInt(DroneStationTileEntity::getFullness).reversed())
                .collect(Collectors.toList());
    }

    private boolean sourceIsValid(DroneSourceTileEntity tileEntity) {
        return tileEntity.isFree() && !tileEntity.isEmpty();
    }

    private void goTo(BlockPos pos) {
        PathNavigator navigator = this.droneEntity.getNavigation();
        navigator.moveTo(navigator.createPath(pos, 1), 1.0);
        this.droneEntity.setStatus(DroneEntity.Status.WORKING);
    }
}
