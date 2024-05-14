package dev.turtywurty.testmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.turtywurty.testmod.client.model.ExampleShieldModel;
import dev.turtywurty.testmod.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ExampleBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    private final ExampleShieldModel shieldModel;

    public static final ExampleBlockEntityWithoutLevelRenderer INSTANCE =
            new ExampleBlockEntityWithoutLevelRenderer(
                    Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                    Minecraft.getInstance().getEntityModels());

    public ExampleBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);

        this.shieldModel = new ExampleShieldModel(modelSet.bakeLayer(ExampleShieldModel.LAYER_LOCATION));
    }

    @Override
    public void renderByItem(@NotNull ItemStack itemStack, @NotNull ItemDisplayContext ctx, @NotNull PoseStack poseStack,
                             @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if(itemStack.is(ItemInit.EXAMPLE_SHIELD.get())) {
            poseStack.pushPose();
            poseStack.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(buffer, this.shieldModel.renderType(ExampleShieldModel.TEXTURE), false, itemStack.hasFoil());
            this.shieldModel.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }
}
