package hexagon.buzzydrones.common.block;

import hexagon.buzzydrones.common.tileentity.AbstractStationTileEntity;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractStationBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public AbstractStationBlock() {
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
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if(!level.isClientSide && player instanceof ServerPlayerEntity) {
            TileEntity blockEntity = level.getBlockEntity(pos);
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) blockEntity, packetBuffer -> packetBuffer.writeBlockPos(pos));
            return ActionResultType.CONSUME;
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!state.is(newState.getBlock())) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if(tileEntity instanceof AbstractStationTileEntity) {
                ((AbstractStationTileEntity) tileEntity).dropInventory();
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }
}
