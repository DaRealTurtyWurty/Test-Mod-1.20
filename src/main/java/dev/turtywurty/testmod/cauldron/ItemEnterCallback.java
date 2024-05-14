package dev.turtywurty.testmod.cauldron;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface ItemEnterCallback {
    void onItemEnter(BlockState state, BlockPos pos, ItemEntity entity, CauldronType type);
}
