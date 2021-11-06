package hexagon.buzzydrones.common.block;

import hexagon.buzzydrones.common.tileentity.SourceStationTileEntity;

import net.minecraft.tileentity.TileEntity;

public class SourceStationBlock extends AbstractStationBlock {

    @Override
    protected TileEntity getTileEntity() {
        return new SourceStationTileEntity();
    }
}
