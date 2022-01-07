package hexagonnico.buzzydrones.content.block;

import hexagonnico.buzzydrones.content.tileentity.SourceStationTileEntity;

import net.minecraft.tileentity.TileEntity;

public class SourceStationBlock extends AbstractStationBlock {

	@Override
	protected TileEntity getTileEntity() {
		return new SourceStationTileEntity();
	}
}
