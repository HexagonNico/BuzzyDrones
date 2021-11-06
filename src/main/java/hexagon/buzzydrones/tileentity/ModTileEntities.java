package hexagon.buzzydrones.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import hexagon.buzzydrones.BuzzyDrones;
import hexagon.buzzydrones.block.ModBlocks;

@Mod.EventBusSubscriber(modid = BuzzyDrones.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTileEntities {

    public static final TileEntityType<DroneSourceTileEntity> DRONE_SOURCE = new TileEntityType<>(DroneSourceTileEntity::new, Sets.newHashSet(ModBlocks.DRONE_SOURCE), null);
    public static final TileEntityType<DroneTargetTileEntity> DRONE_TARGET = new TileEntityType<>(DroneTargetTileEntity::new, Sets.newHashSet(ModBlocks.DRONE_TARGET), null);
    public static final TileEntityType<DroneIdleTileEntity> DRONE_IDLE = new TileEntityType<>(DroneIdleTileEntity::new, Sets.newHashSet(ModBlocks.DRONE_IDLE), null);

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> register) {
        register.getRegistry().registerAll(
                DRONE_SOURCE.setRegistryName("drone_source"),
                DRONE_TARGET.setRegistryName("drone_target"),
                DRONE_IDLE.setRegistryName("drone_idle")
        );
    }
}
