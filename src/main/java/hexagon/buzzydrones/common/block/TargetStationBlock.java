package hexagon.buzzydrones.common.block;

import hexagon.buzzydrones.common.tileentity.TargetStationTileEntity;

import net.minecraft.tileentity.TileEntity;

public class TargetStationBlock extends AbstractStationBlock {

    @Override
    protected TileEntity getTileEntity() {
        return new TargetStationTileEntity();
    }
}
