package hexagon.buzzydrones;

import hexagon.buzzydrones.container.ModContainers;
import hexagon.buzzydrones.entity.ModEntities;

public class Setup {

    public static void setup() {
        ModEntities.registerAttributes();
    }

    public static void clientSetup() {
        ModEntities.registerRenders();
        ModContainers.registerGui();
    }
}
