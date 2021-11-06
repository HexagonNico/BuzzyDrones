package hexagon.buzzydrones.common.tileentity;

import hexagon.buzzydrones.common.block.AbstractStationBlock;
import hexagon.buzzydrones.common.entity.DroneEntity;
import hexagon.buzzydrones.core.registry.BuzzyDronesItems;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
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

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class AbstractStationTileEntity extends LockableLootTileEntity implements ITickableTileEntity {

    protected NonNullList<ItemStack> inventory;
    protected DroneEntity droneEntity;

    private CompoundNBT droneNbtFix;

    public AbstractStationTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
        this.droneEntity = null;
        this.droneNbtFix = new CompoundNBT();
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
            this.level.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
            droneEntity.remove();
        }
    }

    public void droneExit() {
        if(this.level != null) {
            BlockPos pos = this.getBlockPos().relative(this.getBlockState().getValue(AbstractStationBlock.FACING));
            if(this.level.getBlockState(pos).isAir()) {
                DroneEntity newDrone = new DroneEntity(this.droneEntity);
                newDrone.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                this.level.addFreshEntity(newDrone);
                this.level.playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.BEEHIVE_EXIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                this.droneEntity = null;
                this.droneNbtFix = new CompoundNBT();
            }
        }
    }

    public void dropInventory() {
        if(this.level != null) {
            InventoryHelper.dropContents(this.level, this.getBlockPos(), this);
            if(this.droneEntity != null) {
                this.droneEntity.dropItemCarried(this.getBlockPos());
                InventoryHelper.dropItemStack(this.level, this.getBlockPos().getX() + 0.5, this.getBlockPos().getY(), this.getBlockPos().getZ() + 0.5, new ItemStack(BuzzyDronesItems.DRONE.get(), 1));
            }
        }
    }

    @Override
    public void tick() {
        if(this.level != null && this.droneEntity == null && !this.droneNbtFix.isEmpty()) {
            this.droneEntity = new DroneEntity(this.level, 0);
            this.droneEntity.readAdditionalSaveData(this.droneNbtFix);
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
        this.droneNbtFix = nbt.getCompound("Drone");
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.inventory);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        ItemStackHelper.saveAllItems(compound, this.inventory);
        if(this.droneEntity != null) this.droneEntity.writeInterestingData(this.droneNbtFix);
        compound.put("Drone", this.droneNbtFix);
        return compound;
    }
    
    @Nullable
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
}
