package hexagon.buzzydrones.common.entity.ai;

import hexagon.buzzydrones.common.entity.DroneEntity;
import hexagon.buzzydrones.common.tileentity.SourceStationTileEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

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
        if(sourceTileEntity instanceof SourceStationTileEntity) {
            if(this.sourceIsWithinDistance(pos)) {
                this.droneEntity.getNavigation().stop();
                ((SourceStationTileEntity) sourceTileEntity).droneEnter(this.droneEntity);
            }
        }
    }

    private boolean sourceIsWithinDistance(BlockPos pos) {
        return pos.closerThan(this.droneEntity.position(), 1.5);
    }
}
