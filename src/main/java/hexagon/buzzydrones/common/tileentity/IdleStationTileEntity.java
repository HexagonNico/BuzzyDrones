package hexagon.buzzydrones.common.tileentity;

import hexagon.buzzydrones.core.registry.BuzzyDronesTileEntities;

import net.minecraft.tileentity.TileEntity;

public class IdleStationTileEntity extends TileEntity {

    public IdleStationTileEntity() {
        super(BuzzyDronesTileEntities.IDLE_STATION.get());
    }
}
