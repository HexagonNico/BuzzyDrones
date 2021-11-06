package hexagon.buzzydrones.common.tileentity;

import hexagon.buzzydrones.common.container.SourceStationContainer;
import hexagon.buzzydrones.core.registry.BuzzyDronesTileEntities;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

@MethodsReturnNonnullByDefault
public class SourceStationTileEntity extends AbstractStationTileEntity {

    public SourceStationTileEntity() {
        super(BuzzyDronesTileEntities.SOURCE_STATION.get());
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
        return new SourceStationContainer(id, player, this);
    }
}
