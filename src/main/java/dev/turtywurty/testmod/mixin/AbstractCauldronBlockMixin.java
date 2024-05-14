package dev.turtywurty.testmod.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.turtywurty.testmod.cauldron.CauldronItemEnterRegistry;
import dev.turtywurty.testmod.cauldron.CauldronType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {
    @ModifyExpressionValue(
            method = "isEntityInsideContent",
            at = @At("RETURN")
    )
    private boolean testmod$isEntityInsideContent(BlockState state, BlockPos pos, Entity entity, boolean original) {
        if(original && entity instanceof ItemEntity itemEntity) {
            var type = CauldronType.fromState(state);

            var callbacks = CauldronItemEnterRegistry.getCallbacks(type);
            for(var callback : callbacks) {
                callback.onItemEnter(state, pos, itemEntity, type);
            }
        }

        return original;
    }
}
