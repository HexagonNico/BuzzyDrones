package hexagonnico.buzzydrones.common.block;

import hexagonnico.buzzydrones.common.tileentity.IdleStationTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class IdleStationBlock extends Block {

	public IdleStationBlock() {
		super(Properties.of(Material.METAL).strength(3.0f, 4.8f).sound(SoundType.METAL));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new IdleStationTileEntity();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
}
