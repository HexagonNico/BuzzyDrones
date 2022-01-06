package hexagonnico.buzzydrones.common.tileentity;

import hexagonnico.buzzydrones.common.container.SourceStationContainer;
import hexagonnico.buzzydrones.registry.BuzzyDronesTileEntities;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
		if(this.droneInStation != null) {
			for(ItemStack itemStack : this.inventory) {
				if(!itemStack.isEmpty() && this.droneInStation.itemCarried.getCount() < 64) {
					if(this.droneInStation.itemCarried.isEmpty()) {
						this.droneInStation.itemCarried = new ItemStack(itemStack.getItem(), 1);
						itemStack.shrink(1);
						break;
					} else if(this.droneInStation.itemCarried.sameItem(itemStack)) {
						this.droneInStation.itemCarried.grow(1);
						itemStack.shrink(1);
						break;
					}
				}
			}
			if(this.droneCanExit()) this.droneExit();
		}
	}

	private boolean droneCanExit() {
		if(this.droneInStation.itemCarried.getCount() < this.droneInStation.itemCarried.getMaxStackSize()) {
			for(ItemStack itemStack : this.inventory) {
				if(itemStack.sameItem(this.droneInStation.itemCarried))
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
