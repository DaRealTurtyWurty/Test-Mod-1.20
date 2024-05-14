package dev.turtywurty.testmod.init;

import dev.turtywurty.testmod.TestMod;
import dev.turtywurty.testmod.block.ExampleBedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TestMod.MODID);

    public static final RegistryObject<ExampleBedBlock> EXAMPLE_BED = BLOCKS.register("example_bed",
            () -> new ExampleBedBlock(BlockBehaviour.Properties.copy(Blocks.RED_BED)));
}
