package hexagon.buzzydrones.common.blockentity;

import hexagon.buzzydrones.common.block.AbstractStationBlock;
import hexagon.buzzydrones.common.entity.DroneEntity;
import hexagon.buzzydrones.core.registry.BuzzyDronesItems;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class AbstractStationTileEntity extends RandomizableContainerBlockEntity implements Container {

    protected NonNullList<ItemStack> inventory;
    protected DroneEntity droneEntity;

    protected CompoundTag droneNbtFix;

    public AbstractStationTileEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
        this.droneEntity = null;
        this.droneNbtFix = new CompoundTag();
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
        return this.droneEntity == null;
    }

    public void droneEnter(DroneEntity droneEntity) {
        if(this.isFree() && this.level != null) {
            this.droneEntity = droneEntity;
            this.level.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
            droneEntity.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    public void droneExit() {
        if(this.level != null) {
            BlockPos pos = this.getBlockPos().relative(this.getBlockState().getValue(AbstractStationBlock.FACING));
            if(this.level.getBlockState(pos).isAir()) {
                DroneEntity newDrone = new DroneEntity(this.droneEntity);
                newDrone.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                this.level.addFreshEntity(newDrone);
                this.level.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.droneEntity = null;
                this.droneNbtFix = new CompoundTag();
            }
        }
    }

    public void dropInventory() {
        if(this.level != null) {
            Containers.dropContents(this.level, this.getBlockPos(), this);
            if(this.droneEntity != null) {
                this.droneEntity.dropItemCarried(this.getBlockPos());
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
        this.droneNbtFix = nbt.getCompound("Drone");
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        ContainerHelper.saveAllItems(compound, this.inventory);
        if(this.droneEntity != null) this.droneEntity.writeInterestingData(this.droneNbtFix);
        compound.put("Drone", this.droneNbtFix);
        return compound;
    }
    
    @Nullable
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
}
