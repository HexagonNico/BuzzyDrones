package hexagon.buzzydrones.common.blockentity;

import hexagon.buzzydrones.common.container.SourceStationContainer;
import hexagon.buzzydrones.common.entity.DroneEntity;
import hexagon.buzzydrones.core.registry.BuzzyDronesTileEntities;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SourceStationTileEntity extends AbstractStationTileEntity {

    public SourceStationTileEntity(BlockPos pos, BlockState state) {
        super(BuzzyDronesTileEntities.SOURCE_STATION.get(), pos, state);
    }

    @Override
    public Component getDefaultName() {
        return new TranslatableComponent("container.buzzydrones.source");
    }
    
    public static void serverTick(Level level, BlockPos blockPos, BlockState state, SourceStationTileEntity blockEntity) {
        if(blockEntity.droneEntity == null && !blockEntity.droneNbtFix.isEmpty()) {
            blockEntity.droneEntity = new DroneEntity(level, 0);
            blockEntity.droneEntity.readAdditionalSaveData(blockEntity.droneNbtFix);
        }
        if(blockEntity.droneEntity != null) {
            for(ItemStack itemStack : blockEntity.inventory) {
                if(blockEntity.droneEntity.pickUpItems(itemStack))
                    break;
            }
            if(blockEntity.droneCanExit()) blockEntity.droneExit();
        }
    }

    private boolean droneCanExit() {
        if(!this.droneEntity.isFull()) {
            for(ItemStack itemStack : this.inventory) {
                if(itemStack.getItem().equals(this.droneEntity.getItemCarried()))
                    return false;
            }
        }
        return true;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new SourceStationContainer(id, playerInventory, this);
    }
}
