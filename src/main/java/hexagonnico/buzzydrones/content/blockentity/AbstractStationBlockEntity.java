package hexagonnico.buzzydrones.content.blockentity;

import hexagonnico.buzzydrones.content.block.AbstractStationBlock;
import hexagonnico.buzzydrones.content.entity.DroneEntity;
import hexagonnico.buzzydrones.registry.BuzzyDronesItems;
import hexagonnico.buzzydrones.utils.NbtHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractStationBlockEntity extends RandomizableContainerBlockEntity {

	protected NonNullList<ItemStack> inventory;
	protected DroneData droneInStation;

	public AbstractStationBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
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
			this.level.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
			droneEntity.discard();
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
					this.level.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
				this.droneInStation = null;
			}
		}
	}

	public void dropInventory() {
		if(this.level != null) {
			Containers.dropContents(this.level, this.getBlockPos(), this);
			if(this.droneInStation != null) {
				this.level.addFreshEntity(new ItemEntity(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY(), this.worldPosition.getZ() + 0.5, this.droneInStation.itemCarried));
				Containers.dropItemStack(this.level, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY(), this.getBlockPos().getZ() + 0.5, new ItemStack(BuzzyDronesItems.DRONE.get(), 1));
			}
		}
	}

	@Override
	public int getContainerSize() {
		return this.inventory.size();
	}

	@Override
	public Component getDefaultName() {
		return new TextComponent("Drone Station");
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
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.droneInStation = new DroneData(nbt.getCompound("Drone"), NbtHelper.loadSingleItem(nbt, "DroneItem"));
		this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, this.inventory);
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		ContainerHelper.saveAllItems(compound, this.inventory);
		if(this.droneInStation != null) {
			compound.put("Drone", this.droneInStation.nbtData);
			NbtHelper.saveSingleItem(compound, this.droneInStation.itemCarried, "DroneItem");
		}
		return compound;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(this.worldPosition, 1, this.getUpdateTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.save(new CompoundTag());
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}

	protected static class DroneData {

		public CompoundTag nbtData;
		public ItemStack itemCarried;

		public DroneData(DroneEntity entity) {
			this.nbtData = new CompoundTag();
			entity.save(this.nbtData);
			this.itemCarried = entity.getItemCarried();
		}

		public DroneData(CompoundTag nbtData, ItemStack item) {
			this.nbtData = nbtData;
			this.itemCarried = item;
		}
	}
}
