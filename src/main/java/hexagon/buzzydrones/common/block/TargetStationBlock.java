package hexagon.buzzydrones.common.block;

import hexagon.buzzydrones.common.tileentity.TargetStationTileEntity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tileentity.TileEntity;

@MethodsReturnNonnullByDefault
public class TargetStationBlock extends AbstractStationBlock {

    @Override
    protected TileEntity getTileEntity() {
        return new TargetStationTileEntity();
    }
}
