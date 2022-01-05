package hexagonnico.buzzydrones.content.renderer;

import hexagonnico.buzzydrones.content.entity.DroneEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@OnlyIn(Dist.CLIENT)
public class DroneModel<T extends DroneEntity> extends EntityModel<T> {

	private final ModelPart model;

	public DroneModel() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();
		PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0f, 19.0f, 0.0f));
		PartDefinition partDefinition2 = partDefinition1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5f, -4.0f, -5.0f, 7.0f, 7.0f, 10.0f), PartPose.ZERO);
		partDefinition2.addOrReplaceChild("left_antenna", CubeListBuilder.create().texOffs(2, 0).addBox(1.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f), PartPose.offset(0.0f, -2.0f, -5.0f));
		partDefinition2.addOrReplaceChild("right_antenna", CubeListBuilder.create().texOffs(2, 3).addBox(-2.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f), PartPose.offset(0.0f, -2.0f, -5.0f));
		partDefinition1.addOrReplaceChild("front_legs", CubeListBuilder.create().addBox("front_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 1), PartPose.offset(1.5f, 3.0f, -2.0f));
		partDefinition1.addOrReplaceChild("middle_legs", CubeListBuilder.create().addBox("middle_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 3), PartPose.offset(1.5f, 3.0f, 0.0f));
		partDefinition1.addOrReplaceChild("back_legs", CubeListBuilder.create().addBox("back_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 5), PartPose.offset(1.5f, 3.0f, 2.0f));
		this.model = LayerDefinition.create(meshDefinition, 64, 64).bakeRoot();
	}

	@Override
	public void setupAnim(T entity, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.model.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
