package hexagon.buzzydrones.core.registry;

import hexagon.buzzydrones.client.gui.screen.SourceGuiScreen;
import hexagon.buzzydrones.client.gui.screen.TargetGuiScreen;
import hexagon.buzzydrones.common.container.SourceStationContainer;
import hexagon.buzzydrones.common.container.TargetStationContainer;
import hexagon.buzzydrones.core.BuzzyDrones;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BuzzyDronesContainers {
    
    public static final DeferredRegister<ContainerType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, BuzzyDrones.ID);
    
    public static void registerGuis() {
        ScreenManager.register(SOURCE_STATION.get(), SourceGuiScreen::new);
        ScreenManager.register(TARGET_STATION.get(), TargetGuiScreen::new);
    }
    
    public static final RegistryObject<ContainerType<SourceStationContainer>> SOURCE_STATION = REGISTER.register("source_station", () -> IForgeContainerType.create(SourceStationContainer::new));
    public static final RegistryObject<ContainerType<TargetStationContainer>> TARGET_STATION = REGISTER.register("target_station", () -> IForgeContainerType.create(TargetStationContainer::new));
}
