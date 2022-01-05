package hexagonnico.buzzydrones.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NbtHelper {

	public static void saveSingleItem(CompoundTag nbt, ItemStack itemStack, String key) {
		CompoundTag itemNbt = new CompoundTag();
		if(!itemStack.isEmpty()) itemStack.save(itemNbt);
		itemStack.save(itemNbt);
		nbt.put(key, itemNbt);
	}

	public static ItemStack loadSingleItem(CompoundTag nbt, String key) {
		CompoundTag itemNbt = nbt.getCompound(key);
		ItemStack itemStack = ItemStack.EMPTY;
		if(!itemNbt.isEmpty()) itemStack = ItemStack.of(itemNbt);
		return itemStack;
	}
}
