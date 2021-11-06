package hexagon.buzzydrones.common.block;

import hexagon.buzzydrones.common.tileentity.SourceStationTileEntity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tileentity.TileEntity;

@MethodsReturnNonnullByDefault
public class SourceStationBlock extends AbstractStationBlock {

    @Override
    protected TileEntity getTileEntity() {
        return new SourceStationTileEntity();
    }
}
