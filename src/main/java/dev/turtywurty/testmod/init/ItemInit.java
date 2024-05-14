package dev.turtywurty.testmod.init;

import dev.turtywurty.testmod.TestMod;
import dev.turtywurty.testmod.item.ExampleShieldItem;
import dev.turtywurty.testmod.item.FeaturePlacerItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TestMod.MODID);

    public static final RegistryObject<ExampleShieldItem> EXAMPLE_SHIELD = ITEMS.register("example_shield",
            () -> new ExampleShieldItem(new Item.Properties().stacksTo(1).durability(336)));

    public static final RegistryObject<BedItem> EXAMPLE_BED = ITEMS.register("example_bed",
            () -> new BedItem(BlockInit.EXAMPLE_BED.get(), new Item.Properties().stacksTo(1)));

    public static final RegistryObject<FeaturePlacerItem> FEATURE_PLACER = ITEMS.register("feature_placer",
            () -> new FeaturePlacerItem(new Item.Properties().stacksTo(1)));
}
