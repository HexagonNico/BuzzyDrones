package hexagon.buzzydrones.client.renderer;

import hexagon.buzzydrones.common.entity.DroneEntity;
import hexagon.buzzydrones.core.BuzzyDrones;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DroneRenderer extends MobRenderer<DroneEntity, DroneModel<DroneEntity>> {

    private static final ResourceLocation droneBlueTexture = new ResourceLocation(BuzzyDrones.ID, "textures/entity/drone_blue.png");
    private static final ResourceLocation droneGreenTexture = new ResourceLocation(BuzzyDrones.ID, "textures/entity/drone_green.png");
    private static final ResourceLocation droneOrangeTexture = new ResourceLocation(BuzzyDrones.ID, "textures/entity/drone_orange.png");
    private static final ResourceLocation droneRedTexture = new ResourceLocation(BuzzyDrones.ID, "textures/entity/drone_red.png");

    public DroneRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DroneModel<>(), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(DroneEntity entity) {
        switch(entity.getStatus()) {
            case WORKING:
                return droneBlueTexture;
            case IDLE:
                return droneGreenTexture;
            case WARNING:
                return droneOrangeTexture;
            case ERROR:
            default:
                return droneRedTexture;
        }
    }
}
