package hexagon.buzzydrones.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import hexagon.buzzydrones.entity.DroneEntity;
import hexagon.buzzydrones.tileentity.DroneSourceTileEntity;

public class EnterSourceGoal extends Goal {

    private final DroneEntity droneEntity;

    public EnterSourceGoal(DroneEntity droneEntity) {
        this.droneEntity = droneEntity;
    }

    @Override
    public boolean canUse() {
        return this.droneEntity.getNavigation().isInProgress() && !this.droneEntity.isCarryingItems();
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        BlockPos pos = this.droneEntity.getNavigation().getTargetPos();
        TileEntity sourceTileEntity = this.droneEntity.level.getBlockEntity(pos);
        if(sourceTileEntity instanceof DroneSourceTileEntity) {
            if(this.sourceIsWithinDistance(pos)) {
                this.droneEntity.getNavigation().stop();
                ((DroneSourceTileEntity) sourceTileEntity).droneEnter(this.droneEntity);
            }
        }
    }

    private boolean sourceIsWithinDistance(BlockPos pos) {
        return pos.closerThan(this.droneEntity.position(), 1.5);
    }
}
