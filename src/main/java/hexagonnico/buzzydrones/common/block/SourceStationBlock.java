package hexagonnico.buzzydrones.common.block;

import hexagonnico.buzzydrones.common.tileentity.SourceStationTileEntity;

import net.minecraft.tileentity.TileEntity;

public class SourceStationBlock extends AbstractStationBlock {

	@Override
	protected TileEntity getTileEntity() {
		return new SourceStationTileEntity();
	}
}
