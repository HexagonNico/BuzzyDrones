package hexagon.buzzydrones.common.container;

import hexagon.buzzydrones.common.blockentity.TargetStationBlockEntity;
import hexagon.buzzydrones.core.registry.BuzzyDronesContainers;

import javax.annotation.Nonnull;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@MethodsReturnNonnullByDefault
public class TargetStationContainer extends AbstractContainerMenu {

    private final TargetStationBlockEntity container;

    public TargetStationContainer(int id, Inventory playerInventory, TargetStationBlockEntity container) {
        super(BuzzyDronesContainers.TARGET_STATION.get(), id);
        this.container = container;
        checkContainerSize(container, 6);
        container.startOpen(playerInventory.player);

        for(int i = 0; i < 5; i++) {
            this.addSlot(new Slot(container, i, 26 + i * 18, 20));
        }

        this.addSlot(new Slot(container, 5, 26 + 6 * 18, 20));

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, i * 18 + 51));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        }
    }
    
    public TargetStationContainer(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, (TargetStationBlockEntity) playerInventory.player.level.getBlockEntity(buffer.readBlockPos()));
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(itemStack1, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack1, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void removed(@Nonnull Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
