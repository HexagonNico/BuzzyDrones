package hexagon.buzzydrones.common.block;

import hexagon.buzzydrones.common.tileentity.IdleStationTileEntity;

import net.minecraft.tileentity.TileEntity;

public class IdleStationBlock extends AbstractStationBlock {

    @Override
    protected TileEntity getTileEntity() {
        return new IdleStationTileEntity();
    }
}
