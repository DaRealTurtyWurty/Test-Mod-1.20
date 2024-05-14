package dev.turtywurty.testmod.cauldron;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public enum CauldronType {
    EMPTY,
    WATER,
    LAVA,
    POWDERED_SNOW,
    OTHER;

    public static @NotNull CauldronType fromState(BlockState state) {
        if(state.is(Blocks.LAVA_CAULDRON))
            return LAVA;
        if(state.is(Blocks.POWDER_SNOW_CAULDRON))
            return POWDERED_SNOW;
        if(state.is(Blocks.WATER_CAULDRON))
            return WATER;
        if(state.is(Blocks.CAULDRON))
            return EMPTY;

        return OTHER;
    }
}
