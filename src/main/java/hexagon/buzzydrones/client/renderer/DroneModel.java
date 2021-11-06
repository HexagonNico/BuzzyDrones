package hexagon.buzzydrones.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import hexagon.buzzydrones.entity.DroneEntity;

public class DroneModel<T extends DroneEntity> extends EntityModel<T> {

    private final ModelRenderer modelRenderer;

    public DroneModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.modelRenderer = new ModelRenderer(this);
        this.modelRenderer.setPos(0.0F, 19.0F, 0.0F);
        ModelRenderer torso = new ModelRenderer(this, 0, 0);
        torso.setPos(0.0F, 0.0F, 0.0F);
        this.modelRenderer.addChild(torso);
        torso.addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F, 0.0F);
        ModelRenderer leftAntenna = new ModelRenderer(this, 2, 0);
        leftAntenna.setPos(0.0F, -2.0F, -5.0F);
        leftAntenna.addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F);
        ModelRenderer rightAntenna = new ModelRenderer(this, 2, 3);
        rightAntenna.setPos(0.0F, -2.0F, -5.0F);
        rightAntenna.addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F);
        torso.addChild(leftAntenna);
        torso.addChild(rightAntenna);
        ModelRenderer frontLegs = new ModelRenderer(this);
        frontLegs.setPos(1.5F, 3.0F, -2.0F);
        this.modelRenderer.addChild(frontLegs);
        frontLegs.addBox("frontLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0, 0.0F, 26, 1);
        ModelRenderer middleLegs = new ModelRenderer(this);
        middleLegs.setPos(1.5F, 3.0F, 0.0F);
        this.modelRenderer.addChild(middleLegs);
        middleLegs.addBox("midLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0, 0.0F, 26, 3);
        ModelRenderer backLegs = new ModelRenderer(this);
        backLegs.setPos(1.5F, 3.0F, 2.0F);
        this.modelRenderer.addChild(backLegs);
        backLegs.addBox("backLegBox", -5.0F, 0.0F, 0.0F, 7, 2, 0, 0.0F, 26, 5);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
