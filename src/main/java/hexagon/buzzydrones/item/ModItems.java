package hexagon.buzzydrones.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import hexagon.buzzydrones.BuzzyDrones;
import hexagon.buzzydrones.entity.DroneEntity;

@Mod.EventBusSubscriber(modid = BuzzyDrones.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static final Item DRONE_ITEM = new DroneItem(DroneEntity.BASIC);
    public static final Item DRONE_PICKUP_ITEM = new DroneItem(DroneEntity.PICK_UP);

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> register) {
        register.getRegistry().registerAll(
                DRONE_ITEM.setRegistryName("drone"),
                DRONE_PICKUP_ITEM.setRegistryName("drone_pick_up")
        );
    }
}
