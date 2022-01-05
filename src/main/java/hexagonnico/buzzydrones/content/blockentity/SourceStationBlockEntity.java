package hexagonnico.buzzydrones.content.blockentity;

import hexagonnico.buzzydrones.content.container.SourceStationContainer;
import hexagonnico.buzzydrones.registry.BuzzyDronesBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SourceStationBlockEntity extends AbstractStationBlockEntity {

	public SourceStationBlockEntity(BlockPos pos, BlockState state) {
		super(BuzzyDronesBlockEntities.SOURCE_STATION.get(), pos, state);
	}

	@Override
	public Component getDefaultName() {
		return new TranslatableComponent("container.buzzydrones.source");
	}

	public static void serverTick(Level level, BlockPos blockPos, BlockState state, SourceStationBlockEntity blockEntity) {
		if(blockEntity.droneInStation != null) {
			for(ItemStack itemStack : blockEntity.inventory) {
				if(!itemStack.isEmpty() && blockEntity.droneInStation.itemCarried.getCount() < 64) {
					if(blockEntity.droneInStation.itemCarried.isEmpty()) {
						blockEntity.droneInStation.itemCarried = new ItemStack(itemStack.getItem(), 1);
						itemStack.shrink(1);
						break;
					} else if(blockEntity.droneInStation.itemCarried.sameItem(itemStack)) {
						blockEntity.droneInStation.itemCarried.grow(1);
						itemStack.shrink(1);
						break;
					}
				}
			}
			if(blockEntity.droneCanExit()) blockEntity.droneExit();
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
	protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
		return new SourceStationContainer(id, playerInventory, this);
	}
}
