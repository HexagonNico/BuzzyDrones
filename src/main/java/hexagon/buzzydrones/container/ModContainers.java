package hexagon.buzzydrones.container;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
import hexagon.buzzydrones.BuzzyDrones;
import hexagon.buzzydrones.client.gui.screen.SourceGuiScreen;
import hexagon.buzzydrones.client.gui.screen.TargetGuiScreen;
import hexagon.buzzydrones.tileentity.DroneSourceTileEntity;
import hexagon.buzzydrones.tileentity.DroneTargetTileEntity;

@Mod.EventBusSubscriber(modid = BuzzyDrones.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers {

    public static final ContainerType<DroneSourceContainer> DRONE_SOURCE = new ContainerType<>((IContainerFactory<DroneSourceContainer>) (window, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        TileEntity tileEntity = inv.player.level.getBlockEntity(pos);
        return new DroneSourceContainer(window, inv, (DroneSourceTileEntity) tileEntity);
    });

    public static final ContainerType<DroneTargetContainer> DRONE_TARGET = new ContainerType<>((IContainerFactory<DroneTargetContainer>) (window, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        TileEntity tileEntity = inv.player.level.getBlockEntity(pos);
        return new DroneTargetContainer(window, inv, (DroneTargetTileEntity) tileEntity);
    });

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> register) {
        register.getRegistry().registerAll(
                DRONE_SOURCE.setRegistryName("drone_source"),
                DRONE_TARGET.setRegistryName("drone_target")
        );
    }

    public static void registerGui() {
        ScreenManager.register(ModContainers.DRONE_SOURCE, SourceGuiScreen::new);
        ScreenManager.register(ModContainers.DRONE_TARGET, TargetGuiScreen::new);
    }
}
