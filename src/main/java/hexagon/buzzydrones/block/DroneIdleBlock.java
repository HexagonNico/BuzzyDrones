package hexagon.buzzydrones.block;

import net.minecraft.tileentity.TileEntity;
import hexagon.buzzydrones.tileentity.DroneIdleTileEntity;

public class DroneIdleBlock extends DroneStationBlock {

    @Override
    protected TileEntity getTileEntity() {
        return new DroneIdleTileEntity();
    }
}
