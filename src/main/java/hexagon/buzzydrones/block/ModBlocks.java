package hexagon.buzzydrones.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import hexagon.buzzydrones.BuzzyDrones;
import hexagon.buzzydrones.item.ModItemGroup;

@Mod.EventBusSubscriber(modid = BuzzyDrones.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    public static final DroneSourceBlock DRONE_SOURCE = new DroneSourceBlock();
    public static final DroneTargetBlock DRONE_TARGET = new DroneTargetBlock();
    public static final DroneIdleBlock DRONE_IDLE = new DroneIdleBlock();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> register) {
        register.getRegistry().registerAll(
                DRONE_SOURCE.setRegistryName("drone_source"),
                DRONE_TARGET.setRegistryName("drone_target"),
                DRONE_IDLE.setRegistryName("drone_idle")
        );
    }

    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> register) {
        register.getRegistry().registerAll(
                new BlockItem(DRONE_SOURCE, new Item.Properties().tab(ModItemGroup.INSTANCE)).setRegistryName("drone_source"),
                new BlockItem(DRONE_TARGET, new Item.Properties().tab(ModItemGroup.INSTANCE)).setRegistryName("drone_target"),
                new BlockItem(DRONE_IDLE, new Item.Properties().tab(ModItemGroup.INSTANCE)).setRegistryName("drone_idle")
        );
    }
}
