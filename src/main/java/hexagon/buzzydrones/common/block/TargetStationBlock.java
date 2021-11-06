package hexagon.buzzydrones.common.block;

import hexagon.buzzydrones.common.container.TargetStationContainer;
import hexagon.buzzydrones.common.tileentity.AbstractStationTileEntity;
import hexagon.buzzydrones.common.tileentity.TargetStationTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class TargetStationBlock extends AbstractStationBlock {

    @Override
    protected TileEntity getTileEntity() {
        return new TargetStationTileEntity();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!world.isClientSide && player instanceof ServerPlayerEntity) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return ((AbstractStationTileEntity) tileEntity).getDefaultName();
                }

                @Override
                public Container createMenu(int i, PlayerInventory inventory, PlayerEntity p_createMenu_3_) {
                    return new TargetStationContainer(i, inventory, (TargetStationTileEntity) tileEntity);
                }
            }, packetBuffer -> packetBuffer.writeBlockPos(pos));
            return ActionResultType.CONSUME;
        }
        return ActionResultType.SUCCESS;
    }
}
