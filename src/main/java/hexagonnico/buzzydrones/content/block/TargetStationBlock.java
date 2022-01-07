package hexagonnico.buzzydrones.content.block;

import hexagonnico.buzzydrones.content.tileentity.TargetStationTileEntity;

import net.minecraft.tileentity.TileEntity;

public class TargetStationBlock extends AbstractStationBlock {

	@Override
	protected TileEntity getTileEntity() {
		return new TargetStationTileEntity();
	}
}
