package hexagon.buzzydrones.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class NbtHelper {

    public static void saveSingleItem(CompoundNBT nbt, ItemStack itemStack, String key) {
        CompoundNBT itemNbt = new CompoundNBT();
        if(!itemStack.isEmpty()) itemStack.save(itemNbt);
        itemStack.save(itemNbt);
        nbt.put(key, itemNbt);
    }

    public static ItemStack loadSingleItem(CompoundNBT nbt, String key) {
        CompoundNBT itemNbt = nbt.getCompound(key);
        ItemStack itemStack = ItemStack.EMPTY;
        if(!itemNbt.isEmpty()) itemStack = ItemStack.of(itemNbt);
        return itemStack;
    }
}
