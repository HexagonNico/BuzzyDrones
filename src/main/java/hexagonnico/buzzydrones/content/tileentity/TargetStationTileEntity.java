package hexagonnico.buzzydrones.content.tileentity;

import hexagonnico.buzzydrones.content.container.TargetStationContainer;
import hexagonnico.buzzydrones.registry.BuzzyDronesTileEntities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TargetStationTileEntity extends AbstractStationTileEntity implements ISidedInventory {

	public TargetStationTileEntity() {
		super(BuzzyDronesTileEntities.TARGET_STATION.get());
		this.inventory = NonNullList.withSize(6, ItemStack.EMPTY);
	}

	@Override
	public ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.buzzydrones.target");
	}

	@Override
	public void tick() {
		if(this.droneInStation != null && !level.isClientSide) {
			for(int i = 0; i < 5; i++) {
				ItemStack itemStack = this.inventory.get(i);
				if(itemStack.isEmpty()) {
					this.inventory.set(i, new ItemStack(this.droneInStation.itemCarried.getItem(), 1));
					this.droneInStation.itemCarried.shrink(1);
					break;
				} else if(itemStack.sameItem(this.droneInStation.itemCarried) && itemStack.getCount() < itemStack.getMaxStackSize()) {
					itemStack.grow(1);
					this.droneInStation.itemCarried.shrink(1);
					break;
				}
			}
			if(this.droneInStation.itemCarried.isEmpty() || !this.hasRoomFor(this.droneInStation.itemCarried))
				this.droneExit();
		}
	}

	public Item getFilter() {
		return this.inventory.get(5).getItem();
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
	protected Container createMenu(int id, PlayerInventory player) {
		return new TargetStationContainer(id, player, this);
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
