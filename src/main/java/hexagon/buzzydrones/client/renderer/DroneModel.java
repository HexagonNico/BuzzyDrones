package hexagon.buzzydrones.client.renderer;

import hexagon.buzzydrones.common.entity.DroneEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DroneModel<T extends DroneEntity> extends EntityModel<T> {

    private final ModelPart model;
    
    public DroneModel() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0f, 19.0f, 0.0f));
        PartDefinition partdefinition2 = partdefinition1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5f, -4.0f, -5.0f, 7.0f, 7.0f, 10.0f), PartPose.ZERO);
        partdefinition2.addOrReplaceChild("left_antenna", CubeListBuilder.create().texOffs(2, 0).addBox(1.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f), PartPose.offset(0.0f, -2.0f, -5.0f));
        partdefinition2.addOrReplaceChild("right_antenna", CubeListBuilder.create().texOffs(2, 3).addBox(-2.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f), PartPose.offset(0.0f, -2.0f, -5.0f));
        partdefinition1.addOrReplaceChild("front_legs", CubeListBuilder.create().addBox("front_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 1), PartPose.offset(1.5f, 3.0f, -2.0f));
        partdefinition1.addOrReplaceChild("middle_legs", CubeListBuilder.create().addBox("middle_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 3), PartPose.offset(1.5f, 3.0f, 0.0f));
        partdefinition1.addOrReplaceChild("back_legs", CubeListBuilder.create().addBox("back_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 5), PartPose.offset(1.5f, 3.0f, 2.0f));
        this.model = LayerDefinition.create(meshdefinition, 64, 64).bakeRoot();
//        this.model = model;
//        this.model.setPos(0.0f, 19.0f, 0.0f);
//        ModelPart torso = new ModelPart(this, 0, 0);
//        torso.setPos(0.0f, 0.0f, 0.0f);
//        this.model.addChild(torso);
//        torso.addBox(-3.5f, -4.0f, -5.0f, 7.0f, 7.0f, 10.0f, 0.0f);
//        ModelPart leftAntenna = new ModelPart(this, 2, 0);
//        leftAntenna.setPos(0.0f, -2.0f, -5.0f);
//        leftAntenna.addBox(1.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f, 0.0f);
//        ModelPart rightAntenna = new ModelPart(this, 2, 3);
//        rightAntenna.setPos(0.0f, -2.0f, -5.0f);
//        rightAntenna.addBox(-2.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f, 0.0f);
//        torso.addChild(leftAntenna);
//        torso.addChild(rightAntenna);
//        ModelPart frontLegs = new ModelPart(this);
//        frontLegs.setPos(1.5f, 3.0f, -2.0f);
//        this.model.addChild(frontLegs);
//        frontLegs.addBox("frontLegBox", -5.0f, 0.0f, 0.0f, 7, 2, 0, 0.0f, 26, 1);
//        ModelPart middleLegs = new ModelPart(this);
//        middleLegs.setPos(1.5f, 3.0f, 0.0f);
//        this.model.addChild(middleLegs);
//        middleLegs.addBox("midLegBox", -5.0f, 0.0f, 0.0f, 7, 2, 0, 0.0f, 26, 3);
//        ModelPart backLegs = new ModelPart(null, new HashMap<>());
//        backLegs.setPos(1.5f, 3.0f, 2.0f);
//        backLegs.addBox("backLegBox", -5.0f, 0.0f, 0.0f, 7, 2, 0, 0.0f, 26, 5);
    }
    
    @Override
    public void setupAnim(T entity, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {
    
    }
    
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.model.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
