package hexagonnico.buzzydrones.registry;

import hexagonnico.buzzydrones.BuzzyDrones;
import hexagonnico.buzzydrones.common.entity.DroneEntity;
import hexagonnico.buzzydrones.common.item.DroneItem;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BuzzyDronesItems {

	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, BuzzyDrones.ID);

	public static final RegistryObject<DroneItem> DRONE = REGISTER.register("drone_basic", () -> new DroneItem(DroneEntity.BASIC));
	public static final RegistryObject<DroneItem> DRONE_PICK_UP = REGISTER.register("drone_pick_up", () -> new DroneItem(DroneEntity.PICK_UP));

	public static final RegistryObject<BlockItem> SOURCE_STATION = REGISTER.register("source_station", () -> new BlockItem(BuzzyDronesBlocks.SOURCE_STATION.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<BlockItem> TARGET_STATION = REGISTER.register("target_station", () -> new BlockItem(BuzzyDronesBlocks.TARGET_STATION.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));
	public static final RegistryObject<BlockItem> IDLE_STATION = REGISTER.register("idle_station", () -> new BlockItem(BuzzyDronesBlocks.IDLE_STATION.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));
}
