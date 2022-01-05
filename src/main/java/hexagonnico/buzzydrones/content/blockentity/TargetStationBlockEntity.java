package hexagonnico.buzzydrones.content.blockentity;

import hexagonnico.buzzydrones.content.container.TargetStationContainer;
import hexagonnico.buzzydrones.registry.BuzzyDronesBlockEntities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TargetStationBlockEntity extends AbstractStationBlockEntity implements WorldlyContainer {

	public TargetStationBlockEntity(BlockPos pos, BlockState state) {
		super(BuzzyDronesBlockEntities.TARGET_STATION.get(), pos, state);
		this.inventory = NonNullList.withSize(6, ItemStack.EMPTY);
	}

	@Override
	public Component getDefaultName() {
		return new TranslatableComponent("container.buzzydrones.target");
	}

	public static void serverTick(Level level, BlockPos blockPos, BlockState state, TargetStationBlockEntity blockEntity) {
		if(blockEntity.droneInStation != null && !level.isClientSide) {
			for(int i = 0; i < 5; i++) {
				ItemStack itemStack = blockEntity.inventory.get(i);
				if(itemStack.isEmpty()) {
					blockEntity.inventory.set(i, new ItemStack(blockEntity.droneInStation.itemCarried.getItem(), 1));
					blockEntity.droneInStation.itemCarried.shrink(1);
					break;
				} else if(itemStack.sameItem(blockEntity.droneInStation.itemCarried) && itemStack.getCount() < itemStack.getMaxStackSize()) {
					itemStack.grow(1);
					blockEntity.droneInStation.itemCarried.shrink(1);
					break;
				}
			}
			if(blockEntity.droneInStation.itemCarried.isEmpty() || !blockEntity.hasRoomFor(blockEntity.droneInStation.itemCarried))
				blockEntity.droneExit();
		}
	}

	public ItemStack getFilter() {
		return this.inventory.get(5);
	}

	public boolean hasFilter() {
		return !this.inventory.get(5).isEmpty();
	}

	public boolean hasRoomFor(ItemStack item) {
		for(int i = 0; i < 5; i++) {
			ItemStack inventoryItem = this.inventory.get(i);
			if(inventoryItem.isEmpty() || (inventoryItem.getCount() < inventoryItem.getMaxStackSize() && inventoryItem.sameItem(item)))
				return true;
		}
		return false;
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
		return new TargetStationContainer(id, playerInventory, this);
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return new int[] {0, 1, 2, 3, 4};
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
		return index != 5;
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		return index != 5;
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return index != 5;
	}

	LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
		if(!this.remove && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(facing == Direction.UP)
				return handlers[0].cast();
			else if(facing == Direction.DOWN)
				return handlers[1].cast();
			else
				return handlers[2].cast();
		}
		return super.getCapability(capability, facing);
	}
}
