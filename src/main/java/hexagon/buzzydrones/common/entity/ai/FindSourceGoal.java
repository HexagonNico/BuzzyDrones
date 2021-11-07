package hexagon.buzzydrones.common.entity.ai;

import hexagon.buzzydrones.common.blockentity.AbstractStationTileEntity;
import hexagon.buzzydrones.common.blockentity.SourceStationTileEntity;
import hexagon.buzzydrones.common.entity.DroneEntity;

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
        List<SourceStationTileEntity> list = this.getNearbySources();
        if(!list.isEmpty()) {
            for(SourceStationTileEntity tileEntity : list) {
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

    private List<SourceStationTileEntity> getNearbySources() {
        BlockPos dronePos = this.droneEntity.blockPosition();
        return BlockPos.betweenClosedStream(dronePos.offset(-15, -15, -15), dronePos.offset(15, 15, 15))
                .map(pos -> this.droneEntity.level.getBlockEntity(pos))
                .filter(tileEntity -> tileEntity instanceof SourceStationTileEntity)
                .map(tileEntity -> (SourceStationTileEntity) tileEntity)
                .sorted(Comparator.comparingInt(AbstractStationTileEntity::getFullness).reversed())
                .collect(Collectors.toList());
    }

    private boolean sourceIsValid(SourceStationTileEntity tileEntity) {
        return tileEntity.isFree() && !tileEntity.isEmpty();
    }

    private void goTo(BlockPos pos) {
        PathNavigation navigation = this.droneEntity.getNavigation();
        navigation.moveTo(navigation.createPath(pos, 1), 1.0);
        this.droneEntity.setStatus(DroneEntity.Status.WORKING);
    }
}
