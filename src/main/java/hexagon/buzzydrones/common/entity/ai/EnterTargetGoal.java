package hexagon.buzzydrones.common.entity.ai;

import hexagon.buzzydrones.common.entity.DroneEntity;
import hexagon.buzzydrones.common.tileentity.TargetStationTileEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class EnterTargetGoal extends Goal {

    private final DroneEntity droneEntity;

    public EnterTargetGoal(DroneEntity droneEntity) {
        this.droneEntity = droneEntity;
    }

    @Override
    public boolean canUse() {
        return this.droneEntity.getNavigation().isInProgress() && this.droneEntity.isCarryingItems();
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        BlockPos pos = this.droneEntity.getNavigation().getTargetPos();
        TileEntity targetTileEntity = this.droneEntity.level.getBlockEntity(pos);
        if(targetTileEntity instanceof TargetStationTileEntity) {
            if(this.targetIsWithinDistance(pos)) {
                this.droneEntity.getNavigation().stop();
                ((TargetStationTileEntity) targetTileEntity).droneEnter(this.droneEntity);
            }
        }
    }

    private boolean targetIsWithinDistance(BlockPos pos) {
        return pos.closerThan(this.droneEntity.position(), 1.5);
    }
}
