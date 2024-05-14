package dev.turtywurty.testmod.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.turtywurty.testmod.TestMod;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ExampleShieldModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(TestMod.MODID, "example_shield"), "main");

    public static final ResourceLocation TEXTURE =
            new ResourceLocation(TestMod.MODID, "textures/entity/example_shield.png");

    private final ModelPart root;
    private final ModelPart plate;
    private final ModelPart handle;

    public ExampleShieldModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.root = root;
        this.plate = root.getChild("plate");
        this.handle = root.getChild("handle");
    }

    public static LayerDefinition createLayer() {
        var meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -5.0F, -3.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 20).addBox(6.0F, -4.0F, -3.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(17, 20).addBox(-7.0F, -4.0F, -3.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(11, 17).addBox(-5.0F, 7.0F, -3.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(11, 14).addBox(-5.0F, -6.0F, -3.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

        partDefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    public ModelPart plate() {
        return this.plate;
    }

    public ModelPart handle() {
        return this.handle;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer consumer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        this.root.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
