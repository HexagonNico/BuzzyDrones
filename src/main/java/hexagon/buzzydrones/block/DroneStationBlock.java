package hexagon.buzzydrones.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import hexagon.buzzydrones.tileentity.DroneStationTileEntity;

public abstract class DroneStationBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public DroneStationBlock() {
        super(Properties.of(Material.METAL).strength(1.2F).sound(SoundType.METAL));
        super.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return this.getTileEntity();
    }

    protected abstract TileEntity getTileEntity();

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!state.is(newState.getBlock())) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if(tileEntity instanceof DroneStationTileEntity) {
                ((DroneStationTileEntity) tileEntity).dropInventory();
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }
}
