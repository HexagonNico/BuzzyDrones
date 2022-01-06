package hexagonnico.buzzydrones.common.block;

import hexagonnico.buzzydrones.common.tileentity.TargetStationTileEntity;

import net.minecraft.tileentity.TileEntity;

public class TargetStationBlock extends AbstractStationBlock {

	@Override
	protected TileEntity getTileEntity() {
		return new TargetStationTileEntity();
	}
}
