package hexagon.buzzydrones.tileentity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import hexagon.buzzydrones.container.DroneSourceContainer;

public class DroneSourceTileEntity extends DroneStationTileEntity {

    public DroneSourceTileEntity() {
        super(ModTileEntities.DRONE_SOURCE);
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.buzzydrones.source");
    }

    @Override
    public void tick() {
        super.tick();
        if(this.droneEntity != null && this.level != null && !this.level.isClientSide()) {
            for(ItemStack itemStack : this.inventory) {
                if(this.droneEntity.pickUpItems(itemStack))
                    break;
            }
            if(this.droneCanExit()) this.droneExit();
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
    protected Container createMenu(int id, PlayerInventory player) {
        return new DroneSourceContainer(id, player, this);
    }
}
