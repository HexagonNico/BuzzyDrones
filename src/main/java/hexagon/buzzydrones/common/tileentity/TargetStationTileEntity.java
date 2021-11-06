package hexagon.buzzydrones.common.tileentity;

import hexagon.buzzydrones.common.container.TargetStationContainer;
import hexagon.buzzydrones.core.registry.BuzzyDronesTileEntities;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

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
        super.tick();
        if(this.droneEntity != null && this.level != null && !this.level.isClientSide()) {
            for(int i = 0; i < 5; i++) {
                ItemStack itemStack = this.inventory.get(i);
                if(itemStack.isEmpty()) {
                    this.inventory.set(i, new ItemStack(this.droneEntity.getItemCarried(), 1));
                    this.droneEntity.decreaseItemCarriedCount();
                    break;
                } else if(itemStack.getItem().equals(this.droneEntity.getItemCarried()) && itemStack.getCount() < itemStack.getItem().getItemStackLimit(itemStack)) {
                    itemStack.setCount(itemStack.getCount() + 1);
                    this.droneEntity.decreaseItemCarriedCount();
                    break;
                }
            }
            if(!this.droneEntity.isCarryingItems() || !this.hasRoomFor(this.droneEntity.getItemCarried()))
                this.droneExit();
        }
    }

    public Item getFilter() {
        return this.inventory.get(5).getItem();
    }

    public boolean hasFilter() {
        return !this.inventory.get(5).isEmpty();
    }

    public boolean hasRoomFor(Item item) {
        for(int i = 0; i < 5; i++) {
            ItemStack inventoryItem = this.inventory.get(i);
            if(inventoryItem.isEmpty() || (inventoryItem.getCount() < 64 && inventoryItem.getItem().equals(item)))
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
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
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
