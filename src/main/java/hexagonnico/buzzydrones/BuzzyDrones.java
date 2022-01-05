package hexagonnico.buzzydrones;

import hexagonnico.buzzydrones.registry.BuzzyDronesBlockEntities;
import hexagonnico.buzzydrones.registry.BuzzyDronesBlocks;
import hexagonnico.buzzydrones.registry.BuzzyDronesContainers;
import hexagonnico.buzzydrones.registry.BuzzyDronesEntities;
import hexagonnico.buzzydrones.registry.BuzzyDronesItems;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BuzzyDrones.ID)
public class BuzzyDrones {

	public static final String ID = "buzzydrones";

	public BuzzyDrones() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::commonSetup);
		eventBus.addListener(this::clientSetup);

		BuzzyDronesBlocks.REGISTER.register(eventBus);
		BuzzyDronesItems.REGISTER.register(eventBus);
		BuzzyDronesBlockEntities.REGISTER.register(eventBus);
		BuzzyDronesContainers.REGISTER.register(eventBus);
		BuzzyDronesEntities.REGISTER.register(eventBus);
		
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {

	}

	private void clientSetup(final FMLClientSetupEvent event) {
		BuzzyDronesContainers.registerGuis();
	}
}
