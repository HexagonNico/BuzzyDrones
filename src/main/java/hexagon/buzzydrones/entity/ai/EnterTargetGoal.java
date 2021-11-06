package hexagon.buzzydrones.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import hexagon.buzzydrones.entity.DroneEntity;
import hexagon.buzzydrones.tileentity.DroneTargetTileEntity;

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
        if(targetTileEntity instanceof DroneTargetTileEntity) {
            if(this.targetIsWithinDistance(pos)) {
                this.droneEntity.getNavigation().stop();
                ((DroneTargetTileEntity) targetTileEntity).droneEnter(this.droneEntity);
            }
        }
    }

    private boolean targetIsWithinDistance(BlockPos pos) {
        return pos.closerThan(this.droneEntity.position(), 1.5);
    }
}
