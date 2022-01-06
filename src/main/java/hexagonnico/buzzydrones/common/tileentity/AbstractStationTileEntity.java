package hexagonnico.buzzydrones.common.tileentity;

import hexagonnico.buzzydrones.common.block.AbstractStationBlock;
import hexagonnico.buzzydrones.common.entity.DroneEntity;
import hexagonnico.buzzydrones.registry.BuzzyDronesItems;
import hexagonnico.buzzydrones.utils.NbtHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public abstract class AbstractStationTileEntity extends LockableLootTileEntity implements ITickableTileEntity {

	protected NonNullList<ItemStack> inventory;
	protected DroneData droneInStation;

	public AbstractStationTileEntity(TileEntityType<?> tileEntityType) {
		super(tileEntityType);
		this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.inventory;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		this.inventory = items;
	}

	public boolean isFree() {
		return this.droneInStation == null;
	}

	public void droneEnter(DroneEntity droneEntity) {
		if(this.isFree() && this.level != null) {
			droneEntity.stopRiding();
			droneEntity.ejectPassengers();
			this.droneInStation = new DroneData(droneEntity);
			this.level.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
			droneEntity.remove();
		}
	}

	public void droneExit() {
		if(this.level != null) {
			BlockPos pos = this.getBlockPos().relative(this.getBlockState().getValue(AbstractStationBlock.FACING));
			if(this.level.getBlockState(pos).isAir()) {
				DroneEntity droneEntity = (DroneEntity) EntityType.loadEntityRecursive(this.droneInStation.nbtData, this.level, (entity) -> entity);
				if(droneEntity != null) {
					droneEntity.setItemCarried(this.droneInStation.itemCarried);
					droneEntity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					this.level.addFreshEntity(droneEntity);
					this.level.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.BEEHIVE_EXIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				this.droneInStation = null;
			}
		}
	}

	public void dropInventory() {
		if(this.level != null) {
			InventoryHelper.dropContents(this.level, this.getBlockPos(), this);
			if(this.droneInStation != null) {
				this.level.addFreshEntity(new ItemEntity(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY(), this.worldPosition.getZ() + 0.5, this.droneInStation.itemCarried));
				InventoryHelper.dropItemStack(this.level, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY(), this.getBlockPos().getZ() + 0.5, new ItemStack(BuzzyDronesItems.DRONE.get(), 1));
			}
		}
	}

	@Override
	public int getContainerSize() {
		return this.inventory.size();
	}

	@Override
	public ITextComponent getDefaultName() {
		return new StringTextComponent("Drone Station");
	}

	public int getFullness() {
		int count = 0;
		for(ItemStack itemStack : this.inventory) {
			count += itemStack.getCount();
		}
		return count;
	}

	public double getDistance(BlockPos pos) {
		return this.getBlockPos().distSqr(pos);
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		if(nbt.contains("Drone"))
			this.droneInStation = new DroneData(nbt.getCompound("Drone"), NbtHelper.loadSingleItem(nbt, "DroneItem"));
		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, this.inventory);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		ItemStackHelper.saveAllItems(compound, this.inventory);
		if(this.droneInStation != null) {
			compound.put("Drone", this.droneInStation.nbtData);
			NbtHelper.saveSingleItem(compound, this.droneInStation.itemCarried, "DroneItem");
		}
		return compound;
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.save(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(this.getBlockState(), pkt.getTag());
	}

	protected static class DroneData {

		public CompoundNBT nbtData;
		public ItemStack itemCarried;

		public DroneData(DroneEntity entity) {
			this.nbtData = new CompoundNBT();
			entity.save(this.nbtData);
			this.itemCarried = entity.getItemCarried();
		}

		public DroneData(CompoundNBT nbtData, ItemStack item) {
			this.nbtData = nbtData;
			this.itemCarried = item;
		}
	}
}
