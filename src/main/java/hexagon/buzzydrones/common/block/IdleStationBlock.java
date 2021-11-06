package hexagon.buzzydrones.common.block;

import hexagon.buzzydrones.common.tileentity.IdleStationTileEntity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tileentity.TileEntity;

@MethodsReturnNonnullByDefault
public class IdleStationBlock extends AbstractStationBlock {

    @Override
    protected TileEntity getTileEntity() {
        return new IdleStationTileEntity();
    }
}
