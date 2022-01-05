package hexagonnico.buzzydrones.registry;

import hexagonnico.buzzydrones.BuzzyDrones;
import hexagonnico.buzzydrones.content.container.SourceStationContainer;
import hexagonnico.buzzydrones.content.container.TargetStationContainer;
import hexagonnico.buzzydrones.content.gui.screen.SourceGuiScreen;
import hexagonnico.buzzydrones.content.gui.screen.TargetGuiScreen;

import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;

public class BuzzyDronesContainers {

	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, BuzzyDrones.ID);

	public static void registerGuis() {
		MenuScreens.register(SOURCE_STATION.get(), SourceGuiScreen::new);
		MenuScreens.register(TARGET_STATION.get(), TargetGuiScreen::new);
	}

	public static final RegistryObject<MenuType<SourceStationContainer>> SOURCE_STATION = REGISTER.register("source_station", () -> IForgeContainerType.create(SourceStationContainer::new));
	public static final RegistryObject<MenuType<TargetStationContainer>> TARGET_STATION = REGISTER.register("target_station", () -> IForgeContainerType.create(TargetStationContainer::new));
}
