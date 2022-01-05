package hexagonnico.buzzydrones.content.blockentity;

import hexagonnico.buzzydrones.content.container.SourceStationContainer;
import hexagonnico.buzzydrones.content.entity.DroneEntity;
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
