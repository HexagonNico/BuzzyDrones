package hexagonnico.buzzydrones.common.tileentity;

import hexagonnico.buzzydrones.registry.BuzzyDronesTileEntities;

import net.minecraft.tileentity.TileEntity;

public class IdleStationTileEntity extends TileEntity {

	public IdleStationTileEntity() {
		super(BuzzyDronesTileEntities.IDLE_STATION.get());
	}
}
