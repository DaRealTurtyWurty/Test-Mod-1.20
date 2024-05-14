package dev.turtywurty.testmod.init;

import dev.turtywurty.testmod.TestMod;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeTypeInit {
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, TestMod.MODID);

    public static final RegistryObject<RecipeType<CauldronRecipe>> CAULDRON =
            TYPES.register("cauldron", () -> CauldronRecipe.TYPE);
}
