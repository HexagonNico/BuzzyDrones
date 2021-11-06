package hexagon.buzzydrones.common.container;

import hexagon.buzzydrones.common.tileentity.TargetStationTileEntity;
import hexagon.buzzydrones.core.registry.BuzzyDronesContainers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class TargetStationContainer extends Container {

    private final IInventory inventory;

    public TargetStationContainer(int id, PlayerInventory playerInventory, IInventory containerInventory) {
        super(BuzzyDronesContainers.TARGET_STATION.get(), id);
        this.inventory = containerInventory;
        checkContainerSize(containerInventory, 6);
        containerInventory.startOpen(playerInventory.player);

        for(int i = 0; i < 5; i++) {
            this.addSlot(new Slot(containerInventory, i, 26 + i * 18, 20));
        }

        this.addSlot(new Slot(containerInventory, 5, 26 + 6 * 18, 20));

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, i * 18 + 51));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        }
    }
    
    public TargetStationContainer(int windowId, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(windowId, playerInventory, (TargetStationTileEntity) playerInventory.player.level.getBlockEntity(buffer.readBlockPos()));
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.inventory.stillValid(player);
    }

    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void removed(PlayerEntity playerIn) {
        super.removed(playerIn);
        this.inventory.stopOpen(playerIn);
    }
}